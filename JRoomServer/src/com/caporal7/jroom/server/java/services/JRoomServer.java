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

package com.caporal7.jroom.server.java.services;

import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.JRoomSettings;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.Type;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import com.caporal7.jroom.server.java.dao.AttendeeDao;
import com.caporal7.jroom.server.java.dao.CameraDao;
import com.caporal7.jroom.server.java.dao.ConferenceDao;
import com.caporal7.jroom.server.java.dao.ScreenDao;
import com.caporal7.jroom.server.java.dao.VoiceDao;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class JRoomServer {
    
    private ServerSocket serverSocket;
    
    public JRoomServer() throws IOException {
        serverSocket = new ServerSocket(JRoomSettings.PORT);
    }
    
    public void loopForever() throws IOException {
        while (true) 
        {
            Socket socket = serverSocket.accept();
            
            Thread th = new Thread() {
                public void run() {
                    try {
                        handleConnection(socket);
                    } catch (Exception ex) {
                        System.out.println("In JRoomServer.java: Handle Connection Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            };
            th.start();
        }
    }
    
    private void handleConnection(Socket socket) throws IOException, JRoomException {
        System.out.println("[+] New client connected");
        while (true) 
        {
            InputStream is = socket.getInputStream();
            byte[] lengthBytes = is.readNBytes(4);
            int length = JRoomUtils.convertBytesToInt(lengthBytes);
            byte[] bytes = socket.getInputStream().readNBytes(length);

            JRoomRequest request = JRoomRequest.parseFrom(bytes);
            System.out.println("[+] Parsed request");
            Type type = request.getType();
            switch (type) {
                case JOIN_CONFERENCE_PROB:
                    ConferenceDao.handleProbe(request, socket);
                    break;
                case JOIN_CONFERENCE_AUTH:
                    ConferenceDao.handleAuth(request, socket);
                    break;
                case ATTENDEE_REG: 
                    AttendeeDao.handleRegister(request, socket);
                    break;
                case ATTENDEE_AUTH:
                    AttendeeDao.handleAuth(request, socket);
                    break;
                case SCREEN_IN:
                    ScreenDao.handleIncoming(request, socket);
                    break;
                case SCREEN_OUT:
                    ScreenDao.handleOutgoing(request, socket);
                    break;
                case CAMERA_IN:
                    CameraDao.handleIncoming(request, socket);
                    break;
                case CAMERA_OUT:
                    CameraDao.handleOutgoing(request, socket);
                    break;
                case VOICE_IN:
                    VoiceDao.handleIncoming(request, socket);
                    break;
                case VOICE_OUT:
                    VoiceDao.handleOutgoing(request, socket);
                    break;
                case ATTENDEE_SESSION_HEARTBEAT:
                    AttendeeDao.handleSessionHeartbeat(request, socket);
                    break;
                case GET_PERSONAL_CONFERENCE:
                    ConferenceDao.handleGetPersoConf(request, socket);
                    break;
                default:
                    throw new AssertionError(type.name());
            }
        }
    }
}
