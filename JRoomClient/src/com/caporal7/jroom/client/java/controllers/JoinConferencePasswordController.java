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

import com.caporal7.jroom.client.java.dao.ConferenceDao;
import com.caporal7.jroom.common.java.JRoomSettings;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.configuration2.XMLConfiguration;

public class JoinConferencePasswordController {

    @FXML
    private TextField txtPassword;
    
    private int registeredAttendeeId;
    private String guestAttendeeGuid;
    private int conferenceId;
    private boolean isGuest;
    private String sessionCookie;
    
    public void initialize() {
        
    }
    
    @FXML
    private void btnJoinConferenceClick(MouseEvent e) throws Exception {
        ConferenceDao conferenceDao = new ConferenceDao();
        int password = -1;
        try {
            password = Integer.valueOf(txtPassword.getText());
        } catch(Exception ex) {
            ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                    alert.setTitle("Mot de passe invalide");
                    alert.setHeaderText("Le mot de passe doit être numérique. Veuillez vérifier puis réessayer.");
                    alert.showAndWait();
            return;
        }
        JRoomConferenceProtos.JRoomJoinConferenceAuthResponse response = conferenceDao.auth(
                getConferenceId(), password, 0, getGuestAttendeeGuid(), 
                isIsGuest(), getSessionCookie());
        switch(response.getResponse()){
            case INVALID_REQUEST: {
                ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                    alert.setTitle("Requête invalide");
                    alert.setHeaderText("Requête invalide. Veuillez vérifier puis réessayer.");
                    alert.showAndWait();
                break;
            }
            case INVALID_ID: {
                ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                    alert.setTitle("Requête invalide");
                    alert.setHeaderText("L'ID de la réunion est invalide. Veuillez vérifier puis réessayer.");
                    alert.showAndWait();
                break;
            }
            case INVALID_PASS: {
                ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                    alert.setTitle("Requête invalide");
                    alert.setHeaderText("Le mot de passe de la réunion est invalide. Veuillez vérifier puis réessayer.");
                    alert.showAndWait();
                break;
            }
            case SUCCESS: {
                /* Successful authentication, store details */
                XMLConfiguration config = JRoomSettings.getSettings();
                if (isIsGuest()) {
                    config.setProperty("guest-attendee-guid", getGuestAttendeeGuid());
                } else {
                    config.setProperty("registered-attendee-id", getRegisteredAttendeeId());
                    config.setProperty("session-cookie", getSessionCookie());
                }
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/caporal7/jroom/client/resources/view/conference.fxml"));
                ConferenceController controller = new ConferenceController();
                controller.setConferenceId(conferenceId);
                if (isIsGuest()) {
                    controller.setGuestAttendeeGuid(getGuestAttendeeGuid());
                    controller.setRegisteredAttendeeId(0);
                } else {
                    controller.setGuestAttendeeGuid("0");
                    controller.setRegisteredAttendeeId(getRegisteredAttendeeId());
                }
                
                controller.setIsGuest(isGuest);
                controller.setSessionCookie(sessionCookie);
                controller.setAccessToken(response.getAccessToken());
                loader.setController(controller);
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("JRoom Réunion");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                break;
            }
            default:
                throw new AssertionError(response.getResponse().name());
        }
    }
    
    @FXML
    private void btnCancelClick(MouseEvent e) throws Exception {
        ((Node)(e.getSource())).getScene().getWindow().hide();
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
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

    /**
     * @return the registeredAttendeeId
     */
    public int getRegisteredAttendeeId() {
        return registeredAttendeeId;
    }

    /**
     * @param registeredAttendeeId the registeredAttendeeId to set
     */
    public void setRegisteredAttendeeId(int registeredAttendeeId) {
        this.registeredAttendeeId = registeredAttendeeId;
    }

    /**
     * @return the guestAttendeeGuid
     */
    public String getGuestAttendeeGuid() {
        return guestAttendeeGuid;
    }

    /**
     * @param guestAttendeeGuid the guestAttendeeGuid to set
     */
    public void setGuestAttendeeGuid(String guestAttendeeGuid) {
        this.guestAttendeeGuid = guestAttendeeGuid;
    }
}
