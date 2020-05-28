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
import com.caporal7.jroom.common.java.jpa.RegisteredAttendee;
import com.caporal7.jroom.common.java.jpa.Session;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeAuthRequest;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeAuthResponse;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeRegisterRequest;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeRegisterResponse;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenIncomingRequest;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenIncomingResponse;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenOutgoingRequest;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenOutgoingResponse;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import com.google.protobuf.ByteString;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ScreenDao {
    
    public static void handleIncoming(JRoomRequest request, Socket socket) throws IOException, JRoomException 
    {
        JRoomScreenIncomingRequest innerRequest = request.getScreenIn();
        int conferenceId = innerRequest.getConferenceId();
        boolean isGuest = innerRequest.getIsGuest();
        String guestAttendeeGuid = innerRequest.getGuestAttendeeGuid();
        int registeredAttendeeId = innerRequest.getRegisteredAttendeeId();
        String sessionCookie = innerRequest.getSessionCookie();
        String accessToken = innerRequest.getAccessToken();
        
        EntityManager em = JRoomUtils.getEntityManager();
        TypedQuery<Attendance> aQuery;
        if (isGuest) {
            aQuery = em.createQuery(
                "SELECT a FROM Attendance AS a WHERE a.conference.id = :conferenceId AND a.guestAttendee.guid = :guid",
                Attendance.class);
            aQuery.setParameter("guid", guestAttendeeGuid);
        } else {
            aQuery = em.createQuery(
                "SELECT a FROM Attendance AS a WHERE a.conference.id = :conferenceId AND a.registeredAttendee.id = :id",
                Attendance.class);
            aQuery.setParameter("id", registeredAttendeeId);
        }
        aQuery.setParameter("conferenceId", conferenceId);
        List<Attendance> aResults = aQuery.getResultList();
        
        JRoomScreenIncomingResponse.ResponseType innerResponseType = 
                JRoomScreenIncomingResponse.ResponseType.INVALID_REQUEST;
        
        /* TODO: Check if IP has too many requests status */
        boolean tooManyRequests = false;
        ByteString bsData = ByteString.EMPTY;
        if (tooManyRequests) {
            innerResponseType = JRoomScreenIncomingResponse.ResponseType.TOO_MANY_REQUESTS;
        } else if (aResults.isEmpty()) {
            innerResponseType = JRoomScreenIncomingResponse.ResponseType.INVALID_CONFERENCE_ID_OR_NO_SUCH_ATTENDEE;
        } else {
            /* Get the Attendance */
            Attendance a = aResults.get(0);
            if (!a.getAccessToken().equals(accessToken)) {
                innerResponseType = JRoomScreenIncomingResponse.ResponseType.INVALID_ACCESS_TOKEN;
            } else if (!isGuest) {
                /* Validate the registered user session */
                TypedQuery<Session> sQuery = em.createQuery(
                    "SELECT s FROM Session AS s WHERE s.registeredAttendeeId = :registeredAttendeeId AND s.cookie = :session AND s.expired = :expired",
                    Session.class);
                sQuery.setParameter("registeredAttendeeId", registeredAttendeeId);
                sQuery.setParameter("session", sessionCookie);
                sQuery.setParameter("expired", false);
                List<Session> sResults = sQuery.getResultList();
                if (sResults.isEmpty()) {
                    innerResponseType = JRoomScreenIncomingResponse.ResponseType.SESSION_INVALID_OR_EXPIRED;
                }
            } else {
                FileInputStream fis = new FileInputStream("C:\\Users\\sphere\\Desktop\\WindowsXP.jpg");
                bsData = ByteString.readFrom(fis, 1024);
                fis.close();
                innerResponseType = JRoomScreenIncomingResponse.ResponseType.SUCCESS;
            }
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
        TypedQuery<Attendance> aQuery;
        if (isGuest) {
            aQuery = em.createQuery(
                "SELECT a FROM Attendance AS a WHERE a.conference.id = :conferenceId AND a.guestAttendee.guid = :guid",
                Attendance.class);
            aQuery.setParameter("guid", guestAttendeeGuid);
        } else {
            aQuery = em.createQuery(
                "SELECT a FROM Attendance AS a WHERE a.conference.id = :conferenceId AND a.registeredAttendee.id = :id",
                Attendance.class);
            aQuery.setParameter("id", registeredAttendeeId);
        }
        aQuery.setParameter("conferenceId", conferenceId);
        List<Attendance> aResults = aQuery.getResultList();
        
        JRoomScreenOutgoingResponse.ResponseType innerResponseType = 
                JRoomScreenOutgoingResponse.ResponseType.INVALID_REQUEST;
        
        /* TODO: Check if IP has too many requests status */
        boolean tooManyRequests = false;
        if (tooManyRequests) {
            innerResponseType = JRoomScreenOutgoingResponse.ResponseType.TOO_MANY_REQUESTS;
        } else if (aResults.isEmpty()) {
            innerResponseType = JRoomScreenOutgoingResponse.ResponseType.INVALID_CONFERENCE_ID_OR_NO_SUCH_ATTENDEE;
        } else {
            /* Get the Attendance */
            Attendance a = aResults.get(0);
            if (!a.getAccessToken().equals(accessToken)) {
                innerResponseType = JRoomScreenOutgoingResponse.ResponseType.INVALID_ACCESS_TOKEN;
            } else if (!isGuest) {
                /* Validate the registered user session */
                TypedQuery<Session> sQuery = em.createQuery(
                    "SELECT s FROM Session AS s WHERE s.registeredAttendeeId = :registeredAttendeeId AND s.cookie = :session AND s.expired = :expired",
                    Session.class);
                sQuery.setParameter("registeredAttendeeId", registeredAttendeeId);
                sQuery.setParameter("session", sessionCookie);
                sQuery.setParameter("expired", false);
                List<Session> sResults = sQuery.getResultList();
                if (sResults.isEmpty()) {
                    innerResponseType = JRoomScreenOutgoingResponse.ResponseType.SESSION_INVALID_OR_EXPIRED;
                }
            } else {
                innerResponseType = JRoomScreenOutgoingResponse.ResponseType.SUCCESS;
            }
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
