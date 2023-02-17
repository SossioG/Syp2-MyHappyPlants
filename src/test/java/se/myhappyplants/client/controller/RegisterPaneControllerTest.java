package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.robots.DBRobot;
import se.myhappyplants.robots.KeyRobot;

import java.awt.AWTException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Author: Philip Holmqvist
 * Beskrivning: Testklass för att testa funktionalliteten i RegisterPaneController som används
 *              till att regristrera nya användare.
 *
 */

class RegisterPaneControllerTest extends ApplicationTest {
    //Create a new user in the register pane.
    static String regEmail = "testEmail@.se";
    static String regUsername = "testUser123";
    static String regPassword = "testPassword123";

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
        String query = String.format("DELETE FROM tuser WHERE username = '%s' OR email = '%s'", regUsername, regEmail);
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

    @AfterAll
    static void closeConnection() {
        DBRobot.closeConnection();
    }


    private void enterRegFields(String email1, String email2, String username, String password1, String password2) throws AWTException {
        // Skriv in användaruppgifter i fälten.
        if(email1 != null) KeyRobot.clickAndWrite("#txtFldNewEmail", email1);
        if(email2 != null) KeyRobot.clickAndWrite("#txtFldNewEmail1", email2);
        if(username != null) KeyRobot.clickAndWrite("#txtFldNewUsername", username);
        if(password1 != null) KeyRobot.clickAndWrite("#passFldNewPassword", password1);
        if(password2 != null) KeyRobot.clickAndWrite("#passFldNewPassword1", password2);
        KeyRobot.click("#signUpButton");

        // Vänta på att FX är klar.
        WaitForAsyncUtils.waitForFxEvents();
    }

    //Test där ny användare registreras med giltig indata. Testanvändaren raderas från databasen.
    @Test
    @DisplayName("Registration goes through with valid inserts")
    public void registerNewUser() {
        try {
            enterRegFields(regEmail, regEmail, regUsername, regPassword, regPassword);
        } catch(AWTException e) {
            throw new RuntimeException(e);
        }

        // Kolla i databasen om den nya användaren har lagts till.
        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", regUsername);

        try(ResultSet result = DBRobot.getQuery(query)) {
            result.next();

            Integer id = result.getInt("id");
            String username = result.getString("username");
            String email = result.getString("email");

            Assertions.assertThat(id).isNotNull().isInstanceOf(Integer.class);
            Assertions.assertThat(regUsername).isEqualTo(username);
            Assertions.assertThat(regEmail).isEqualTo(email);
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }

    //Försöker regristrera med ogiltiga mailadresser.
    @Test
    public void invalidEmail(){
        try {
            enterRegFields(regEmail, regEmail, "non-valid email", regPassword, regPassword);
        } catch(AWTException e) {
            Assertions.fail(e.getMessage());
        }

        // Kolla i databasen om den nya användaren har lagts till.
        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", regUsername);

       try(ResultSet result = DBRobot.getQuery(query)) {
            Assertions.assertThat(result.next()).isFalse();
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        } finally {
            DBRobot.closeConnection();
        }
    }

    @Test
    public void emptyEmail(){
        try {
            enterRegFields(regEmail, regEmail, null, regPassword, regPassword);
        } catch(AWTException e) {
            Assertions.fail(e.getMessage());
        }

        // Kolla i databasen om den nya användaren har lagts till.
        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", regUsername);

        try(ResultSet result = DBRobot.getQuery(query)) {
            Assertions.assertThat(result.next()).isFalse();
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        } finally {
            DBRobot.closeConnection();
        }
    }

    //Försöker regristrera med ogitliga mailadresser.
    @Test
    public void invalidPassword(){
        try {
            enterRegFields(regEmail, regEmail, null, "regPassword", "regPassword");
        } catch(AWTException e) {
            Assertions.fail(e.getMessage());
        }

        // Kolla i databasen om den nya användaren har lagts till.
        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", regUsername);

       try(ResultSet result = DBRobot.getQuery(query)) {
            Assertions.assertThat(result.next()).isFalse();
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }
}