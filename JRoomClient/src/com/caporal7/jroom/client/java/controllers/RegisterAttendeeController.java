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
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos;
import java.io.IOException;
import java.net.URL;
import java.time.Period;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RegisterAttendeeController implements Initializable {

    @FXML
    private VBox vbBirthDate;
    @FXML
    private ComboBox<?> cboDay;
    @FXML
    private ComboBox<?> cboMonth;
    @FXML
    private ComboBox<?> cboYear;
    @FXML
    private Button btnContinue;
    @FXML
    private VBox vbCanNotRegister;
    @FXML
    private HBox vbCanRegister;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    
    private AttendeeDao attendeeDao;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* Initialize day */
        ObservableList olDay = FXCollections.observableArrayList();
        for (int i = 1; i <= 31; i++) {
            olDay.add(String.valueOf(i));
        }
        cboDay.setItems(olDay);
        
        /* Initialize Month */
        ObservableList olMonth = FXCollections.observableArrayList(
            "janv.", "février", "mars", "avril", 
            "mai", "juin", "juil.", "août", 
            "sept.", "oct.", "nov.", "déc.");
        cboMonth.setItems(olMonth);
        
        /* Initialize Year */
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        ObservableList olYear = FXCollections.observableArrayList();
        for (int i = currentYear; i >= 1900; i--) {
            olYear.add(String.valueOf(i));
        }
        cboYear.setItems(olYear);
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
    private void btnContinueClick(MouseEvent event) {
        int day = Integer.valueOf(String.valueOf(
                cboDay.getSelectionModel().getSelectedItem()));
        int month = cboMonth.getSelectionModel().getSelectedIndex() + 1;
        int year = Integer.valueOf(String.valueOf(
                cboYear.getSelectionModel().getSelectedItem()));
        Period p1 = Period.of(year, month, day);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Period p2 = Period.of(currentYear, currentMonth, currentDay);
        Period result = p2.minus(p1);
        /* Check if the attendee has at least 18 years old */
        if (result.getYears() < 18) {
            /* Can not register */
            vbBirthDate.setVisible(false);
            vbCanNotRegister.setVisible(true);
            vbCanRegister.setVisible(false);
        } else {
            /* Can register */
            vbBirthDate.setVisible(false);
            vbCanNotRegister.setVisible(false);
            vbCanRegister.setVisible(true);
        }
    }

    @FXML
    private void btnRegisterClick(MouseEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String passwordConfirm = txtConfirmPassword.getText();
        if (!password.equals(passwordConfirm)) {
            ButtonType btnQuit = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING, "", btnQuit);
            alert.setTitle("Erreur de mot de passe");
            alert.setHeaderText("Les mots de passe doivent être identiques. Veuillez vérifier puis réessayer.");
            alert.showAndWait();
            return;
        }
        
        try {
            JRoomAttendeeProtos.JRoomAttendeeRegisterResponse response = attendeeDao.register(email, password);
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
                case ALREADY_REGISTERED: {
                    ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                    alert.setTitle("Compte déjà existe");
                    alert.setHeaderText("L'adresse mail spécifiée est déjà enregistrée. Veuillez vérifier puis réessayer.");
                    alert.showAndWait();
                    break;
                }
                case SUCCESS: {
                    ButtonType btnQuit = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "", btnQuit);
                    alert.setTitle("Inscription réussie");
                    alert.setHeaderText("Votre compte a été crée avec succès.");
                    alert.showAndWait();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
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
            reconnect();
        } catch (JRoomException ex) {
            ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                alert.setTitle("Erreur de protocole");
                alert.setHeaderText("La réponse envoyée par le serveur est invalide. Veuillez vérifier puis réessayer.");
                alert.showAndWait();
        }
    }

    @FXML
    private void allCboAction(ActionEvent event) {
        
        boolean all = cboDay.getSelectionModel().isEmpty()
                || cboMonth.getSelectionModel().isEmpty()
                || cboYear.getSelectionModel().isEmpty();
        btnContinue.setDisable(all);
    }
}
