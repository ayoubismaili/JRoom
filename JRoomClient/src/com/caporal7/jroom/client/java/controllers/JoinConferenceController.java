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

import com.caporal7.jroom.client.java.dao.AttendeeDao;
import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.JRoomSettings;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeAuthResponse;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class JoinConferenceController {

    @FXML
    private Label lblVersion;
    @FXML
    private StackPane spMain;
    @FXML
    private HBox hbLogin;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private CheckBox cbDontLogout;
    @FXML
    private Hyperlink hlForgotPassword;
    @FXML
    private Hyperlink hlRegister;
    @FXML
    private Button btnJoin;
    @FXML
    private Button btnLoginInterface;
    
    private AttendeeDao attendeeDao;

    public void initialize() {
        lblVersion.setText("Version : " + JRoomSettings.VERSION);
        reconnect();
    }
    
    private boolean reconnect() {
        try {
            attendeeDao = new AttendeeDao();
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
    
    @FXML
    private void btnJoinClick(MouseEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../resources/view/join-conference-id.fxml"));
        Stage stage = new Stage();
        stage.setTitle("JRoom");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @FXML
    private void btnLoginInterfaceClick(MouseEvent e) {
        spMain.setVisible(false);
        hbLogin.setVisible(true);
    }
    
    @FXML
    private void btnBackClick(MouseEvent e) {
        spMain.setVisible(true);
        hbLogin.setVisible(false);
    }
    
    @FXML
    private void btnLoginClick(MouseEvent e) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        
        try {
            JRoomAttendeeAuthResponse response = attendeeDao.auth(
                    email, password);
            switch(response.getType())
            {
                case INVALID_REQUEST: {
                    ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                    alert.setTitle("Requête invalide");
                    alert.setHeaderText("Le serveur a détecté que la requête est invalide. Veuillez vérifier puis réessayer.");
                    alert.showAndWait();
                    break;
                }
                case INVALID_EMAIL_AND_OR_PASSWORD: {
                    ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                    alert.setTitle("L'email ou/et le mot de passe est incorrect");
                    alert.setHeaderText("L'adresse mail ou/et le mot de passe saisie est incorrect. Veuillez vérifier puis réessayer.");
                    alert.showAndWait();
                    break;
                }
                case SUCCESS: {
                    /* Successful authentication, store Registered Attendee Id & Session Cookie */
                    XMLConfiguration config = JRoomSettings.getSettings();
                    config.setProperty("registered-attendee-id", response.getRegisteredAttendeeId());
                    config.setProperty("session-cookie", response.getSessionCookie());
                    
                    Parent root = FXMLLoader.load(getClass().getResource("../../resources/view/principal-interface.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("JRoom");
                    stage.setScene(new Scene(root));
                    stage.show();
                    
                    ((Node)(e.getSource())).getScene().getWindow().hide();
                    break;
                }
                case TOO_MANY_REQUESTS:{
                    ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.WARNING, "", btnQuit);
                    alert.setTitle("Comportement suspect");
                    alert.setHeaderText("Le serveur a détecté l'envoi de plusieurs requêtes depuis votre adresse IP. Veuillez vérifier puis réessayer.");
                    alert.showAndWait();
                    break;
                }
                default:
                    throw new AssertionError(response.getType().name());
            }
        } catch(IOException ex) {
            ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                alert.setTitle("Connexion échouée");
                alert.setHeaderText("Echec de la communication avec le serveur distant. Veuillez vérifier puis réessayer.");
                alert.showAndWait();
                /* Try to reconnect to server */
                reconnect();
        } catch (JRoomException ex) {
            ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                alert.setTitle("Erreur de protocole");
                alert.setHeaderText("La réponse envoyée par le serveur est invalide. Veuillez vérifier puis réessayer.");
                alert.showAndWait();
        } catch (ConfigurationException ex) {
            ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                alert.setTitle("Erreur de configuration");
                alert.setHeaderText("Un problème a été rencontré lors de l'accès au fichier de configuration. Veuillez vérifier puis réessayer.");
                alert.showAndWait();
        }
    }
    
    @FXML
    private void hlForgotPasswordClick(MouseEvent e) {
        
    }

    @FXML
    private void hlRegisterClick(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../resources/view/register-attendee.fxml"));
        Stage stage = new Stage();
        stage.setTitle("S'inscrire");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
