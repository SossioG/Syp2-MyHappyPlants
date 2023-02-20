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
 * Phille jobbar här
 *
 */

class SettingsTabPaneControllerTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        //mainNode låter applikationen refresha efter varje test.
        URL fxml = RegisterPaneController.class.getResource("loginPane" + ".fxml");
        Assertions.assertThat(fxml).isNotNull();
        Parent mainNode = FXMLLoader.load(fxml);
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
        StartClient.setScene(stage.getScene());
    }

    @BeforeEach
    public void setUp(){
        KeyRobot.clickAndWrite("#txtFldEmail", "2@2.se", 1000);
        KeyRobot.clickAndWrite("#passFldPassword", "2@2.se", 1000);
        KeyRobot.click("#test", 1000);


    }

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

    @Test
    @DisplayName("Test")
    public void testFunc(){

    }



}