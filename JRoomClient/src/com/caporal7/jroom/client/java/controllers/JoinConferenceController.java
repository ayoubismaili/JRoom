package com.caporal7.jroom.client.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class JoinConferenceController {

    @FXML
    private Label lblVersion;
    //@FXML
    //private Button btnJoin;    
    //@FXML
    //private Button btnLogin;


    public void initialize() {
        lblVersion.setText("Version : 0.1.0");
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
    private void btnLoginClick(MouseEvent e) {
        
    }
    
}
