package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        if(email1 != null) KeyRobot.clickAndWrite("#txtFldNewEmail", email1, 0);
        if(email2 != null) KeyRobot.clickAndWrite("#txtFldNewEmail1", email2, 0);
        if(username != null) KeyRobot.clickAndWrite("#txtFldNewUsername", username, 0);
        if(password1 != null) KeyRobot.clickAndWrite("#passFldNewPassword", password1, 0);
        if(password2 != null) KeyRobot.clickAndWrite("#passFldNewPassword1", password2, 0);
        KeyRobot.click("#signUpButton", 0);

        // Vänta på att FX är klar.
        WaitForAsyncUtils.waitForFxEvents();
    }

    private void assertMessageAndClick(String expected) {
        WaitForAsyncUtils.waitForFxEvents();
        Node messageLbl = KeyRobot.getNode("#messageLbl");
        Assertions.assertThat(messageLbl).isNotNull().isInstanceOf(Label.class);
        Assertions.assertThat(((Label) messageLbl).getText()).isEqualTo(expected);

        Node okButton = KeyRobot.getNode("#okButton");
        Assertions.assertThat(okButton).isNotNull().isInstanceOf(Button.class);

        KeyRobot.click(okButton);
    }

    //Test där ny användare registreras med giltig indata. Testanvändaren raderas från databasen.
    @Test
    @DisplayName("Registration goes through with valid inserts")
    public void registerNewUser() {
        enterRegFields(regEmail, regEmail, regUsername, regPassword, regPassword);
        assertMessageAndClick("Account created successfully! Now logged in as " + regUsername);

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

    @Test
    @DisplayName("Registration does not go through with empty email insert")
    void registerNewUserEmptyEmail() {
        enterRegFields(null, null, regUsername, regPassword, regPassword);
        assertMessageAndClick("Make sure to fill all boxes.");

        enterRegFields(regEmail, null, null, null, null);
        assertMessageAndClick("Make sure to fill all boxes.");

        KeyRobot.clearText("#txtFldNewEmail");

        enterRegFields(null, regEmail, null, null, null);
        assertMessageAndClick("Make sure to fill all boxes.");


        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", regUsername);
        try(ResultSet result = DBRobot.getQuery(query)) {
            Assertions.assertThat(result.next()).isFalse();
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void invalidEmail(){
        enterRegFields(regEmail, regEmail, "non-valid email", regPassword, regPassword);

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
        enterRegFields(regEmail, regEmail, null, regPassword, regPassword);

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
        enterRegFields(regEmail, regEmail, null, "regPassword", "regPassword");

        // Kolla i databasen om den nya användaren har lagts till.
        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", regUsername);

       try(ResultSet result = DBRobot.getQuery(query)) {
            Assertions.assertThat(result.next()).isFalse();
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }
}