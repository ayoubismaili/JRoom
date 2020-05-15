/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.server.java.dao;

import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.jpa.Conference;
import com.caporal7.jroom.common.java.protoc.JRoomJoinConferenceProtos;
import com.caporal7.jroom.common.java.protoc.JRoomJoinConferenceProtos.JRoomJoinConferenceProbeRequest;
import com.caporal7.jroom.common.java.protoc.JRoomJoinConferenceProtos.JRoomJoinConferenceProbeResponse.ProbeResponseType;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import java.io.IOException;
import java.net.Socket;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ayoub Ismaili
 */
public class JRoomJoinConferenceDao {
    
    private Socket socket;
    
    public JRoomJoinConferenceDao(Socket socket) {
        this.socket = socket;
    }
    
    public void handleProbe(JRoomRequest request) throws IOException, JRoomException 
    {
        JRoomJoinConferenceProbeRequest probRequest = request.getJoinConferenceProb();
        int conferenceId = probRequest.getConferenceId();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JRoomServerPU");
        EntityManager em = emf.createEntityManager();
        Conference conference = em.find(Conference.class, conferenceId);
        ProbeResponseType probeResponseType = ProbeResponseType.INVALID_ID;
        if (conference != null)
        {
            if (conference.getActive())
            {
                probeResponseType = ProbeResponseType.SUCCESS;
            }
        }
        JRoomJoinConferenceProtos.JRoomJoinConferenceProbeResponse probResponse = JRoomJoinConferenceProtos
                .JRoomJoinConferenceProbeResponse.newBuilder()
                .setResponse(probeResponseType)
                .build();
        JRoomResponse response = JRoomResponse.newBuilder()
                .setType(JRoomProtos.Type.JOIN_CONFERENCE_PROB)
                .setJoinConferenceProb(probResponse)
                .build();
        byte[] responseBytes = response.toByteArray();
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(responseBytes.length);
        socket.getOutputStream().write(lengthBytes);
        socket.getOutputStream().write(responseBytes);
    }
    
    
}
