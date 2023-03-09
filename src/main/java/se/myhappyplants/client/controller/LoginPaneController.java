package se.myhappyplants.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.mindrot.jbcrypt.BCrypt;
import se.myhappyplants.client.model.*;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

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
    @FXML private PasswordField newFldPassword;
    @FXML private PasswordField newConfirmFldPassword;
    @FXML private Button newPasswordBtn;
    @FXML public Label goBackButtonLogin;

    private int verificationCode;
    private long timeDifference;

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
                goBackButtonLogin.setFocusTraversable(true); //sets the goback button on focus to remove from first textfield
                goBackButtonLogin.setVisible(false);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML public void forgotPasswordPane()
    {
        goBackButtonLogin.setVisible(true);
        passFldPassword.setVisible(false);
        loginButton.setDisable(true);
        loginButton.setVisible(false);
        verifyButton.setDisable(false);
        verifyButton.setVisible(true);
        verificationCodeBtn.setVisible(false);

    }

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
                    verificationCode = generateVerificationCode();
                    Platform.runLater(() -> txtFldEmail.setText(""));

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

    public int generateVerificationCode()
    {
        int verificationCode;
        try
        {
            HandleVerificationCodeTask threadTask = new HandleVerificationCodeTask();
            Thread thread = new Thread(threadTask); //generate Verification code
            thread.start();
            thread.join();
            verificationCode = threadTask.getCode(); //store Code
            new Thread(new SendVerificationCodeTask(verificationCode,txtFldEmail.getText())).start(); //send code
            timeDifference = System.currentTimeMillis(); //get current time to see if response is within 10 min.
            return verificationCode;
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public void verifyMatchingVerCode()
    {

        //Felhantering av verifikations kod.
        int txtFieldCode = 0;
        try{
            txtFieldCode = Integer.parseInt(verificationCodeField.getText());

            if(txtFieldCode == 0) {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Please enter a verification code!"));
                System.out.println("Integer is 0");
            }else if(txtFieldCode < 99999 || txtFieldCode > 1000000) {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Verification code is not valid!"));
                System.out.println("Ver code not in intervall " + txtFieldCode);
            }else{
                long currentTime = System.currentTimeMillis();
                if(verificationCode != -1)
                {
                    if(currentTime - timeDifference <= 600000) //check if code has been out for 10 minutes
                    {
                        if(txtFieldCode == verificationCode)
                        {
                            disableVerificationCodeMenu();
                            enableResetPasswordMenu();
                        }
                        else
                        {
                            Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Verification code is not valid!"));
                            System.out.println("Not valid in verifyMatchingVerCode ");
                        }
                    }
                    else
                    {
                        System.out.println("Code has Expired!"); //tell user code has expired
                        Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Verification code has expired!"));
                    }
                }

            }

        }catch (Exception e){
            Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Please enter a valid varification code!"));
        }
    }

    public void resetPassword()
    {
        String newPw = newFldPassword.getText();
        String cnfNewPw = newConfirmFldPassword.getText();
        String password = "";

        if(Objects.equals(newPw, cnfNewPw)){
            System.out.println("resetPassword: password equals");
            String hashPword = BCrypt.hashpw(cnfNewPw, BCrypt.gensalt());

            Thread checkPasswordThread = new Thread(() -> {
                Message checkIfSamePassword = new Message(MessageType.checkPasswordEquals, hashPword, txtFldEmail.getText());
                ServerConnection connection = ServerConnection.getClientConnection();
                Message samePasswordResponse = connection.makeRequest(checkIfSamePassword);
                if(!samePasswordResponse.isSuccess())
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Old password can't be new password!"));
                else{
                    System.out.println("updating password in DB. Sending to method.");
                    updatePasswordInDB(hashPword);
                }




            });
            checkPasswordThread.start();
        }
    }

    private void updatePasswordInDB(String finalHashedPassword){
        Thread updatePasswordThread = new Thread(() -> {
            Message updatePasswordMessage = new Message(MessageType.updatePassword, finalHashedPassword, txtFldEmail.getText());
            ServerConnection connection = ServerConnection.getClientConnection();
            Message updatePasswordResponse = connection.makeRequest(updatePasswordMessage);

            if (updatePasswordResponse != null) {
                if (updatePasswordResponse.isSuccess()) {
                    System.out.println("Password Updated in DB");
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Password has been updated!"));
                    swapToLogin();
                }
                else {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Error: Password not updated"));
                }
            }
            else {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }

        });
        updatePasswordThread.start();

    }

    public void enableResetPasswordMenu()
    {
        newFldPassword.setDisable(false);
        newFldPassword.setVisible(true);
        newConfirmFldPassword.setDisable(false);
        newConfirmFldPassword.setVisible(true);
        newPasswordBtn.setDisable(false);
        newPasswordBtn.setVisible(true);
    }

    public void disableVerificationCodeMenu()
    {
        verificationCodeField.setDisable(true);
        verificationCodeField.setVisible(false);
        verificationCodeBtn.setDisable(true);
        verificationCodeBtn.setVisible(false);
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

    public void swapToLogin() {
        try {
            StartClient.setRoot(RootName.loginPane.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
