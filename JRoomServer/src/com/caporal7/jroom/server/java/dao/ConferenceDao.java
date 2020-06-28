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
import com.caporal7.jroom.common.java.jpa.DataStream;
import com.caporal7.jroom.common.java.jpa.Discussion;
import com.caporal7.jroom.common.java.jpa.GuestAttendee;
import com.caporal7.jroom.common.java.jpa.RegisteredAttendee;
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
import com.caporal7.jroom.common.java.utils.JRoomAttendance;
import com.caporal7.jroom.common.java.utils.JRoomConference;
import com.caporal7.jroom.common.java.utils.JRoomConferenceManager;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    
    public static void handleAuth(JRoomRequest request, Socket socket) 
        throws IOException, JRoomException 
    {
        JRoomJoinConferenceAuthRequest innerRequest = request.getJoinConferenceAuth();
        int conferenceId = innerRequest.getConferenceId();
        int password = innerRequest.getPassword();
        boolean isGuest = innerRequest.getIsGuest();
        int registeredAttendeeId = innerRequest.getRegisteredAttendeeId();
        String guestAttendeeGuid = innerRequest.getGuestAttendeeGuid();
        EntityManager em = JRoomUtils.getEntityManager();
       
        TypedQuery<Conference> query;
        // TODO: Implement bruteforce guard
        query = em.createQuery(
            "SELECT c FROM Conference AS c WHERE c.id = :id AND c.password = :password",
            Conference.class);
        query.setParameter("id", conferenceId);
        query.setParameter("password", password);
        List<Conference> results = query.getResultList();
        AuthResponseType innerResponseType = AuthResponseType.INVALID_REQUEST;
        String accessToken = "";
        if (results.size() > 0)
        {
            Conference conference = results.get(0);
            if (!conference.getActive()) {
                innerResponseType = AuthResponseType.INVALID_ID;
            } else {
                /* Create an Attendance in the Conference for the actual Attendee if it does not exist */
                AttendancePK pk = new AttendancePK();
                if (isGuest) {
                    registeredAttendeeId = 0;
                } else {
                    guestAttendeeGuid = "0";
                }
                pk.setConferenceId(conferenceId);
                pk.setRegisteredAttendeeId(registeredAttendeeId);
                pk.setGuestAttendeeGuid(guestAttendeeGuid);
                TypedQuery<Attendance> query2 = em.createQuery(
                    "SELECT a FROM Attendance AS a WHERE a.attendancePK.conferenceId = :conferenceId AND a.attendancePK.registeredAttendeeId = :registeredAttendeeId AND a.attendancePK.guestAttendeeGuid = :guestAttendeeGuid",
                    Attendance.class);
                query2.setParameter("conferenceId", conferenceId);
                query2.setParameter("registeredAttendeeId", registeredAttendeeId);
                query2.setParameter("guestAttendeeGuid", guestAttendeeGuid);
                List<Attendance> results2 = query2.getResultList();
                if (results2.size() > 0) {
                    accessToken = results2.get(0).getAccessToken();
                    //System.out.println("Damn, why this ?");
                } else {
                    /* If guest, try to persist the GUID */
                    if (isGuest) {
                        System.out.println("=================== BGEIN GUEST REGISTER");
                        try {
                            GuestAttendee ga = new GuestAttendee(guestAttendeeGuid);
                            
                            EntityTransaction et = em.getTransaction();
                            et.begin();
                            em.persist(ga);
                            et.commit();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Exception while registering GuestAttendee");
                        }
                        System.out.println("=================== END GUEST REGISTER");
                    }
                    Attendance att = new Attendance();
                    att.setAttendancePK(pk);
                    accessToken = JRoomUtils.getNewAccessToken();
                    att.setAccessToken(accessToken);
                    em.getTransaction().begin();
                    /* Create 3 DataStreams for the actual Attendance */
                    DataStream screenDs = new DataStream();
                    DataStream voiceDs = new DataStream();
                    DataStream cameraDs = new DataStream();
                    em.persist(screenDs);
                    em.persist(voiceDs);
                    em.persist(cameraDs);
                    em.getTransaction().commit();
                    /* Create the Attendance */
                    em.getTransaction().begin();
                    att.setScreenStreamId(screenDs.getId());
                    att.setVoiceStreamId(voiceDs.getId());
                    att.setCameraStreamId(cameraDs.getId());
                    em.persist(att);
                    em.getTransaction().commit();
                    /* Update the cache system */
                    JRoomConference jConf = JRoomConferenceManager.findConference(conferenceId);
                    if (jConf == null) {
                        int confId = conference.getId();
                        int confPassword = conference.getPassword();
                        int confOwnerId = -1;
                        RegisteredAttendee owner = conference.getOwnerId();
                        if (owner != null) {
                            confOwnerId = owner.getId();
                        }
                        int confDiscussionId = -1;
                        Discussion discussion = conference.getDiscussionId();
                        if (discussion != null) {
                            confDiscussionId = discussion.getId();
                        }
                        int confRegisteredAnimatorId = -1;
                        RegisteredAttendee registeredAnimator = conference.getRegisteredAnimatorId();
                        if (registeredAnimator != null) {
                            confRegisteredAnimatorId = registeredAnimator.getId();
                        }
                        String confGuestAnimatorGuid = null;
                        GuestAttendee guestAnimator = conference.getGuestAnimatorGuid();
                        if (guestAnimator != null) {
                            confGuestAnimatorGuid = guestAnimator.getGuid();
                        }
                        jConf = new JRoomConference(confId, confPassword, 
                                confOwnerId, confDiscussionId, confRegisteredAnimatorId, 
                                confGuestAnimatorGuid);
                        JRoomConferenceManager.addConference(jConf);
                    }
                    JRoomAttendance ja = new JRoomAttendance(registeredAttendeeId, 
                            guestAttendeeGuid, accessToken, voiceDs.getId(), 
                            cameraDs.getId(), screenDs.getId());
                    jConf.addAttendance(ja);
                }
                innerResponseType = AuthResponseType.SUCCESS;
            }
        }
        JRoomJoinConferenceAuthResponse authResponse = 
                JRoomJoinConferenceAuthResponse.newBuilder()
                .setAccessToken(accessToken)
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
