/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.server.java.services;

import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.JRoomSettings;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.JRoomRequest;
import com.caporal7.jroom.common.java.protoc.JRoomProtos.Type;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import com.caporal7.jroom.server.java.dao.AttendeeDao;
import com.caporal7.jroom.server.java.dao.ConferenceDao;
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
                        System.out.println(ex.getMessage());
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
                default:
                    throw new AssertionError(type.name());
            }
        }
    }
}
