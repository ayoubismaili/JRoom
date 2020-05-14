/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.server.java;

import com.caporal7.jroom.server.java.services.JRoomServer;

/**
 *
 * @author sphere
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
            JRoomServer appServer = new JRoomServer();
            appServer.loopForever();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
