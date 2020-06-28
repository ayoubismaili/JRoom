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

package com.caporal7.jroom.client.java;

import com.caporal7.jroom.client.java.dao.AttendeeDao;
import com.caporal7.jroom.common.java.JRoomSettings;
import com.caporal7.jroom.common.java.protoc.JRoomAttendeeProtos.JRoomAttendeeSessionHeartbeatResponse;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.apache.commons.configuration2.XMLConfiguration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /* Perform a session heartbeat, and autologin if successful */
        XMLConfiguration config = JRoomSettings.getSettings();
        AttendeeDao dao = null;
        try {
            dao = new  AttendeeDao();
        } catch (Exception e) {
            ButtonType btnQuit = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.ERROR, "", btnQuit);
                alert.setTitle("Serveur indisponible");
                alert.setHeaderText("Le serveur demandé est actuellement indisponible (spécifié au niveau du config.xml). Veuillez vérifier puis réessayer.");
                alert.showAndWait();
            return;
        }
        
        int registeredAttendeeId = config.getInt("registered-attendee-id", 0);
        String sessionCookie = config.getString("session-cookie", "");
        JRoomAttendeeSessionHeartbeatResponse.ResponseType type = dao.sessionHeartbeat(registeredAttendeeId, sessionCookie);
        if (type != JRoomAttendeeSessionHeartbeatResponse.ResponseType.SUCCESS) {
            Parent root = FXMLLoader.load(getClass().getResource("../resources/view/join-conference.fxml"));
            primaryStage.setTitle("Joindre une réunion JRoom");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("../resources/view/principal-interface.fxml"));
            Stage stage = new Stage();
            stage.setTitle("JRoom");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
