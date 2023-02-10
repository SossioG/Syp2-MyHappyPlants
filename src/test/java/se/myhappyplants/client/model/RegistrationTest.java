package se.myhappyplants.client.model;

import mockcodefiles.RegisterPaneControllerMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
class RegistrationTest {
    /**
     * Reqid: ANV03F
     * Skapa konto - MUST  - En användare ska kunna skapa ett nytt konto genom att ange e-postadress, namn och lösenord.
     */
    private final String validEmail = "firstname.surname@gmail.com";
    private final String validUsername = "username123";
    private final String validPassword = "Password1";
    private
    Verifier verifier = new Verifier();


    @Test
    void validReg(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail,
                validUsername, validPassword, validPassword);
        Assertions.assertTrue(mock.validateRegistration(mock));
    }

    @Test
    void emailCheckWhenEmptyStrings(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock("", "",
                validUsername, validPassword, validPassword);
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    void usernameEmptyStrings(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, "",
                validPassword, validPassword);
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    void passwordFirstEmptyStrings(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, validUsername,
                "", validPassword);
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    void passwordSecondEmptyStrings(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, validUsername,
                validPassword, "");
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    void passwordDontMatch(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, validUsername,
                validPassword, validPassword + "a");
        Assertions.assertFalse(mock.validateRegistration(mock));
    }
}