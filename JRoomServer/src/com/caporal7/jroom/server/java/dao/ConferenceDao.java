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
import com.caporal7.jroom.common.java.jpa.AttendancePK;
import com.caporal7.jroom.common.java.jpa.Conference;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomGetPersonalConferenceRequest;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomGetPersonalConferenceResponse;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceAuthRequest;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceAuthResponse;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceAuthResponse.AuthResponseType;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceProbeRequest;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceProbeResponse.ProbeResponseType;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ConferenceDao {
    
    public static void handleProbe(JRoomRequest request, Socket socket) throws IOException, JRoomException 
    {
        JRoomJoinConferenceProbeRequest probRequest = request.getJoinConferenceProb();
        int conferenceId = probRequest.getConferenceId();
        EntityManager em = JRoomUtils.getEntityManager();
        Conference conference = em.find(Conference.class, conferenceId);
        ProbeResponseType probeResponseType = ProbeResponseType.INVALID_ID;
        if (conference != null)
        {
            if (conference.getActive())
            {
                probeResponseType = ProbeResponseType.SUCCESS;
            }
        }
        JRoomConferenceProtos.JRoomJoinConferenceProbeResponse probResponse = JRoomConferenceProtos
                .JRoomJoinConferenceProbeResponse.newBuilder()
                .setResponse(probeResponseType)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.JOIN_CONFERENCE_PROB)
                .setJoinConferenceProb(probResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(responseBytes);
    }
    
    public static void handleAuth(JRoomRequest request, Socket socket) throws IOException, JRoomException 
    {
        JRoomJoinConferenceAuthRequest innerRequest = request.getJoinConferenceAuth();
        int conferenceId = innerRequest.getConferenceId();
        int password = innerRequest.getPassword();
        boolean isGuest = innerRequest.getIsGuest();
        //int registeredAttendeeId = ...
        //String guestAttendeeGuid = ...
        EntityManager em = JRoomUtils.getEntityManager();
        Conference conference = em.find(Conference.class, conferenceId);
        AuthResponseType innerResponseType = AuthResponseType.INVALID_ID;
        if (conference != null)
        {
            if (conference.getPassword() != password) {
                innerResponseType = AuthResponseType.INVALID_PASS;
                // TODO: Implement bruteforce guard
            } else if (!conference.getActive()) {
                innerResponseType = AuthResponseType.INVALID_ID;
            } else {
                /* Create an Attendance in the Conference for the actual Attendee */
                AttendancePK pk = new AttendancePK();
                pk.setConferenceId(conferenceId);
                
                Attendance att = new Attendance();
            }
        }
        JRoomJoinConferenceAuthResponse authResponse = 
                JRoomJoinConferenceAuthResponse.newBuilder()
                .setResponse(innerResponseType)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.JOIN_CONFERENCE_AUTH)
                .setJoinConferenceAuth(authResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(responseBytes);
    }

    public static void handleGetPersoConf(JRoomRequest request, Socket socket) throws IOException {
        JRoomGetPersonalConferenceRequest innerRequest = request.getGetPersoConf();
        int registeredAttendeeId = innerRequest.getRegisteredAttendeeId();
        String sessionCookie = innerRequest.getSessionCookie();
        EntityManager em = JRoomUtils.getEntityManager();
       
        TypedQuery<Conference> query = em.createQuery(
            "SELECT s.registeredAttendeeId.personalConferenceId FROM Session AS s WHERE s.registeredAttendeeId.id = :registeredAttendeeId AND s.cookie = :cookie AND s.expired = :expired",
            Conference.class);
        query.setParameter("registeredAttendeeId", registeredAttendeeId);
        query.setParameter("cookie", sessionCookie);
        query.setParameter("expired", false);
        List<Conference> results = query.getResultList();
        int conferenceId = 0;
        int password = 0;
        JRoomGetPersonalConferenceResponse.ResponseType innerReponseType = JRoomGetPersonalConferenceResponse.ResponseType.INVALID_REQUEST;
        
        /* TODO: Check if IP has too many requests status */
        boolean tooManyRequests = false;
        if (tooManyRequests) {
            //innerReponseType = JRoomGetPersonalConferenceResponse.ResponseType.T;
        } else if (results.isEmpty()) {
            innerReponseType = JRoomGetPersonalConferenceResponse.ResponseType.SESSION_INVALID_OR_EXPIRED;
        } else {
            Conference c = results.get(0);
            conferenceId = c.getId();
            password = c.getPassword();
            innerReponseType = JRoomGetPersonalConferenceResponse.ResponseType.SUCCESS;
        }
        
        JRoomGetPersonalConferenceResponse innerResponse = 
                JRoomGetPersonalConferenceResponse.newBuilder()
                .setResponse(innerReponseType)
                .setConferenceId(conferenceId)
                .setPassword(password)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.GET_PERSONAL_CONFERENCE)
                .setGetPersoConf(innerResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(responseBytes);
    }
}
