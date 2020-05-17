/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.server.java.dao;

import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.jpa.Conference;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos;
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

import javax.persistence.EntityManager;

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
        JRoomJoinConferenceAuthRequest authRequest = request.getJoinConferenceAuth();
        int conferenceId = authRequest.getConferenceId();
        int password = authRequest.getPassword();
        EntityManager em = JRoomUtils.getEntityManager();
        Conference conference = em.find(Conference.class, conferenceId);
        AuthResponseType authResponseType = AuthResponseType.INVALID_ID;
        if (conference != null)
        {
            if (conference.getActive()) {
                authResponseType = AuthResponseType.SUCCESS;
            }
            if (conference.getPassword() != password) {
                authResponseType = AuthResponseType.INVALID_PASS;
                // TODO: Implement bruteforce guard
            }
        }
        JRoomJoinConferenceAuthResponse authResponse = 
                JRoomJoinConferenceAuthResponse.newBuilder()
                .setResponse(authResponseType)
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
    
}
