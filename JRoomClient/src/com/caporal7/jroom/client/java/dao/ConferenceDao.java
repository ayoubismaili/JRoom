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

import com.caporal7.jroom.common.java.JRoomSettings;
import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.protoc.JRoomJoinConferenceProtos.JRoomJoinConferenceProbeRequest;
import com.caporal7.jroom.common.java.protoc.JRoomJoinConferenceProtos.JRoomJoinConferenceProbeResponse.ProbeResponseType;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomResponse;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConferenceDao {
    
    private Socket socket;
    
    public ConferenceDao() throws IOException {
        // TODO: Change to SSLSocket
        socket = new Socket(JRoomSettings.HOST, JRoomSettings.PORT);
    }
    
    public ProbeResponseType probe(int conferenceId) throws IOException, JRoomException {
        JRoomJoinConferenceProbeRequest probRequest = 
                JRoomJoinConferenceProbeRequest.newBuilder()
                .setConferenceId(conferenceId)
                .build();
        JRoomRequest request = JRoomRequest.newBuilder()
                .setType(JRoomProtos.Type.JOIN_CONFERENCE_PROB)
                .setJoinConferenceProb(probRequest)
                .build();
        byte[] requestBytes = request.toByteArray();     
        byte[] lengthBytes = JRoomUtils.convertIntToBytes(requestBytes.length);
        
        OutputStream os = socket.getOutputStream();
        os.write(lengthBytes);
        os.write(requestBytes);
        os.flush();
        
        InputStream is = socket.getInputStream();
        lengthBytes = is.readNBytes(4);
        int length = JRoomUtils.convertBytesToInt(lengthBytes);
        byte[] responseBytes = is.readNBytes(length);
        
        JRoomResponse response = JRoomResponse.parseFrom(responseBytes);
        if (response.getType() != JRoomProtos.Type.JOIN_CONFERENCE_PROB) {
            throw new JRoomException("Invalid response");
        }
        return response.getJoinConferenceProb().getResponse();
    }
}
