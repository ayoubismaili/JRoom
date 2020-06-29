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
import com.caporal7.jroom.common.java.jpa.Conference;
import com.caporal7.jroom.common.java.jpa.RegisteredAttendee;
import com.caporal7.jroom.common.java.jpa.Session;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeAuthRequest;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeAuthResponse;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeRegisterRequest;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeRegisterResponse;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeSessionHeartbeatRequest;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeSessionHeartbeatResponse;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class AttendeeDao {
    
    public static void handleRegister(JRoomRequest request, Socket socket) throws IOException, JRoomException 
    {
        JRoomAttendeeRegisterRequest innerRequest = request.getAttendeeRegister();
        String email = innerRequest.getEmail();
        String password = innerRequest.getPassword();
        EntityManager em = JRoomUtils.getEntityManager();
        
        TypedQuery<RegisteredAttendee> query = em.createQuery(
            "SELECT ra FROM RegisteredAttendee AS ra WHERE ra.email = :email",
            RegisteredAttendee.class);
        query.setParameter("email", email);
        List<RegisteredAttendee> results = query.getResultList();
        
        JRoomAttendeeRegisterResponse.ResponseType innerResponseType = 
                JRoomAttendeeRegisterResponse.ResponseType.INVALID_REQUEST;
        
        /* TODO: Check if IP has too many requests status */
        boolean tooManyRequests = false;
        if (tooManyRequests) {
            innerResponseType = JRoomAttendeeRegisterResponse.ResponseType.TOO_MANY_REQUESTS;
        } else if (!results.isEmpty()) {
            innerResponseType = JRoomAttendeeRegisterResponse.ResponseType.ALREADY_REGISTERED;
        } else { 
            EntityTransaction et = em.getTransaction();
            et.begin();
            /* Create personal Conference for later use */
            Conference c = new Conference();
            c.setPassword(JRoomUtils.getNewConferencePassword());
            c.setActive(true);
            em.persist(c);
            /* Attendee can register */
            RegisteredAttendee ra = new RegisteredAttendee();
            ra.setEmail(email);
            ra.setPassword(password);
            ra.setPersonalConferenceId(c);
            em.persist(ra);
            /* Update Conference - Set default Owner and Animator */
            c.setOwnerId(ra);
            c.setRegisteredAnimatorId(ra);
            em.merge(c);
            et.commit();
            innerResponseType = JRoomAttendeeRegisterResponse.ResponseType.SUCCESS;
        }
        JRoomAttendeeRegisterResponse innerResponse = JRoomAttendeeRegisterResponse.newBuilder()
                .setType(innerResponseType)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.ATTENDEE_REG)
                .setAttendeeRegister(innerResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(responseBytes);
    }

    public static void handleAuth(JRoomRequest request, Socket socket) throws IOException {
        JRoomAttendeeAuthRequest innerRequest = request.getAttendeeAuth();
        String email = innerRequest.getEmail();
        String password = innerRequest.getPassword();
        EntityManager em = JRoomUtils.getEntityManager();
        
        TypedQuery<RegisteredAttendee> query = em.createQuery(
            "SELECT ra FROM RegisteredAttendee AS ra WHERE ra.email = :email AND ra.password = :password",
            RegisteredAttendee.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        List<RegisteredAttendee> results = query.getResultList();
        
        JRoomAttendeeAuthResponse.ResponseType innerResponseType = 
                JRoomAttendeeAuthResponse.ResponseType.INVALID_REQUEST;
        
        /* TODO: Check if IP has too many requests status */
        boolean tooManyRequests = false;
        /* The registered attendee id */
        int registeredAttendeeId = 0;
        /* The session cookie */
        String sessionCookie = "";
        if (tooManyRequests) {
            innerResponseType = JRoomAttendeeAuthResponse.ResponseType.TOO_MANY_REQUESTS;
        } else if (results.isEmpty()) {
            innerResponseType = JRoomAttendeeAuthResponse.ResponseType.INVALID_EMAIL_AND_OR_PASSWORD;
        } else { 
            /* Attendee email & password are correct. Let's create a session */
            RegisteredAttendee ra = results.get(0);
            registeredAttendeeId = ra.getId();
            sessionCookie = JRoomUtils.getNewSessionCookie();
            Session s = new Session();
            s.setCookie(sessionCookie);
            s.setRegisteredAttendeeId(ra);
            s.setExpired(false);
            
            em.getTransaction().begin();
            em.persist(s);
            em.getTransaction().commit();
            innerResponseType = JRoomAttendeeAuthResponse.ResponseType.SUCCESS;
        }
        JRoomAttendeeAuthResponse innerResponse = JRoomAttendeeAuthResponse.newBuilder()
                .setType(innerResponseType)
                .setRegisteredAttendeeId(registeredAttendeeId)
                .setSessionCookie(sessionCookie)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.ATTENDEE_AUTH)
                .setAttendeeAuth(innerResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(responseBytes);
    }
    
    public static void handleSessionHeartbeat(JRoomRequest request, Socket socket) throws IOException {
        JRoomAttendeeSessionHeartbeatRequest innerRequest = request.getAttendeeSessionHeartbeat();
        int registeredAttendeeId = innerRequest.getRegisteredAttendeeId();
        String sessionCookie = innerRequest.getSessionCookie();
        EntityManager em = JRoomUtils.getEntityManager();
        
        TypedQuery<RegisteredAttendee> query = em.createQuery(
            "SELECT s FROM Session AS s WHERE s.registeredAttendeeId.id = :registeredAttendeeId AND s.cookie = :cookie AND s.expired = :expired",
            RegisteredAttendee.class);
        query.setParameter("registeredAttendeeId", registeredAttendeeId);
        query.setParameter("cookie", sessionCookie);
        query.setParameter("expired", false);
        List<RegisteredAttendee> results = query.getResultList();
        
        JRoomAttendeeSessionHeartbeatResponse.ResponseType innerResponseType = 
                JRoomAttendeeSessionHeartbeatResponse.ResponseType.INVALID_REQUEST;
        
        /* TODO: Check if IP has too many requests status */
        boolean tooManyRequests = false;
        if (tooManyRequests) {
            innerResponseType = JRoomAttendeeSessionHeartbeatResponse.ResponseType.TOO_MANY_REQUESTS;
        } else if (results.isEmpty()) {
            innerResponseType = JRoomAttendeeSessionHeartbeatResponse.ResponseType.SESSION_INVALID_OR_EXPIRED;
        } else { 
            /* Return success since session is valide */
            innerResponseType = JRoomAttendeeSessionHeartbeatResponse.ResponseType.SUCCESS;
        }
        JRoomAttendeeSessionHeartbeatResponse innerResponse = JRoomAttendeeSessionHeartbeatResponse.newBuilder()
                .setType(innerResponseType)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.ATTENDEE_SESSION_HEARTBEAT)
                .setAttendeeSessionHeartbeat(innerResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(responseBytes);
    }
}
