/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.server.java;

import com.caporal7.jroom.common.java.utils.JRoomConferenceManager;
import com.caporal7.jroom.server.java.services.JRoomServer;

/**
 *
 * @author Ayoub Ismaili
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
            /* Load caching system */
            System.out.println("[+] Loading caching system");
            JRoomConferenceManager.load();
            System.out.println("[+] Caching system loaded");
            JRoomServer appServer = new JRoomServer();
            appServer.loopForever();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
