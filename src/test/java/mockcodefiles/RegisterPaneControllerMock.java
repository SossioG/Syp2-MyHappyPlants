package mockcodefiles;

import javafx.application.Platform;
import se.myhappyplants.client.controller.RegisterPaneController;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.view.MessageBox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
    public class RegisterPaneControllerMock {

        private String txtFldNewEmail;
        private String txtFldNewEmail1;
        private String txtFldNewUsername;
        private String passFldNewPassword;
        private String passFldNewPassword1;

        public RegisterPaneControllerMock(String txtFldNewEmail, String txtFldNewEmail1, String txtFldNewUsername, String passFldNewPassword, String passFldNewPassword1) {
            this.txtFldNewEmail = txtFldNewEmail;
            this.txtFldNewEmail1 = txtFldNewEmail1;
            this.txtFldNewUsername = txtFldNewUsername;
            this.passFldNewPassword = passFldNewPassword;
            this.passFldNewPassword1 = passFldNewPassword1;
        }

        public String[] getComponentsToVerify() {
            String[] loginInfoToCompare = new String[5];
            loginInfoToCompare[0] = txtFldNewEmail;
            loginInfoToCompare[1] = txtFldNewEmail1;
            loginInfoToCompare[2] = txtFldNewUsername;
            loginInfoToCompare[3] = passFldNewPassword;
            loginInfoToCompare[4] = passFldNewPassword1;
            return loginInfoToCompare;
        }



    public boolean validateRegistration(RegisterPaneControllerMock registerPaneControllerMock) {
        String[] loginInfoToCompare = this.getComponentsToVerify();

        if (!validateEmail(loginInfoToCompare[0])) {
            //Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Please enter your email address in format: yourname@example.com"));
            return false;
        }
        if (loginInfoToCompare[2].isEmpty()) {
           // Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Please enter a username"));
            return false;
        }
        if (loginInfoToCompare[3].isEmpty()) {
            //Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Please enter a password"));
            return false;
        }
        if (!loginInfoToCompare[1].equals(loginInfoToCompare[0])) {
           //Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Please enter the same email twice"));
            return false;
        }
        if (!loginInfoToCompare[4].equals(loginInfoToCompare[3])) {
            //Platform.runLater(() -> MessageBox.display(BoxTitle.Error, "Please enter the same password twice"));
            return false;
        }

        return true;

    }

    public boolean validateEmail(String email) throws NullPointerException{
        final String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
