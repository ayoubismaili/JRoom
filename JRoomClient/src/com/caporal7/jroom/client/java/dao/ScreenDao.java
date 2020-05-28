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
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenIncomingRequest;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenIncomingResponse;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenOutgoingRequest;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenOutgoingResponse;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import com.google.protobuf.ByteString;
import java.io.IOException;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ScreenDao {
    
    private JRoomClient client;
    
    public ScreenDao() throws IOException, ConfigurationException, JRoomException {
        client = new JRoomClient();
    }
    
    public JRoomScreenIncomingResponse incoming(int conferenceId, String guestAttendeeGuid, 
            int registeredAttendeeId, boolean isGuest, String sessionCookie, 
            String accessToken) throws IOException, JRoomException {
        JRoomScreenIncomingRequest innerRequest = 
                JRoomScreenIncomingRequest.newBuilder()
                .setConferenceId(conferenceId)
                .setGuestAttendeeGuid(guestAttendeeGuid)
                .setRegisteredAttendeeId(registeredAttendeeId)
                .setIsGuest(isGuest)
                .setSessionCookie(sessionCookie)
                .setAccessToken(accessToken)
                .build();
        JRoomRequest request = JRoomRequest.newBuilder()
                .setType(JRoomProtos.Type.SCREEN_IN)
                .setScreenIn(innerRequest)
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
        if (response.getType() != JRoomProtos.Type.SCREEN_IN) {
            throw new JRoomException("Invalid response");
        }
        return response.getScreenIn();
    }
    
    public JRoomScreenOutgoingResponse outgoing(int conferenceId, String guestAttendeeGuid, 
            int registeredAttendeeId, boolean isGuest, String sessionCookie, 
            String accessToken, byte[] data) throws IOException, JRoomException {
        JRoomScreenOutgoingRequest innerRequest = 
                JRoomScreenOutgoingRequest.newBuilder()
                .setConferenceId(conferenceId)
                .setGuestAttendeeGuid(guestAttendeeGuid)
                .setRegisteredAttendeeId(registeredAttendeeId)
                .setIsGuest(isGuest)
                .setSessionCookie(sessionCookie)
                .setAccessToken(accessToken)
                .setData(ByteString.copyFrom(data))
                .build();
        JRoomRequest request = JRoomRequest.newBuilder()
                .setType(JRoomProtos.Type.SCREEN_OUT)
                .setScreenOut(innerRequest)
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
        if (response.getType() != JRoomProtos.Type.SCREEN_OUT) {
            throw new JRoomException("Invalid response");
        }
        return response.getScreenOut();
    }
}
