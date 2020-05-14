package com.caporal7.jroom.client.java.controllers;

import com.caporal7.jroom.client.java.dao.JRoomJoinConferenceDao;
import com.caporal7.jroom.common.java.protoc.JRoomJoinConferenceProtos.JRoomJoinConferenceProbeResponse.ProbeResponseType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    
    public void initialize() {
        
    }
    
    @FXML
    private void btnJoinClick(MouseEvent e) throws Exception {
        int conferenceId;
        
        try 
        {
            conferenceId = Integer.valueOf(txtConferenceId.getText().replaceAll("\\s+",""));
        } 
        catch(Exception ex) 
        {
            ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, "", btnQuit);
                alert.setTitle("Quitter la réunion");
                alert.setHeaderText("Ce N° de réunion n'est pas valide. Veuillez vérifier puis réessayer.");
                //alert.setContentText("I have a great message for you!");
                alert.showAndWait();
            return;
        }
        String attendeeName = txtName.getText();
        boolean rememberName = cbRememberName.isSelected();
        boolean dontConnectSound = cbDontConnectSound.isSelected();
        boolean stopMyVideo = cbStopMyVideo.isSelected();
        
        JRoomJoinConferenceDao dao = new JRoomJoinConferenceDao();
        ProbeResponseType responseType = dao.probe(conferenceId);
        switch(responseType) 
        {
            case INVALID_ID:
            {
                ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, "", btnQuit);
                alert.setTitle("Quitter la réunion");
                alert.setHeaderText("Ce N° de réunion n'est pas valide. Veuillez vérifier puis réessayer.");
                //alert.setContentText("I have a great message for you!");
                alert.showAndWait();
            }
            case SUCCESS:
            {
                Parent root = FXMLLoader.load(getClass().getResource("../../resources/view/join-conference-password.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Saisir le mot de passe de la réunion");
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }
    
    @FXML
    private void btnCancelClick(MouseEvent e) throws Exception {
        
    }
    
}
