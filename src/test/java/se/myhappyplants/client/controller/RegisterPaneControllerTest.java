package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import javafx.scene.Scene;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import se.myhappyplants.server.services.DatabaseConnection;
import se.myhappyplants.server.services.IDatabaseConnection;
import se.myhappyplants.server.services.QueryExecutor;

import java.sql.ResultSet;


/**
 * Author: Philip Holmqvist
 * Beskrivning: Testklass för att testa funktionalliteten i RegisterPaneController som används
 *              till att regristrera nya användare.
 *
 */

class RegisterPaneControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception{
        //mainNode låter applikationen refresha efter varje test.
        Parent mainNode = FXMLLoader.load(RegisterPaneController.class.getResource("registerPane" + ".fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp() throws Exception{

    }

    @After
    public void tearDown() throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }


    //Test där ny användare registreras med giltig indata. Testanvändaren raderas från databasen.
    @Test
    public void registerNewUser(){

        //Create a new user in the register pane.
        String regEmail = "testEmail@.se";
        String regUsername = "testUser123";
        String regPassword = "testPassword123";


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

        //Check in database that new user has been added.
        IDatabaseConnection conn = new DatabaseConnection("am3281");
        QueryExecutor database = new QueryExecutor(conn);
        try{
            ResultSet result = database.executeQuery("select * from tuser where username = 'testUser123';");
            Assertions.assertNotNull(result);

            while (result.next()) {
                String id = result.getString("id");
                String username = result.getString("username");
                String email = result.getString("email");
                Assertions.assertNotNull(id);
                Assertions.assertEquals(regUsername, username);
                Assertions.assertEquals(regEmail, email);
            }
        }catch (Exception e){
            Assertions.fail("Database connection failed. Could'nt select test user from DB.");
        }

        try{
            ResultSet res = database.executeQuery("delete from tuser where username = 'testUser123';");
        }catch (Exception e){
            e.printStackTrace();
        }

        conn.closeConnection();
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