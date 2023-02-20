package se.myhappyplants.client.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import se.myhappyplants.client.view.LibraryPlantPane;
import se.myhappyplants.robots.DBRobot;
import se.myhappyplants.robots.KeyRobot;
import java.awt.event.KeyEvent;
import javafx.scene.control.ListView;
import se.myhappyplants.shared.Plant;


import java.awt.AWTException;
import java.net.URL;
import java.security.Key;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Phille jobbar här
 *
 */



class MyPlantsTabPaneControllerTest extends ApplicationTest {

    private Scene scene;
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

    @BeforeEach //Loggar in test Användaren.
    public void setUp(){
        KeyRobot.clickAndWrite("#txtFldEmail", "2@2.se", 0);
        KeyRobot.clickAndWrite("#passFldPassword", "2@2.se", 0);
        KeyRobot.click("#test", 0);
        WaitForAsyncUtils.waitForFxEvents();
        KeyRobot.pressEnter();
        sleep(2000);
    }

    //@AfterEach
    public void tearDown() throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    //@AfterAll
    static void closeConnection() {

    }

    //Selects the correct sorting option.
    //Input is the item number. First option is = 0. Next is 1 and so on.
    private void selectSortingOption(int itemNumber){
        KeyRobot.click("#cmbSortOption");
        sleep(1000);
        for(int i = 0; i <= itemNumber; i++)
            KeyRobot.press(KeyEvent.VK_DOWN);

        KeyRobot.pressEnter();

    }

    //@Test
    @DisplayName("Test")
    public void sortByNickname(){
        //cmbSortOption
    }

    @Test
    @DisplayName("Sort by water needed")
    public void sortByWaterNeeded(){
        selectSortingOption(1);

        Node n = StartClient.getScene().lookup("#lstViewUserPlantLibrary");
        ListView view = (ListView) n;
        ObservableList<LibraryPlantPane> list = view.getItems();
        for(LibraryPlantPane plant : list){
            System.out.println(plant.getPlant().getNickname());
        }


    }

    //@Test
    @DisplayName("Expand all plants")
    public void expandAll(){
        //btnExpandAll
    }

    //@Test
    @DisplayName("Collapse all plants")
    public void collapseAll(){
        //btnCollapseAll
    }

    //@Test
    @DisplayName("Water all plants")
    public void waterAll(){
        //btnWaterAll
    }

    //@Test
    @DisplayName("Show Info about a plant")
    public void showInfo(){

    }

    //@Test
    @DisplayName("Validate settings button")
    public void settingsButton(){
        //Change nickname

        //Change picture

        //Delete plant

        //Add the plant back.
    }



}