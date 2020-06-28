/*
 * The MIT License
 *
 * Copyright 2020 Ayoub Ismaili <ayoubismaili1@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.caporal7.jroom.server.java.dao;

import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.jpa.Attendance;
import com.caporal7.jroom.common.java.jpa.Conference;
import com.caporal7.jroom.common.java.jpa.DataStream;
import com.caporal7.jroom.common.java.jpa.Session;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenIncomingRequest;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenIncomingResponse;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenOutgoingRequest;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenOutgoingResponse;
import com.caporal7.jroom.common.java.utils.JRoomAttendance;
import com.caporal7.jroom.common.java.utils.JRoomConference;
import com.caporal7.jroom.common.java.utils.JRoomConferenceManager;
import com.caporal7.jroom.common.java.utils.JRoomDataStream;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class ScreenDao {
    
    public static void handleIncoming(JRoomRequest request, Socket socket) throws IOException, JRoomException {
        JRoomScreenIncomingRequest innerRequest = request.getScreenIn();
        int conferenceId = innerRequest.getConferenceId();
        boolean isGuest = innerRequest.getIsGuest();
        String guestAttendeeGuid = innerRequest.getGuestAttendeeGuid();
        int registeredAttendeeId = innerRequest.getRegisteredAttendeeId();
        String sessionCookie = innerRequest.getSessionCookie();
        String accessToken = innerRequest.getAccessToken();
        EntityManager em = JRoomUtils.getEntityManager();
        JRoomScreenIncomingResponse.ResponseType innerResponseType = 
                JRoomScreenIncomingResponse.ResponseType.INVALID_REQUEST;
        ByteString bsData = ByteString.EMPTY;
        System.out.println(guestAttendeeGuid); /* DEBUG */
        try
        {
            JRoomConference jConf = JRoomConferenceManager.findConference(conferenceId);
            JRoomAttendance jAtt = null;
            if (isGuest) {
                jAtt = jConf.findGuestAttendance(guestAttendeeGuid);
            } else {
                jAtt = jConf.findRegisteredAttendance(registeredAttendeeId);
            }
            /* TODO: Check if IP has too many requests status */
            boolean tooManyRequests = false;
            if (tooManyRequests) {
                innerResponseType = JRoomScreenIncomingResponse.ResponseType.TOO_MANY_REQUESTS;
            } else if (isGuest && guestAttendeeGuid.equals("0")) { 
                /* Usage of "0" as GUID is forbidden because it could allow arbitraty access */
                innerResponseType = JRoomScreenIncomingResponse.ResponseType.INVALID_REQUEST;
                System.out.println("Invalid request"); /* DEBUG */
            } else if (!isGuest && registeredAttendeeId == 0) { 
                /* Usage of 0 as ID is forbidden because it could allow arbitraty access */
                innerResponseType = JRoomScreenIncomingResponse.ResponseType.INVALID_REQUEST;
            } else if (jAtt == null) {
                innerResponseType = JRoomScreenIncomingResponse.ResponseType.INVALID_CONFERENCE_ID_OR_NO_SUCH_ATTENDEE;
                System.out.println("Attendance not found"); /* DEBUG */
            } else {
                if (!jAtt.getAccessToken().equals(accessToken)) {
                    innerResponseType = JRoomScreenIncomingResponse.ResponseType.INVALID_ACCESS_TOKEN;
                    System.out.println("Invalid access token"); /* DEBUG */
                } else {
                    innerResponseType = JRoomScreenIncomingResponse.ResponseType.SUCCESS;
                    System.out.println("Success"); /* DEBUG */
                }
            }
            if (innerResponseType == JRoomScreenIncomingResponse.ResponseType.SUCCESS) {
                //Bug Fix: We were using the attendance of the actual Attendee, and not the animator attendance
                JRoomAttendance animatorAttendance = jConf.getAnimatorAttendance();
                JRoomDataStream ds = animatorAttendance.getScreenStream();
                byte[] data = ds.pop();
                if (data == null) {
                    bsData = ByteString.EMPTY;
                    System.out.println("Empty"); /* DEBUG */
                } else {
                    bsData = ByteString.copyFrom(data);
                    System.out.println("Got data"); /* DEBUG */
                }
            }
        } catch (Exception ex) {
            innerResponseType = JRoomScreenIncomingResponse.ResponseType.INTERNAL_ERROR;
        }
        JRoomScreenIncomingResponse innerResponse = JRoomScreenIncomingResponse.newBuilder()
                .setType(innerResponseType)
                .setData(bsData)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.SCREEN_IN)
                .setScreenIn(innerResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(responseBytes);
    }

    public static void handleOutgoing(JRoomRequest request, Socket socket) throws IOException {
        JRoomScreenOutgoingRequest innerRequest = request.getScreenOut();
        int conferenceId = innerRequest.getConferenceId();
        boolean isGuest = innerRequest.getIsGuest();
        String guestAttendeeGuid = innerRequest.getGuestAttendeeGuid();
        int registeredAttendeeId = innerRequest.getRegisteredAttendeeId();
        String sessionCookie = innerRequest.getSessionCookie();
        String accessToken = innerRequest.getAccessToken();
        byte[] data = innerRequest.getData().toByteArray();
        EntityManager em = JRoomUtils.getEntityManager();
        JRoomScreenOutgoingResponse.ResponseType innerResponseType = 
                JRoomScreenOutgoingResponse.ResponseType.INVALID_REQUEST;
        try
        {
            JRoomConference jConf = JRoomConferenceManager.findConference(conferenceId);
            JRoomAttendance jAtt = null;
            if (isGuest) {
                jAtt = jConf.findGuestAttendance(guestAttendeeGuid);
            } else {
                jAtt = jConf.findRegisteredAttendance(registeredAttendeeId);
            }
            /* TODO: Check if IP has too many requests status */
            boolean tooManyRequests = false;
            if (tooManyRequests) {
                innerResponseType = JRoomScreenOutgoingResponse.ResponseType.TOO_MANY_REQUESTS;
            } else if (isGuest && guestAttendeeGuid.equals("0")) { 
                /* Usage of "0" as GUID is forbidden because it could allow arbitraty access */
                innerResponseType = JRoomScreenOutgoingResponse.ResponseType.INVALID_REQUEST;
            } else if (!isGuest && registeredAttendeeId == 0) { 
                /* Usage of 0 as ID is forbidden because it could allow arbitraty access */
                innerResponseType = JRoomScreenOutgoingResponse.ResponseType.INVALID_REQUEST;
            } else if (jAtt == null) {
                innerResponseType = JRoomScreenOutgoingResponse.ResponseType.INVALID_CONFERENCE_ID_OR_NO_SUCH_ATTENDEE;
            } else {
                if (!jAtt.getAccessToken().equals(accessToken)) {
                    innerResponseType = JRoomScreenOutgoingResponse.ResponseType.INVALID_ACCESS_TOKEN;
                } else  {
                    innerResponseType = JRoomScreenOutgoingResponse.ResponseType.SUCCESS;
                }
            }
            if (innerResponseType == JRoomScreenOutgoingResponse.ResponseType.SUCCESS) {
                /* Check if the data is coming from the real animator */
                //TypedQuery<Conference> cQuery;
                boolean isAnimator = false;
                if (isGuest) {
                    isAnimator = jConf.getAnimatorAttendance().getGuestAttendeeGuid().equals(guestAttendeeGuid);
                } else {
                    isAnimator = jConf.getAnimatorAttendance().getRegisteredAttendeeId() == registeredAttendeeId;
                }
                if (isAnimator) {
                    /* The user is the real animator, let's store the received data */
                    jConf.getAnimatorAttendance().getScreenStream().push(data);
                } else {
                    /* Data is not coming from real attendee, let's discard it */
                    innerResponseType = JRoomScreenOutgoingResponse.ResponseType.ACCESS_DENIED;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            innerResponseType = JRoomScreenOutgoingResponse.ResponseType.INTERNAL_ERROR;
        }
        JRoomScreenOutgoingResponse innerResponse = JRoomScreenOutgoingResponse.newBuilder()
                .setType(innerResponseType)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.SCREEN_OUT)
                .setScreenOut(innerResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(responseBytes);
    }
}
