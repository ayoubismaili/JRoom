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
import com.caporal7.jroom.common.java.protoc.JRoomScreenProtos.JRoomScreenOutgoingResponse;
import com.google.protobuf.ByteString;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;
import javax.imageio.ImageIO;

public class ConferenceController implements Initializable {

    @FXML
    private ImageView imgView;
    @FXML
    private Button btnFin;
    @FXML
    private Button btnShareScreen;
    @FXML
    private Button btnStartScreen;
        
    private ObjectProperty<Image> imgProperty = new SimpleObjectProperty<Image>();
    private ScreenDao screenInDao;
    private ScreenDao screenOutDao;

    private int conferenceId;
    private String guestAttendeeGuid;
    private int registeredAttendeeId;
    private boolean isGuest;
    private String sessionCookie;
    private String accessToken;
    
    private boolean stopScreenIncoming = false;
    private boolean stopScreenOutgoing = true;
    private boolean stopCameraIncoming = true;
    private boolean stopCameraOutgoing = true;
    private boolean stopVoiceIncoming = true;
    private boolean stopVoiceOutgoing = true;
    
    private boolean killScreenIncoming = false;
    private boolean killScreenOutgoing = false;
    private boolean killCameraIncoming = false;
    private boolean killCameraOutgoing = false;
    private boolean killVoiceIncoming = false;
    private boolean killVoiceOutgoing = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            screenInDao = new ScreenDao();
            screenOutDao = new ScreenDao();
        } catch (Exception ex) {
            Logger.getLogger(ConferenceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        /* Start Screen tasks */
        screenIncomingTask();
        screenOutgoingTask();
        /* Start Camera tasks */
        //cameraIncomingTask();
        //cameraOutgoingTask();
        /* Start Voice tasks */
        //voiceIncomingTask();
        //voiceOutgoingTask();
    }
    
    @FXML
    private void btnFinClick(MouseEvent event) {
        killScreenIncoming = true;
        killScreenOutgoing = true;
        killCameraIncoming = true;
        killCameraOutgoing = true;
        killVoiceIncoming = true;
        killVoiceOutgoing = true;
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    private void btnShareScreenClick(MouseEvent event) {
        /* TODO: Check if is there any actual screen sharing */
        /* By default, we will start sharing screen and disable incoming screen traffic */
        if (stopScreenOutgoing) {
            stopScreenOutgoing = false;
            btnShareScreen.setText("Arrêter le partage d'écran");
        } else {
            stopScreenOutgoing = true;
            btnShareScreen.setText("Partager l'écran");
        }
    }
    
    @FXML
    private void btnStartScreenClick(MouseEvent event) {
        /* If animator want to enable incoming screen traffic after sharing its own screen he can do it, 
           Otherwise incoming traffic is enabled by default */
        if (stopScreenIncoming) {
            stopScreenIncoming = false;
            btnStartScreen.setText("Arrêter vidéo");
        } else {
            stopScreenIncoming = true;
            btnStartScreen.setText("Demarrer vidéo");
        }
    }
    
    private void screenIncomingTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!killScreenIncoming) {
                    if(!stopScreenIncoming) {
                        try {
                            JRoomScreenIncomingResponse response = screenInDao.incoming(
                                conferenceId, guestAttendeeGuid, registeredAttendeeId, isGuest, sessionCookie, accessToken);
                            if (response.getData() != ByteString.EMPTY) {
                                InputStream is = new ByteArrayInputStream(response.getData().toByteArray());
                                Image img = new Image(is);
                                imgProperty.set(img);
                                is.close();
                            } else {
                                imgProperty.set(null);
                            }
                        } catch (Exception ex) {

                        }
                    }
                    Thread.sleep(200);
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
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!killScreenOutgoing) {
                    if(!stopScreenOutgoing) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Robot robot = new Robot();
                                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                                    WritableImage wi = new WritableImage((int)dim.getWidth(), (int)dim.getHeight());
                                    Rectangle2D rect = new Rectangle2D(0, 0, dim.getWidth(), dim.getHeight());
                                    robot.getScreenCapture(wi, rect);
                                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                                    ImageIO.write(SwingFXUtils.fromFXImage(wi, null), "png", os);
                                    os.close();
                                    byte[] data = os.toByteArray();
                                    JRoomScreenOutgoingResponse response = screenOutDao.outgoing(
                                        conferenceId, guestAttendeeGuid, registeredAttendeeId, isGuest, sessionCookie, accessToken, data);
                                } catch (Exception ex) {
                                    System.out.println(ex.getMessage());
                                }
                            }
                        });
                    }
                    Thread.sleep(100);
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
    
    private void cameraIncomingTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!killCameraIncoming) {
                    try {
                        
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
    }
    
    private void cameraOutgoingTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!killCameraOutgoing) {
                    try {
                        
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
    }
    
    private void voiceIncomingTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!killVoiceIncoming) {
                    try {
                        
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
    }
    
    private void voiceOutgoingTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!killVoiceOutgoing) {
                    try {
                        
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
