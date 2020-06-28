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

package com.caporal7.jroom.client.java.dao;

import com.caporal7.jroom.client.java.services.JRoomClient;
import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomGetPersonalConferenceRequest;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomGetPersonalConferenceResponse;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceAuthRequest;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceAuthResponse;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceProbeRequest;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceProbeResponse.ProbeResponseType;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import java.io.IOException;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConferenceDao {
    
    private JRoomClient client;
    
    public ConferenceDao() throws IOException, ConfigurationException, JRoomException {
        client = new JRoomClient();
    }
    
    public ProbeResponseType probe(int conferenceId) 
            throws IOException, JRoomException {
        JRoomJoinConferenceProbeRequest innerRequest = 
                JRoomJoinConferenceProbeRequest.newBuilder()
                .setConferenceId(conferenceId)
                .build();
        JRoomRequest request = JRoomRequest.newBuilder()
                .setType(JRoomProtos.Type.JOIN_CONFERENCE_PROB)
                .setJoinConferenceProb(innerRequest)
                .build();
        byte[] requestBytes = request.toByteArray();     
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(requestBytes.length);
        
        client.write(lengthBytes);
        client.write(requestBytes);
        client.flush();
        
        lengthBytes = client.readNBytes(4);
        int length = JRoomUtils.convertBytesToInt(lengthBytes);
        byte[] responseBytes = client.readNBytes(length);
        
        JRoomResponse response = JRoomResponse.parseFrom(responseBytes);
        if (response.getType() != JRoomProtos.Type.JOIN_CONFERENCE_PROB) {
            throw new JRoomException("Invalid response");
        }
        return response.getJoinConferenceProb().getResponse();
    }
    
    public JRoomJoinConferenceAuthResponse auth(
        int conferenceId, int password, int registeredAttendeeId, 
        String guestAttendeeGuid, boolean isGuest, String sessionCookie) 
            throws IOException, JRoomException 
    {
        JRoomJoinConferenceAuthRequest innerRequest = 
                JRoomJoinConferenceAuthRequest.newBuilder()
                .setConferenceId(conferenceId)
                .setPassword(password)
                .setRegisteredAttendeeId(registeredAttendeeId)
                .setGuestAttendeeGuid(guestAttendeeGuid)
                .setIsGuest(isGuest)
                .setSessionCookie(sessionCookie)
                .build();
        JRoomRequest request = JRoomRequest.newBuilder()
                .setType(JRoomProtos.Type.JOIN_CONFERENCE_AUTH)
                .setJoinConferenceAuth(innerRequest)
                .build();
        byte[] requestBytes = request.toByteArray();     
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(requestBytes.length);
        
        client.write(lengthBytes);
        client.write(requestBytes);
        client.flush();
        
        lengthBytes = client.readNBytes(4);
        int length = JRoomUtils.convertBytesToInt(lengthBytes);
        byte[] responseBytes = client.readNBytes(length);
        
        JRoomResponse response = JRoomResponse.parseFrom(responseBytes);
        if (response.getType() != JRoomProtos.Type.JOIN_CONFERENCE_AUTH) {
            throw new JRoomException("Invalid response");
        }
        return response.getJoinConferenceAuth();
    }
    
    public JRoomGetPersonalConferenceResponse getPersonalConference(
            int registeredAttendeeId, String sessionCookie) 
                throws IOException, JRoomException {
        JRoomGetPersonalConferenceRequest innerRequest = 
                JRoomGetPersonalConferenceRequest.newBuilder()
                .setRegisteredAttendeeId(registeredAttendeeId)
                .setSessionCookie(sessionCookie)
                .build();
        JRoomRequest request = JRoomRequest.newBuilder()
                .setType(JRoomProtos.Type.GET_PERSONAL_CONFERENCE)
                .setGetPersoConf(innerRequest)
                .build();
        byte[] requestBytes = request.toByteArray();     
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(requestBytes.length);
        
        client.write(lengthBytes);
        client.write(requestBytes);
        client.flush();
        
        lengthBytes = client.readNBytes(4);
        int length = JRoomUtils.convertBytesToInt(lengthBytes);
        byte[] responseBytes = client.readNBytes(length);
        
        JRoomResponse response = JRoomResponse.parseFrom(responseBytes);
        if (response.getType() != JRoomProtos.Type.GET_PERSONAL_CONFERENCE) {
            throw new JRoomException("Invalid response");
        }
        return response.getGetPersoConf();
    }
}
