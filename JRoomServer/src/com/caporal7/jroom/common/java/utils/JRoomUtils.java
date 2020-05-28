/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.common.java.utils;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ayoub Ismaili <ayoubismaili1@gmail.com>
 */
public class JRoomUtils {
    
    private static EntityManager em = null;
    private static SecureRandom secureRandom = new SecureRandom();
    
    public static byte[] convertIntToBytes(int i) {
        ByteBuffer bb = ByteBuffer.allocate(4); 
        bb.putInt(i);
        return bb.array();
    }
    
    public static int convertBytesToInt(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.getInt();
    }
    
    public static EntityManager getEntityManager() 
    {
        if (em == null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JRoomServerPU");
            em = emf.createEntityManager();
        }
        return em;
    }
    
    public static String getNewSessionCookie() {
        byte[] bytes = new byte[25];
        secureRandom.nextBytes(bytes);
        StringBuffer sb = new StringBuffer(50);
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    public static int getNewConferencePassword() {
        int password = secureRandom.nextInt(999999);
        return password;
    }
}

