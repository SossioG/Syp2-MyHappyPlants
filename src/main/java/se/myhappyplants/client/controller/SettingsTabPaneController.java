package se.myhappyplants.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.model.SetAvatar;
import se.myhappyplants.client.model.Verifier;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.ButtonText;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

/**
 * A controller class to the "settings"-tab
 * Created by: Linn Borgström, Anton Holm, Frida Jacobsson, Eric Simonsson, Susanne Vikström, Christopher O'Driscoll
 * Updated by: Linn Borgström, 2021-06-02
 */
public class SettingsTabPaneController {

    @FXML private ToggleButton tglBtnChangeFunFacts;
    @FXML private ToggleButton tglBtnChangeNotification;
    @FXML private MainPaneController mainPaneController;
    @FXML private Circle imgUserAvatar;
    @FXML private Label lblUsername;
    @FXML private PasswordField passFldDeleteAccount;
    @FXML public PasswordField newPassFld;
    @FXML public Button newPassBtn;

    /**
     * Method to initialize the GUI
     */
    @FXML public void initialize() {
        User loggedInUser = LoggedInUser.getInstance().getUser();
        lblUsername.setText(loggedInUser.getUsername());
        imgUserAvatar.setFill(new ImagePattern(new Image(SetAvatar.setAvatarOnLogin(loggedInUser.getEmail()))));
        tglBtnChangeNotification.setSelected(loggedInUser.areNotificationsActivated());
        ButtonText.setButtonText(tglBtnChangeNotification);
        tglBtnChangeFunFacts.setSelected(loggedInUser.areFunFactsActivated());
        ButtonText.setButtonText(tglBtnChangeFunFacts);
    }
    /**
     * Method to set the mainController
     * @param mainPaneController the controller to set
     */
    public void setMainController(MainPaneController mainPaneController) {
        this.mainPaneController = mainPaneController;
    }

