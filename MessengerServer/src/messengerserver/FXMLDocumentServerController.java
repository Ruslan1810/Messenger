/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerserver;

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
import org.json.simple.JSONObject;

/**
 *
 * @author Руслан
 */
public class FXMLDocumentServerController implements Initializable {

    @FXML
    private TextArea TextAreaSend;
    @FXML
    private TextArea TextAreaHistory;
    @FXML
    private Button buttonSendMessage;
    
    Socket clientSocket;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        int port = 8080;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            MyReader reader = new MyReader(clientSocket);
            reader.t = TextAreaHistory;
            reader.start();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       // JSONObject obj = new JSONObject();
    }

    @FXML
    private void handleButtonSendMessage(ActionEvent event) throws IOException {
        
        String userInput;
        PrintWriter out;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);

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
                //This part is printing the output to console
                //Instead it should be appending the output to some file
                //or some swing element. Because this output may overlap
                //the user input from console
                System.out.println(outputFromServer);
                t.appendText(outputFromServer + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
