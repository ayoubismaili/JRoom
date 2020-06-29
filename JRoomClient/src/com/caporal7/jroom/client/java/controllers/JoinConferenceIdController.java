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
import com.caporal7.jroom.common.java.protoc.JRoomConferenceProtos.JRoomJoinConferenceProbeResponse.ProbeResponseType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.configuration2.XMLConfiguration;

public class JoinConferenceIdController {

    @FXML
    private TextField txtConferenceId;
    @FXML
    private TextField txtName;
    @FXML
    private CheckBox cbRememberName;
    @FXML
    private CheckBox cbDontConnectSound;
    @FXML
    private CheckBox cbStopMyVideo;
    private ConferenceDao dao;
    
    public void initialize() {
        try {
            txtName.setText(JRoomSettings.getSettings().getString("attendee-name", ""));
            dao = new ConferenceDao();
        } catch (Exception ex) {
            Logger.getLogger(JoinConferenceIdController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void btnJoinClick(MouseEvent e) throws Exception {
        int conferenceId;
        
        try 
        {
            conferenceId = Integer.valueOf(txtConferenceId.getText().replaceAll("\\s+",""));
        } 
        catch(NumberFormatException ex)
        {
            ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, "", btnQuit);
                alert.setTitle("Quitter la réunion");
                alert.setHeaderText("Ce N° de réunion n'est pas valide. Veuillez vérifier puis réessayer.");
                alert.showAndWait();
            return;
        }
        String attendeeName = txtName.getText();
        boolean rememberName = cbRememberName.isSelected();
        if (rememberName) {
            JRoomSettings.getSettings().addProperty("attendee-name", attendeeName);
        }
        boolean dontConnectSound = cbDontConnectSound.isSelected();
        boolean stopMyVideo = cbStopMyVideo.isSelected();
        
        ProbeResponseType responseType = dao.probe(conferenceId);
        switch(responseType) 
        {
            case INVALID_ID:
            {
                ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, "", btnQuit);
                alert.setTitle("Quitter la réunion");
                alert.setHeaderText("Ce N° de réunion n'est pas valide. Veuillez vérifier puis réessayer.");
                alert.showAndWait();
                break;
            }
            case SUCCESS:
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/caporal7/jroom/client/resources/view/join-conference-password.fxml"));
                
                XMLConfiguration config = JRoomSettings.getSettings();
                int registeredAttendeeId = config.getInt("registered-attendee-id", 0);
                String guestAttendeeGuid = config.getString("guest-attendee-id", java.util.UUID.randomUUID().toString());
                String sessionCookie = config.getString("session-cookie", "<null>");
                /* If session cookie does not exist, we are talking about a guest */
                boolean isGuest = true;
                if (!sessionCookie.equals("<null>")) {
                    isGuest = false;
                }
                JoinConferencePasswordController controller = new JoinConferencePasswordController();
                controller.setConferenceId(conferenceId);
                controller.setIsGuest(isGuest);
                controller.setSessionCookie(sessionCookie);
                controller.setRegisteredAttendeeId(registeredAttendeeId);
                controller.setGuestAttendeeGuid(guestAttendeeGuid);
                loader.setController(controller);
                Parent root = loader.load();
                
                Stage stage = new Stage();
                stage.setTitle("Saisir le mot de passe de la réunion");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                break;
            }
        }
    }
    
    @FXML
    private void btnCancelClick(MouseEvent e) throws Exception {
        ((Node)(e.getSource())).getScene().getWindow().hide();
    }
}