    /**
     * Method to send to the server to change settings of the notifications.
     */
    @FXML public void changeNotificationsSetting() {
        tglBtnChangeNotification.setDisable(true);
        Thread changeNotificationsThread = new Thread(() -> {
            Message notificationRequest = new Message(MessageType.changeNotifications, tglBtnChangeNotification.isSelected(), LoggedInUser.getInstance().getUser());
            ServerConnection connection = ServerConnection.getClientConnection();
            Message notificationResponse = connection.makeRequest(notificationRequest);
            if (notificationResponse != null) {
                if (notificationResponse.isSuccess()) {
                    LoggedInUser.getInstance().getUser().setIsNotificationsActivated(tglBtnChangeNotification.isSelected());
                    tglBtnChangeNotification.setDisable(false);
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Success, "Notification settings has been changed."));
                } else {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Settings could not be changed."));
                }
            } else {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }

        });
        changeNotificationsThread.start();
        ButtonText.setButtonText(tglBtnChangeNotification);
        mainPaneController.getMyPlantsTabPaneController().createCurrentUserLibrary();
    }

    /**
     * Message to send to the server to change the setting of the fun facts
     */
    @FXML public void changeFunFactsSetting() {
        tglBtnChangeFunFacts.setDisable(true);
        Thread changeFunFactsThread = new Thread(() -> {
            Message changeFunFactsRequest = new Message(MessageType.changeFunFacts, tglBtnChangeFunFacts.isSelected(), LoggedInUser.getInstance().getUser());
            ServerConnection connection = ServerConnection.getClientConnection();
            Message funFactsResponse = connection.makeRequest(changeFunFactsRequest);
            if (funFactsResponse != null) {
                if (funFactsResponse.isSuccess()) {
                    LoggedInUser.getInstance().getUser().setFunFactsActivated(tglBtnChangeFunFacts.isSelected());
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Success, "Fun Facts settings has been changed."));
                    tglBtnChangeFunFacts.setDisable(false);
                } else {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Settings could not be changed."));
                }
            } else {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }
        });
        changeFunFactsThread.start();
        ButtonText.setButtonText(tglBtnChangeFunFacts);
        mainPaneController.getSearchTabPaneController().showFunFact(tglBtnChangeFunFacts.isSelected());
    }

    /**
     * Method that handles actions when a user clicks button to delete account.
     */
    @FXML private void deleteAccountButtonPressed() {
        MessageBox.display(BoxTitle.Delete, "All your personal information will be deleted and cannot be restored.");
        int answer = MessageBox.askYesNo(BoxTitle.Delete, "Are you sure you want to delete your account?");
        if (answer == 1) {
            Thread deleteAccountThread = new Thread(() -> {
                Message deleteMessage = new Message(MessageType.deleteAccount, new User(LoggedInUser.getInstance().getUser().getEmail(), passFldDeleteAccount.getText()));
                ServerConnection connection = ServerConnection.getClientConnection();
                Message deleteResponse = connection.makeRequest(deleteMessage);
                if (deleteResponse != null) {
                    if (deleteResponse.isSuccess()) {
                        Platform.runLater(() -> MessageBox.display(BoxTitle.Success, "We are sorry to see you go."));
                        try {
                            logoutButtonPressed();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The passwords you entered does not match."));
                    }
                } else {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
                }
            });
            deleteAccountThread.start();
        }
    }
    /**
     * Method to message the right controller-class that the log out-button has been pressed
     * @throws IOException
     */
    @FXML private void logoutButtonPressed() throws IOException {
        mainPaneController.logoutButtonPressed();
    }

    /**
     * Method to update the users avatar picture on the tab
     */
    public void updateAvatar() {
        imgUserAvatar.setFill(new ImagePattern(new Image(LoggedInUser.getInstance().getUser().getAvatarURL())));
    }

    /**
     * User selects a picture from their computer. The application copies it into
     * the resources/images/user_avatars folder and renames it to "[users email]_avatar".
     * Then the application sets stored picture to profile picture
     * @author Anton
     */
    @FXML private void selectAvatarImage() {
        User user = LoggedInUser.getInstance().getUser();
        FileChooser fc = new FileChooser();

        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png");
        fc.getExtensionFilters().add(fileExtensions);

        File selectedImage = fc.showOpenDialog(null);

        if (selectedImage != null) {
            String imagePath = selectedImage.toString();
            String imageExtension = imagePath.substring(imagePath.indexOf("."));
            try {
                try {
                    Files.copy(selectedImage.toPath(), new File("resources/images/user_avatars/" + user.getEmail() + "_avatar" + imageExtension).toPath());
                } catch (FileAlreadyExistsException e) {
                    Files.delete(new File("resources/images/user_avatars/" + user.getEmail() + "_avatar" + imageExtension).toPath());
                    Files.copy(selectedImage.toPath(), new File("resources/images/user_avatars/" + user.getEmail() + "_avatar" + imageExtension).toPath());
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/images/user_avatars/" + user.getEmail() + "_avatar.txt"))) {
                    bw.write("resources/images/user_avatars/" + user.getEmail() + "_avatar" + imageExtension);
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                user.setAvatar("resources/images/user_avatars/" + user.getEmail() + "_avatar" + imageExtension);
                mainPaneController.updateAvatarOnAllTabs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML private void changePassword() {
        Verifier verifier = new Verifier();
        if(!verifier.validatePassword(newPassFld.getText())) {
            return;
        }
        
        Thread changePasswordThread = new Thread(() -> {

            Message changePasswordRequest = new Message(MessageType.updatePassword, newPassFld.getText(), LoggedInUser.getInstance().getUser().getEmail());
            ServerConnection connection = ServerConnection.getClientConnection();
            Message changePasswordResponse = connection.makeRequest(changePasswordRequest);
            if (changePasswordResponse != null) {
                if (changePasswordResponse.isSuccess()) {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Success, "Password has been changed."));
                    newPassFld.setText("");
                } else {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The passwords you entered does not match."));
                }
            } else {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }
        });
        changePasswordThread.start();
    }
}
