package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.server.services.DatabaseConnection;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
    public void start(Stage stage) throws Exception{
        //mainNode låter applikationen refresha efter varje test.
        URL fxml = RegisterPaneController.class.getResource("registerPane" + ".fxml");
        Assertions.assertThat(fxml).isNotNull();
        Parent mainNode = FXMLLoader.load(fxml);
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
        StartClient.setScene(stage.getScene());
    }

    @BeforeAll
    static void deleteUser() {
        DatabaseConnection connection = new DatabaseConnection("am3281");
        String query = String.format("DELETE FROM tuser WHERE email = '%s'", regUsername);

        try(Statement statement = connection.getConnection().createStatement()) {
            statement.executeUpdate(query);
        } catch(SQLException e) {
            Assertions.fail("Database connection failed. Failed to delete test user.");
        } finally {
            connection.closeConnection();
        }
    }

    // tearDown() körs efter varje test och stänger ner applikationen.
    @AfterEach
    public void tearDown() throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }


    //Test där ny användare registreras med giltig indata. Testanvändaren raderas från databasen.
    @Test
    public void registerNewUser(){
        // Klicka igenom registreringen.
        clickOn("#txtFldNewEmail");
        write(regEmail);
        clickOn("#txtFldNewEmail1");
        write(regEmail);
        clickOn("#txtFldNewUsername");
        write(regUsername);
        clickOn("#passFldNewPassword");
        write(regPassword);
        clickOn("#passFldNewPassword1");
        write(regPassword);
        clickOn("#signUpButton");

        // Vänta på att FX är klar.
        WaitForAsyncUtils.waitForFxEvents();

        // Kolla i databasen om den nya användaren har lagts till.
        DatabaseConnection connection = new DatabaseConnection("am3281");
        String query = String.format("SELECT * FROM tuser WHERE username = '%s'", regUsername);

        try(Statement statement = connection.getConnection().createStatement()) {
            ResultSet result = statement.executeQuery(query);
            result.next();

            Integer id = result.getInt("id");
            String username = result.getString("username");
            String email = result.getString("email");

            Assertions.assertThat(id).isNotNull().isInstanceOf(Integer.class);
            Assertions.assertThat(regUsername).isEqualTo(username);
            Assertions.assertThat(regEmail).isEqualTo(email);
        } catch(SQLException e) {
            e.printStackTrace();
            Assertions.fail("Database connection failed. Couldn't select test user from DB.");
        } finally {
            connection.closeConnection();
        }
    }

    //Försöker regristrera med ogiltiga mailadresser.
    @Test
    public void invalidEmail(){

    }

    //Försöker regristrera med ogitliga mailadresser.
    @Test
    public void invalidPassword(){

    }
}