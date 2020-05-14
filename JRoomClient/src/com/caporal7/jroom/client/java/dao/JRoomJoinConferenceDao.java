/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.client.java.dao;

import com.caporal7.jroom.common.java.JRoomSettings;
import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.protoc.JRoomJoinConferenceProtos.JRoomJoinConferenceProbeRequest;
import com.caporal7.jroom.common.java.protoc.JRoomJoinConferenceProtos.JRoomJoinConferenceProbeResponse.ProbeResponseType;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 *
 * @author sphere
 */
public class JRoomJoinConferenceDao {
    
    private Socket socket;
    
    public JRoomJoinConferenceDao() throws IOException {
        socket = new Socket(JRoomSettings.HOST, JRoomSettings.PORT);
    }
    
    public ProbeResponseType probe(int conferenceId) throws IOException, JRoomException {
        JRoomJoinConferenceProbeRequest probRequest = 
                JRoomJoinConferenceProbeRequest.newBuilder()
                .setConferenceId(conferenceId)
                .build();
        JRoomRequest request = JRoomRequest.newBuilder()
                .setType(JRoomProtos.Type.JOIN_CONFERENCE_AUTH)
                .setJoinConferenceProb(probRequest)
                .build();
        byte[] requestBytes = request.toByteArray();     
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(requestBytes.length);
        //System.out.println("proto.len = " + requestBytes.length);
        socket.getOutputStream().write(lengthBytes);
        socket.getOutputStream().write(requestBytes);
        socket.getOutputStream().flush();

        JRoomResponse response = JRoomResponse.parseFrom(socket.getInputStream());
        if (response.getType() != JRoomProtos.Type.JOIN_CONFERENCE_PROB) {
            throw new JRoomException("Invalid response");
        }
        return response.getJoinConferenceProb().getResponse();
    }
    
    
}
