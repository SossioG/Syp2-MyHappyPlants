package se.myhappyplants.client.controller;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.model.RootName;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.shared.Email;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controls the inputs from a user that hasn't logged in
 * Created by: Eric Simonsson, Christopher O'Driscoll
 * Updated by: Linn Borgström, 2021-05-13
 */
public class LoginPaneController {

    @FXML public Hyperlink registerLink;
    @FXML public Hyperlink forgotPassword;
    @FXML private TextField txtFldEmail;
    @FXML private PasswordField passFldPassword;
    @FXML private Button loginButton;
    @FXML private Button verifyButton;

    /**
     * Switches to 'logged in' scene
     * @throws IOException
     */
    @FXML public void initialize() throws IOException {
        String lastLoggedInUser;

        File file = new File("resources/lastLogin.txt");
        if (!file.exists()) {
            file.createNewFile();

        }
        else if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader("resources/lastLogin.txt"));) {
                lastLoggedInUser = br.readLine();
                txtFldEmail.setText(lastLoggedInUser);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML public void forgotPasswordPane()
    {
        passFldPassword.setVisible(false);
        loginButton.setDisable(true);
        loginButton.setVisible(false);
        verifyButton.setDisable(false);
        verifyButton.setVisible(true);

    }

    //todo generate a code on the server that is valid for ex 10 min for a specific mail adress.
    //  Send verification code to that mail
    //  if user Enters correct mail, send them their password on mail.

    public void verifyMail()
    {
        Thread verificationThread = new Thread(() -> {
            Message verificationCodeMessage = new Message(MessageType.verifyMail, txtFldEmail.getText());
            ServerConnection connection = ServerConnection.getClientConnection();
            Message verificationResponse = connection.makeRequest(verificationCodeMessage);

            System.out.println(verificationResponse.isSuccess());
            if (verificationResponse != null) {

                if (verificationResponse.isSuccess()) {
                    System.out.println("success");
                    //make server generate a 6-Letter code that is valid for 10 min

                    //sendVerificationCode(); //send code to user mail

                    //switch to a verification code textbox

                    //send code from textfield to server to check if code is right

                }
                else {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Email is invalid."));
                }
            }
            else {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }
        });
        verificationThread.start();
    }

    //Send a generated Verification code to user
    public void sendVerificationCode()
    {
        String generatedCode = "";
        String mail = txtFldEmail.getText();
        String code = String.format("Your Verification code is: " + "%s",generatedCode);
        try
        {
            Email.postEmail(mail,"VerificationCode",code);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method which tries to log in user. If it's successful, it changes scene
     * @throws IOException
     */
    @FXML private void loginButtonPressed() {
        Thread loginThread = new Thread(() -> {
            Message loginMessage = new Message(MessageType.login, new User(txtFldEmail.getText(), passFldPassword.getText()));
            ServerConnection connection = ServerConnection.getClientConnection();
            Message loginResponse = connection.makeRequest(loginMessage);

            if (loginResponse != null) {
                if (loginResponse.isSuccess()) {
                    LoggedInUser.getInstance().setUser(loginResponse.getUser());

                    Platform.runLater(() -> MessageBox.display(BoxTitle.Success, "Now logged in as " + LoggedInUser.getInstance().getUser().getUsername()));

                    try {
                        switchToMainPane();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Password and/or email is invalid."));

                }
            }
            else {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }
        });
        loginThread.start();
    }

    /**
     * Method to switch to the mainPane FXML
     * @throws IOException
     */
    @FXML private void switchToMainPane() throws IOException {
        StartClient.setRoot(String.valueOf(RootName.mainPane));
    }

    /**
     * Method to switch to the registerPane
     * @param actionEvent
     */
    public void swapToRegister(ActionEvent actionEvent) {
        try {
            StartClient.setRoot(RootName.registerPane.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
