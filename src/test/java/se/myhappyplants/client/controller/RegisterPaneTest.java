package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.robots.DBRobot;
import se.myhappyplants.robots.KeyRobot;
import se.myhappyplants.server.StartServer;

import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Author: Philip Holmqvist
 * Beskrivning: Testklass för att testa funktionalliteten i RegisterPaneController som används
 *              till att regristrera nya användare.
 *
 */

class RegisterPaneTest extends ApplicationTest {
    //Create a new user in the register pane.
    static String validEmail = "testuser@example.com";
    static String validUsername = "TestUsername";
    static String validPassword = "Password1";

    @Override
    public void start(Stage stage) throws Exception {
        //mainNode låter applikationen refresha efter varje test.
        URL fxml = RegisterPaneController.class.getResource("registerPane" + ".fxml");
        Assertions.assertThat(fxml).isNotNull();
        Parent mainNode = FXMLLoader.load(fxml);
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
        StartClient.setScene(stage.getScene());
    }

    @BeforeEach
    void deleteUser() {
        String query = String.format("DELETE FROM tuser WHERE username = '%s' OR email = '%s'", validUsername, validEmail);
        try {
            DBRobot.runQuery(query);
        } catch(SQLException e) {
            Assertions.fail("Database connection failed. Failed to delete test user.");
        }
    }

    // tearDown() körs efter varje test och stänger ner applikationen.
    @AfterEach
    public void tearDown() throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @BeforeAll
    static void startUp() {
        try {
            StartServer.main(null);
        } catch(UnknownHostException | SQLException e) {
            Assertions.fail("Failed to start server: " + e.getMessage());
        }
    }

    @AfterAll
    static void shutDown() {
        DBRobot.closeConnection();
    }


    private void enterRegFields(String email1, String email2, String username, String password1, String password2) {
        // Skriv in användaruppgifter i fälten.
        if(email1 != null) KeyRobot.setText("#txtFldNewEmail", email1);
        if(email2 != null) KeyRobot.setText("#txtFldNewEmail1", email2);
        if(username != null) KeyRobot.setText("#txtFldNewUsername", username);
        if(password1 != null) KeyRobot.setText("#passFldNewPassword", password1);
        if(password2 != null) KeyRobot.setText("#passFldNewPassword1", password2);
        KeyRobot.click("#signUpButton");

        // Vänta på att FX är klar.
        WaitForAsyncUtils.waitForFxEvents();
    }

    private void assertMessageAndClick(String expected) {
        Node messageLbl = KeyRobot.getNode("#messageLbl");
        Assertions.assertThat(messageLbl).isNotNull().isInstanceOf(Label.class);
        Assertions.assertThat(((Label) messageLbl).getText()).isEqualTo(expected);

        Node okButton = KeyRobot.getNode("#okButton");
        Assertions.assertThat(okButton).isNotNull().isInstanceOf(Button.class);

        KeyRobot.click(okButton);
    }

