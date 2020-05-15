/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.server.java;

import com.caporal7.jroom.common.java.jpa.Conference;
import com.caporal7.jroom.server.java.services.JRoomServer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
            JRoomServer appServer = new JRoomServer();
            appServer.loopForever();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
