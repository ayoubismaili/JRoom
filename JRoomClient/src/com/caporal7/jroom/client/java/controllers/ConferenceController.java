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

package com.caporal7.jroom.client.java.controllers;

import com.caporal7.jroom.client.java.dao.ScreenDao;
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenIncomingResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ConferenceController implements Initializable {

    @FXML
    private ImageView imgView;
    private ObjectProperty<Image> imgProperty = new SimpleObjectProperty<Image>();
    private ScreenDao screenDao;

    private int conferenceId;
    private String guestAttendeeGuid;
    private int registeredAttendeeId;
    private boolean isGuest;
    private String sessionCookie;
    private String accessToken;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            screenDao = new ScreenDao();
        } catch (Exception ex) {
            Logger.getLogger(ConferenceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void screenIncomingTask() {
        boolean stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        JRoomScreenIncomingResponse response = screenDao.incoming(
                                0, "", 0, true, "", "");
                        InputStream is = new ByteArrayInputStream(response.getData().toByteArray());
                        Image img = new Image(is);
                        imgProperty.set(img);
                        is.close();
                    } catch (Exception ex) {

                    }
                    Thread.sleep(550);
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgView.imageProperty().bind(imgProperty);
    }
    
    private void screenOutgoingTask() {
        boolean stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        JRoomScreenIncomingResponse response = screenDao.incoming(
                                0, "", 0, true, "", "");
                        InputStream is = new ByteArrayInputStream(response.getData().toByteArray());
                        Image img = new Image(is);
                        imgProperty.set(img);
                        is.close();
                    } catch (Exception ex) {

                    }
                    Thread.sleep(550);
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgView.imageProperty().bind(imgProperty);
    }
    
    private void cameraIncomingTask() {
        boolean stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        JRoomScreenIncomingResponse response = screenDao.incoming(
                                0, "", 0, true, "", "");
                        InputStream is = new ByteArrayInputStream(response.getData().toByteArray());
                        Image img = new Image(is);
                        imgProperty.set(img);
                        is.close();
                    } catch (Exception ex) {

                    }
                    Thread.sleep(550);
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgView.imageProperty().bind(imgProperty);
    }
    
    private void cameraOutgoingTask() {
        boolean stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        JRoomScreenIncomingResponse response = screenDao.incoming(
                                0, "", 0, true, "", "");
                        InputStream is = new ByteArrayInputStream(response.getData().toByteArray());
                        Image img = new Image(is);
                        imgProperty.set(img);
                        is.close();
                    } catch (Exception ex) {

                    }
                    Thread.sleep(550);
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgView.imageProperty().bind(imgProperty);
    }
    
    private void voiceIncomingTask() {
        boolean stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        JRoomScreenIncomingResponse response = screenDao.incoming(
                                0, "", 0, true, "", "");
                        InputStream is = new ByteArrayInputStream(response.getData().toByteArray());
                        Image img = new Image(is);
                        imgProperty.set(img);
                        is.close();
                    } catch (Exception ex) {

                    }
                    Thread.sleep(550);
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgView.imageProperty().bind(imgProperty);
    }
    
    private void voiceOutgoingTask() {
        boolean stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        JRoomScreenIncomingResponse response = screenDao.incoming(
                                0, "", 0, true, "", "");
                        InputStream is = new ByteArrayInputStream(response.getData().toByteArray());
                        Image img = new Image(is);
                        imgProperty.set(img);
                        is.close();
                    } catch (Exception ex) {

                    }
                    Thread.sleep(550);
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgView.imageProperty().bind(imgProperty);
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getGuestAttendeeGuid() {
        return guestAttendeeGuid;
    }

    public void setGuestAttendeeGuid(String guestAttendeeGuid) {
        this.guestAttendeeGuid = guestAttendeeGuid;
    }

    public int getRegisteredAttendeeId() {
        return registeredAttendeeId;
    }

    public void setRegisteredAttendeeId(int registeredAttendeeId) {
        this.registeredAttendeeId = registeredAttendeeId;
    }

    public boolean isIsGuest() {
        return isGuest;
    }

    public void setIsGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }

    public String getSessionCookie() {
        return sessionCookie;
    }

    public void setSessionCookie(String sessionCookie) {
        this.sessionCookie = sessionCookie;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
