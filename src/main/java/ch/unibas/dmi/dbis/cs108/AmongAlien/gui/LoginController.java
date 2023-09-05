package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import ch.unibas.dmi.dbis.cs108.AmongAlien.client.AmongAlienClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

/**
 * LoginController is a Controller for the GUILogin.fxml file
 * it controls the GUI components for the Login GUI as
 * described int the method Java Docs
 *
 * @author Rayes Diyab
 * @version 2021.05.22
 */
public class LoginController {

    @FXML
    private TextField portField;
    @FXML
    private TextField myUserName;
    @FXML
    private TextField serverIP;
    @FXML
    private Button myButton;

    private String username = "";
    private String hostname = "";
    private int port = 0;
    private AmongAlienClient client;

    /**
     * The method starts the login process when clicking
     * the Login Button in the GUI
     */
    public void login(){

        if(serverIP.getText().isEmpty()){
            hostname = "localhost";
        }else{
            hostname = serverIP.getText();
        }

        if(portField.getText().isEmpty()){
            port = 8090;
        }else{
            try{
                port = Integer.parseInt(portField.getText());
            }catch (NumberFormatException e){
                System.out.println("Write a Number into the Port field!");
            }
        }

        if(myUserName.getText().isEmpty()){
            username = System.getProperty("user.name");
        }else{
            username = myUserName.getText();
        }

        if(!Objects.equals(username, "") && !Objects.equals(hostname, "") && port != 0){
            System.out.println("Got inputs");
            try {
                client = new AmongAlienClient(hostname, port, username);
                GUI.getInstance().client = client;
                client.execute();

                GUI.getInstance().switchScene("/GUILobby.fxml");
            } catch (IOException e) {
                System.out.println("Connection refused!");
            }
        }else{
            System.out.println("Please check your Inputs!");
        }
    }
}
