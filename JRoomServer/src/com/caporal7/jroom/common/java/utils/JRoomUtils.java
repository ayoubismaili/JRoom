/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.common.java.utils;

import java.nio.ByteBuffer;

/**
 *
 * @author Ayoub Ismaili <ayoubismaili1@gmail.com>
 */
public class JRoomUtils {
    
    public static byte[] convertIntToBytes(int i) {
        ByteBuffer bb = ByteBuffer.allocate(4); 
        bb.putInt(i);
        return bb.array();
    }
    
    public static int convertBytesToInt(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.getInt();
    }
}

