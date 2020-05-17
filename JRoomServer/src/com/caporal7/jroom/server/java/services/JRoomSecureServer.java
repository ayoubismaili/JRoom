/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.server.java.services;

import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.JRoomSettings;
import com.caporal7.jroom.common.java.protoc.JRoomProtos;
import com.caporal7.jroom.common.java.utils.JRoomUtils;
import com.caporal7.jroom.server.java.dao.ConferenceDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author Ayoub Ismaili <ayoubismaili1@gmail.com>
 */
public class JRoomSecureServer {
    
    private ServerSocket serverSocket;
    
    public JRoomSecureServer() throws Exception {
        serverSocket = getSSLServer(new File("/home/sphere/dist/cert.jks")).createServerSocket(JRoomSettings.PORT);
    }
    
    private static SSLServerSocketFactory getSSLServer(File crtFile) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        X509Certificate result;
        try (InputStream input = new FileInputStream(crtFile)) {
            result = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(input);
        }
        trustStore.setCertificateEntry(crtFile.getName(), result);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();

        sslContext.init(null, trustManagers, null);
        return sslContext.getServerSocketFactory();
    }
    
    public void loopForever() throws IOException {
        while (true) {
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
        
        byte[] lengthBytes = socket.getInputStream().readNBytes(4);
        int length = JRoomUtils.convertBytesToInt(lengthBytes);
        byte[] bytes = socket.getInputStream().readNBytes(length);
        
        JRoomProtos.JRoomRequest request = JRoomProtos.JRoomRequest.parseFrom(bytes);
        System.out.println("[+] Parsed request");
        JRoomProtos.Type type = request.getType();
        switch (type) {
            case JOIN_CONFERENCE_PROB:
                ConferenceDao.handleProbe(request, socket);
                break;
            case JOIN_CONFERENCE_AUTH:
                System.out.println("Got JOIN_CONFERENCE_AUTH");
                break;
            default:
                throw new AssertionError(type.name());
        }
        socket.close();
    }
}