    private void assertUserNotExists() {
        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", validUsername);
        try(ResultSet result = DBRobot.getQuery(query)) {
            Assertions.assertThat(result.next()).isFalse();
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }

    private void clearTextFieldAndAssertEmpty(String query) {
        Node node = KeyRobot.getNode(query);
        KeyRobot.clearText(node);
        WaitForAsyncUtils.waitForFxEvents();
        Assertions.assertThat(((TextField) node).getText()).isEmpty();
    }

    //Test där ny användare registreras med giltig indata. Testanvändaren raderas från databasen.
    @Test
    @DisplayName("Registration goes through with valid inserts")
    public synchronized void registerNewUser() throws InterruptedException {
        enterRegFields(validEmail, validEmail, validUsername, validPassword, validPassword);
        Thread.sleep(2000);
        assertMessageAndClick("Account created successfully! Now logged in as " + validUsername);

        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", validUsername);
        try(ResultSet result = DBRobot.getQuery(query)) {
            result.next();

            Integer id = result.getInt("id");
            String username = result.getString("username");
            String email = result.getString("email");

            Assertions.assertThat(id).isNotNull().isInstanceOf(Integer.class);
            Assertions.assertThat(validUsername).isEqualTo(username);
            Assertions.assertThat(validEmail).isEqualTo(email);
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Registration does not go through with first email insert not matching second email insert")
    void registerNewUserEmailMismatchFirst() {
        enterRegFields("user@example.com", validEmail, validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter the same email twice.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with second email insert not matching first email insert")
    void registerNewUserEmailMismatchSecond() {
        enterRegFields(validEmail, "user@example.com", validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter the same email twice.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with first email insert being empty")
    void emailFirstEmptyString() {
        enterRegFields(null, validEmail, validUsername, validPassword, validPassword);
        assertMessageAndClick("Make sure to fill all boxes.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with second email insert being empty")
    void emailSecondEmptyString() {
        enterRegFields(null, validEmail, validUsername, validPassword, validPassword);
        assertMessageAndClick("Make sure to fill all boxes.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with both email insert being empty")
    void emailBothEmptyString() {
        enterRegFields(null, null, validUsername, validPassword, validPassword);
        assertMessageAndClick("Make sure to fill all boxes.");
        assertUserNotExists();
    }


    // first email bad input without @
    @Test
    @DisplayName("Registration does not go through with first email insert missing at sign")
    void emailFirstNoAt() {
        enterRegFields("userexample.com", validEmail, validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter your email address in format: yourname@example.com");
        assertUserNotExists();
    }

    // first email bad input with @ at end
    @Test
    @DisplayName("Registration does not go through with first email missing domain")
    void emailFirstNoDomain() {
        enterRegFields("user@.com", validEmail, validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter your email address in format: yourname@example.com");
        assertUserNotExists();
    }

    // frist email bad input with @ at start
    @Test
    @DisplayName("Registration does not go through with first email missing local-part")
    void emailFirstNoLocalPart() {
        enterRegFields("@example.com", validEmail, validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter your email address in format: yourname@example.com");
        assertUserNotExists();
    }

    // second email bad input without @
    @Test
    @DisplayName("Registration does not go through with second email insert missing at sign")
    void emailSecondNoAt() {
        enterRegFields(validEmail, "userexample.com", validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter the same email twice.");
        assertUserNotExists();
    }

    // second email bad input with @ at end
    @Test
    @DisplayName("Registration does not go through with second email insert missing domain")
    void emailSecondNoDomain() {
        enterRegFields(validEmail, "user@.com", validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter the same email twice.");
        assertUserNotExists();
    }

    // second email bad input with @ at start
    @Test
    @DisplayName("Registration does not go through with second email insert missing local-part")
    void emailSecondNoLocalPart() {
        enterRegFields(validEmail, "@example.com", validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter the same email twice.");
        assertUserNotExists();
    }

    // first email bad input without @ and with @ at end
    @Test
    @DisplayName("Registration does not go through with both email inserts missing at sign")
    void emailBothNoAt() {
        enterRegFields("userexample.com", "userexample.com", validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter your email address in format: yourname@example.com");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with both email inserts missing domain")
    void emailBothNoDomain() {
        enterRegFields("userexample.com", "userexample.com", validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter your email address in format: yourname@example.com");
        assertUserNotExists();
    }

    // first email bad input without @ and with @ at start
    @Test
    @DisplayName("Registration does not go through with both email inserts missing local-part")
    void emailBothNoLocalPart() {
        enterRegFields("@example.com", "@example.com", validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter your email address in format: yourname@example.com");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with invalid email insert")
    void emailInvalid() {
        validEmail = "@@@";
        enterRegFields(validEmail, validEmail, validUsername, validPassword, validPassword);
        assertMessageAndClick("Please enter your email address in format: yourname@example.com");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with empty username insert")
    public void emptyUsername(){
        enterRegFields(validEmail, validEmail, null, validPassword, validPassword);
        assertMessageAndClick("Make sure to fill all boxes.");
        assertUserNotExists();
    }


    @Test
    @DisplayName("Registration does not go through with first password insert not matching second password insert")
    void passwordMismatchFirst() {
        enterRegFields(validEmail, validEmail, validUsername, validPassword, "12345678Aa");
        assertMessageAndClick("Please enter the same password twice.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with second password insert not matching first password insert")
    void passwordMismatchSecond() {
        enterRegFields(validEmail, validEmail, validUsername, "12345678Aa", validPassword);
        assertMessageAndClick("Please enter the same password twice.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with first password insert being empty")
    public void passwordFirstEmpty(){
        enterRegFields(validEmail, validEmail, validUsername, null, validPassword);
        assertMessageAndClick("Make sure to fill all boxes.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through with second password insert being empty")
    public void passwordSecondEmpty(){
        enterRegFields(validEmail, validEmail, validUsername, validPassword, null);
        assertMessageAndClick("Make sure to fill all boxes.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when first password is missing a capital letter")
    void passwordFirstNoCapitalLetter(){
        enterRegFields(validEmail, validEmail, validUsername, "password1", validPassword);
        assertMessageAndClick("Password must contain one character with uppercase.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when first password is missing a number")
    void passwordFirstNoNumber(){
        enterRegFields(validEmail, validEmail, validUsername, "Password", validPassword);
        assertMessageAndClick("Password must contain one digit.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when first password is missing a capital letter and number")
    void passwordFirstNoBoth(){
        enterRegFields(validEmail, validEmail, validUsername, "password", validPassword);
        assertMessageAndClick("Password must contain one character with uppercase.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when second password is missing a capital letter")
    void passwordSecondNoCapitalLetter(){
        enterRegFields(validEmail, validEmail, validUsername, validPassword, "password1");
        assertMessageAndClick("Please enter the same password twice.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when second password is missing a number")
    void passwordSecondNoNumber(){
        enterRegFields(validEmail, validEmail, validUsername, validPassword, "Password");
        assertMessageAndClick("Please enter the same password twice.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when second password is missing a capital letter and number")
    void passwordSecondNoBoth(){
        enterRegFields(validEmail, validEmail, validUsername, validPassword, "password");
        assertMessageAndClick("Please enter the same password twice.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when first password is too short")
    void passwordFirstTooShort(){
        enterRegFields(validEmail, validEmail, validUsername, "P1", validPassword);
        assertMessageAndClick("Password must be at least 8 characters.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when second password is too short")
    void passwordSecondTooShort(){
        enterRegFields(validEmail, validEmail, validUsername, validPassword, "P1");
        assertMessageAndClick("Please enter the same password twice.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when both passwords missing a capital letter")
    void passwordBothNoCapitalLetter(){
        enterRegFields(validEmail, validEmail, validUsername, "password1", "password1");
        assertMessageAndClick("Password must contain one character with uppercase.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when both passwords missing a number")
    void passwordBothNoNumber(){
        enterRegFields(validEmail, validEmail, validUsername, "Password", "Password");
        assertMessageAndClick("Password must contain one digit.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when both passwords missing a capital letter and number")
    void passwordBothNoBoth(){
        enterRegFields(validEmail, validEmail, validUsername, "password", "password");
        assertMessageAndClick("Password must contain one character with uppercase.");
        assertUserNotExists();
    }

    @Test
    @DisplayName("Registration does not go through when both passwords are too short")
    void passwordBothTooShort(){
        enterRegFields(validEmail, validEmail, validUsername, "P1", "P1");
        assertMessageAndClick("Password must be at least 8 characters.");
        assertUserNotExists();
    }
}