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

import java.net.URL;
import java.time.Period;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
        
    }

    @FXML
    private void allCboAction(ActionEvent event) {
        
        boolean all = cboDay.getSelectionModel().isEmpty()
                || cboMonth.getSelectionModel().isEmpty()
                || cboYear.getSelectionModel().isEmpty();
        btnContinue.setDisable(all);
    }
    
}
