package se.myhappyplants.client.controller;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import se.myhappyplants.client.model.*;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.shared.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controls the inputs from a user that hasn't logged in
 * Created by: Eric Simonsson, Christopher O'Driscoll
 * Updated by: Linn Borgström, 2021-05-13
 */
public class LoginPaneController {

    @FXML public Hyperlink registerLink;
    @FXML public Hyperlink forgotPassword;
    @FXML private TextField txtFldEmail;
    @FXML private TextField verificationCodeField;
    @FXML private PasswordField passFldPassword;
    @FXML private Button loginButton;
    @FXML private Button verifyButton;
    @FXML private Button verificationCodeBtn;

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

    //todo:
    //  generate a 6-Letter code which is valid for 10 min
    //  sendVerificationCode(); //send code to user mail
    //  send code from textfield to server to check if code is right

    public void verifyMail()
    {
        String mail = txtFldEmail.getText();
        Thread verificationThread = new Thread(() -> {
            Message verificationCodeMessage = new Message(MessageType.verifyMail, mail);
            ServerConnection connection = ServerConnection.getClientConnection();
            Message verificationResponse = connection.makeRequest(verificationCodeMessage);

            if (verificationResponse != null) {
                if (verificationResponse.isSuccess()) {
                    System.out.println("Email found on server");
                    disableEmailVerificationMenu();
                    enableVerificationCodeMenu();
                    generateVerificationCode();
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

    public void generateVerificationCode()
    {
        int verificationCode;
        try
        {
            HandleVerificationCodeTask threadTask = new HandleVerificationCodeTask();
            Thread thread = new Thread(threadTask);
            thread.start();
            thread.join();
            verificationCode = threadTask.getCode();
            sendVerificationCode(verificationCode);

            //new Thread(new SendVerificationCodeTask(verificationCode,txtFldEmail.getText())).start();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void sendVerificationCode(int code)
    {
        String codeMsg = String.format("Your Verification code is: " + "%s",code);
        try
        {
            Email.postEmail(txtFldEmail.getText(),"VerificationCode",codeMsg);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }


        /* //seperate thread solution
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new EmailTest(txtFldEmail.getText(), "VerificationCode",codeMsg));
        executor.shutdown();

         */
    }

    public void disableEmailVerificationMenu()
    {
        txtFldEmail.setDisable(true);
        txtFldEmail.setVisible(false);
        verifyButton.setDisable(true);
        verifyButton.setVisible(false);
    }

    public void enableVerificationCodeMenu()
    {
        verificationCodeField.setDisable(false);
        verificationCodeField.setVisible(true);
        verificationCodeBtn.setDisable(false);
        verificationCodeBtn.setVisible(true);
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
