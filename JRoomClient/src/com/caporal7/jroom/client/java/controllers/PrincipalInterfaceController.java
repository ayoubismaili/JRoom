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
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomGetPersonalConferenceResponse;
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceAuthResponse;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.configuration2.XMLConfiguration;

public class PrincipalInterfaceController implements Initializable {

    @FXML
    private ComboBox<?> cboSearch;
    
    private ObjectProperty<String> txtPersonalConfIdProperty = new SimpleObjectProperty<String>();
    private ObjectProperty<String> txtPersonalConfPasswordProperty = new SimpleObjectProperty<String>();
    private ConferenceDao conferenceDao;
    @FXML
    private TextField txtPersonalConfId;
    @FXML
    private TextField txtPersonalConfPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            conferenceDao = new ConferenceDao();
        } catch (Exception ex) {
            //Logger.getLogger(ConferenceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    XMLConfiguration config = JRoomSettings.getSettings();
                    int registeredAttendeeId = config.getInt("registered-attendee-id", 0);
                    String sessionCookie = config.getString("session-cookie", "");
                    JRoomGetPersonalConferenceResponse response = conferenceDao.getPersonalConference(registeredAttendeeId, sessionCookie);
                    String conferenceId = String.valueOf(response.getConferenceId());
                    String password = String.valueOf(response.getPassword());
                    txtPersonalConfIdProperty.set(conferenceId);
                    txtPersonalConfPasswordProperty.set(password);
                } catch (Exception ex) {

                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        txtPersonalConfId.textProperty().bind(txtPersonalConfIdProperty);
        txtPersonalConfPassword.textProperty().bind(txtPersonalConfPasswordProperty);
    }    

    @FXML
    private void btnHomeClick(MouseEvent event) {
        
    }

    @FXML
    private void btnChatClick(MouseEvent event) {
        
    }

    @FXML
    private void btnConferencesClick(MouseEvent event) {
        
    }

    @FXML
    private void btnContactsClick(MouseEvent event) {
        
    }

    @FXML
    private void btnNewConferenceClick(MouseEvent event) throws Exception {
        /* Get Personal Room Credentials */
        ConferenceDao conferenceDao = new ConferenceDao();
        int conferenceId = Integer.valueOf(txtPersonalConfId.getText());
        int password = Integer.valueOf(txtPersonalConfPassword.getText());
        XMLConfiguration config = JRoomSettings.getSettings();
        int registeredAttendeeId = config.getInt("registered-attendee-id", 0);
        String sessionCookie = config.getString("session-cookie", "");
        JRoomJoinConferenceAuthResponse response = conferenceDao.auth(conferenceId, password, registeredAttendeeId, "0", false, sessionCookie);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/view/conference.fxml"));
                ConferenceController controller = new ConferenceController();
                controller.setConferenceId(conferenceId);
                controller.setGuestAttendeeGuid("0");
                controller.setRegisteredAttendeeId(registeredAttendeeId);
                controller.setIsGuest(false);
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
    private void btnJoinClick(MouseEvent event) {
        
    }

    @FXML
    private void btnScheduleClick(MouseEvent event) {
        
    }

    @FXML
    private void btnShareScreenClick(MouseEvent event) {
        
    }

    @FXML
    private void btnSettingsClick(MouseEvent event) {
        
    }
    
}
