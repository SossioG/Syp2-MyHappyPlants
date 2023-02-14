package se.myhappyplants.client.controller;

import mockcodefiles.RegisterPaneControllerMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.model.Verifier;

class ANV03F {
    /**
     * Reqid: ANV03F
     * Skapa konto: En användare ska kunna skapa ett nytt konto genom att ange e-postadress, namn och lösenord.
     */
    private final String validEmail = "firstname.surname@gmail.com";
    private final String validUsername = "username123";
    private final String validPassword = "Password1";
    private
    Verifier verifier = new Verifier();


    @Test
    @DisplayName("Registration goes through with valid inserts")
    void validReg(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail,
                validUsername, validPassword, validPassword);
        Assertions.assertTrue(mock.validateRegistration(mock));
    }

    @Test
    @DisplayName("Registration does not go through with empty email insert")
    void emailCheckWhenEmptyStrings(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock("", "",
                validUsername, validPassword, validPassword);
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    @DisplayName("Registration does not go through with empty username insert")
    void usernameEmptyStrings(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, "",
                validPassword, validPassword);
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    @DisplayName("Registration does not go through with first password insert being empty")
    void passwordFirstEmptyStrings(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, validUsername,
                "", validPassword);
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    @DisplayName("Registration does not go through with second password insert being empty")
    void passwordSecondEmptyStrings(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, validUsername,
                validPassword, "");
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    @DisplayName("Registration does not go through with not matching passwords")
    void passwordDontMatch(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, validUsername,
                validPassword, validPassword + "a");
        Assertions.assertFalse(mock.validateRegistration(mock));
    }

    @Test
    @DisplayName("Registration does not go through when one password use one capital letter and not the other")
    void passwordDontMatchCapitalLetter(){
        RegisterPaneControllerMock mock = new RegisterPaneControllerMock(validEmail, validEmail, validUsername,
                validPassword, "password1");
        Assertions.assertFalse(mock.validateRegistration(mock));
    }
}