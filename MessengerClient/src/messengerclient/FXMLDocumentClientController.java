/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerclient;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Руслан
 */
public class FXMLDocumentClientController implements Initializable {
    
    @FXML
    private TextArea TextAreaHistory;
    @FXML
    private TextArea TextAreaSend;
    @FXML
    private Button buttonSendMessage;

    Socket socket;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        socket = null;

        String address = "localhost";
        int port = 8080;
        try {
            InetAddress inetAddress = InetAddress.getByName(address);

            socket = new Socket(inetAddress, port);
            System.out.println("InetAddress: " + inetAddress);
            System.out.println("Port: " + port);

            MyReader reader = new MyReader(socket);
            reader.t = TextAreaHistory;
            reader.start();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentClientController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleButtonSendMessage(ActionEvent event) throws IOException {
        
        String userInput;
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);

            userInput = TextAreaSend.getText();
            out.println(userInput + "\n");
            TextAreaHistory.appendText(userInput + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class MyReader extends Thread {

    Socket socket;
    TextArea t;

    public MyReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String outputFromServer = "";
            while ((outputFromServer = in.readLine()) != null) {
                System.out.println(outputFromServer);
                t.appendText(outputFromServer + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}