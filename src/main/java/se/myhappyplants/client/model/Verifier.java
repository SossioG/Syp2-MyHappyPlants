package se.myhappyplants.client.model;

import javafx.application.Platform;
import se.myhappyplants.client.controller.RegisterPaneController;
import se.myhappyplants.client.view.MessageBox;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to validate the registration
 */
public class Verifier {


    /**
     * Static method to validate the registration when a user register a new account
     * @return boolean if successful
     */
    public boolean validateRegistration(RegisterPaneController registerPaneController) {
        String[] loginInfoToCompare = registerPaneController.getComponentsToVerify();

        for(int i = 0; i < 4; i++) {
            if(loginInfoToCompare[i].isEmpty()) {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Make sure to fill all boxes."));
                return false;
            }
        }
        if (!validateEmail(loginInfoToCompare[0])) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Please enter your email address in format: yourname@example.com"));
            return false;
        }
        if (loginInfoToCompare[3].length() < 8) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Password must be at least 8 characters."));
            return false;
        }

        if (!Pattern.matches(".*[A-Z].*", loginInfoToCompare[3])) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Password must contain one character with uppercase."));
            return false;
        }

        if (!Pattern.matches(".*[0-9].*", loginInfoToCompare[3])) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Password must contain one digit."));
            return false;
        }

        if (!loginInfoToCompare[1].equals(loginInfoToCompare[0])) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Please enter the same email twice."));
            return false;
        }
        if (!loginInfoToCompare[4].equals(loginInfoToCompare[3])) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Please enter the same password twice."));
            return false;
        }

        return true;

    }

    /**
     * Method for validating an email by checking that it contains @
     *
     * @param email input email from user in application
     * @return true if the email contains @, false if it is not valid
     */
    public boolean validateEmail(String email) {
        final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
