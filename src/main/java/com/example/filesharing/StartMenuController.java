package com.example.filesharing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class StartMenuController {
    @FXML
    private Label errorMsg;
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField portTextField;
    @FXML
    protected void connectBtnClick(ActionEvent event) {
        String ip = ipTextField.getText();
        Socket socket = null;
        try {
            int port = Integer.parseInt(portTextField.getText());
            try {
                socket = new Socket(ip, port);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client.fxml"));
                Parent root = fxmlLoader.load();
                FTPClientController controller = fxmlLoader.getController();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 400, 500);
                stage.setScene(scene);
                stage.show();

                controller.setFTPClient(socket);
            } finally {
                errorMsg.setVisible(true);
                errorMsg.setText("Invalid ip or port");
                if (socket != null) socket.close();
            }
        } catch (NumberFormatException e) {
            errorMsg.setText("Invalid port");
        }  catch (IOException e) {
            errorMsg.setText("Invalid ip or port");
        }
    }
}
