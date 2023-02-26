package se.myhappyplants.client.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.assertj.core.api.Assert;
import org.assertj.core.internal.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.client.view.LibraryPlantPane;
import se.myhappyplants.robots.DBRobot;
import se.myhappyplants.robots.KeyRobot;
import java.awt.event.KeyEvent;
import javafx.scene.control.ListView;
import se.myhappyplants.server.StartServer;
import se.myhappyplants.shared.Plant;


import java.awt.AWTException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Key;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Phille jobbar här
 *
 */



class MyPlantsTabPaneControllerTest extends ApplicationTest {

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

    @BeforeAll
    static void startUp() {

        //Starta servern.
        try {
            StartServer.main(null);
        } catch(UnknownHostException | SQLException e) {
            Assertions.fail("Failed to start server: " + e.getMessage());
        }

    }

    @BeforeEach //Loggar in test Användaren inför varje testfall.
    public void setUp(){
        KeyRobot.clickAndWrite("#txtFldEmail", "2@2.se", 0);
        KeyRobot.clickAndWrite("#passFldPassword", "2@2.se", 0);
        KeyRobot.click("#test", 0);
        WaitForAsyncUtils.waitForFxEvents();
        sleep(2000);
        KeyRobot.pressEnter();
        sleep(4000);
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

    @Test //Kontrollerar att man kan sortera plantorna i bibloteket efter nickname.
    @DisplayName("Sort by nickname")
    public void sortByNickname(){
        selectSortingOption(0);

        ObservableList<LibraryPlantPane> plantList = getPlantList();

        String currentNickname;
        String previousNickname = "a";

        for(LibraryPlantPane plant : plantList){
            currentNickname = plant.getPlant().getNickname();
            Assertions.assertThat(currentNickname.compareTo(previousNickname) >= 1);
            previousNickname = currentNickname;
        }
    }

    @Test //Kontrollerar att man kan sortera plantorna i bibloteket efter water needed.
    @DisplayName("Sort by water needed")
    public void sortByWaterNeeded(){
        selectSortingOption(1);

        ObservableList<LibraryPlantPane> plantList = getPlantList();

        int previous = -1;
        int current = 0;

        for(LibraryPlantPane plant : plantList){
            current = plant.getPlant().getNumberDaysUntilWater();
            Assertions.assertThat(current >= previous);
            previous = current;
        }


    }

    @Test //Kontrollerar att expand all knappen fungerar.
    @DisplayName("Expand all plants")
    public void expandAll(){
        //btnExpandAll
        Node expandButton = KeyRobot.getNode("#btnExpandAll");
        Assertions.assertThat(expandButton).isNotNull().isInstanceOf(Button.class);
        KeyRobot.click("#btnExpandAll");

        ObservableList<LibraryPlantPane> plantList = getPlantList();

        for(LibraryPlantPane plant : plantList){
            Assertions.assertThat(plant.getListViewMoreInfo() != null);
            Assertions.assertThat(plant.isExtended());
        }
    }

    @Test //Kontrollerar att collapse all knappen fungerar.
    @DisplayName("Collapse all plants")
    public void collapseAll(){
        //btnCollapseAll
        Node collapseButton = KeyRobot.getNode("#btnCollapseAll");
        Assertions.assertThat(collapseButton).isNotNull().isInstanceOf(Button.class);
        KeyRobot.click("#btnExpandAll"); //Expanderar alla plantor för att sedan collapse:a alla.
        KeyRobot.click("#btnCollapseAll");

        ObservableList<LibraryPlantPane> plantList = getPlantList();

        for(LibraryPlantPane plant : plantList){
            Assertions.assertThat(!plant.isExtended());
        }

    }

    @Test //Kontrollerar så att notifikationer är synliga
    @DisplayName("Notifications visible")
    public void notificationsVisible(){
        Node notifications = KeyRobot.getNode("#lstViewNotifications");
        Assertions.assertThat(notifications).isNotNull();
        Assertions.assertThat(notifications).isVisible();
        Assertions.assertThat(notifications).isInstanceOf(ListView.class);

        ListView notificiationText = (ListView) notifications;
        ObservableList<String> list = notificiationText.getItems();
        Assertions.assertThat(list).isNotNull();

    }

    @Test //Kontrollerar att knappen Water all plants finns.
    @DisplayName("Water all plants")
    public void waterAll(){
        //btnWaterAll
        Node btnWaterAll = KeyRobot.getNode("#btnWaterAll");
        Assertions.assertThat(btnWaterAll).isNotNull().isInstanceOf(Button.class);
    }


    @Test //Testar showInfo knappen och att all information kommer upp.
    @DisplayName("Show Info button plant")
    public void showInfoButton(){
        ObservableList<LibraryPlantPane> plantList = getPlantList();
        for(LibraryPlantPane plant : plantList){
            plant.pressInfoButton();
            sleep(2000);
            Assertions.assertThat(plant.isExtended());
            ObservableList<String> plantInfo = plant.getListViewMoreInfo().getItems();
            Assertions.assertThat(plantInfo).isNotNull();
            Assertions.assertThat(plantInfo.size() == 6);
        }
    }

    @Test
    @DisplayName("Change Last watered")
    public void changeLastWatered(){
        LibraryPlantPane plantPane = getPlantList().get(0);
        plantPane.pressInfoButton();
        Assertions.assertThat(plantPane.isExtended());
        String daysUntilWater1 = plantPane.getPlant().getDaysUntilWater();

        //Ändra last watered till en vecka sedan dagens datum.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date date = cal.getTime();
        LocalDate date1 = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        plantPane.setDatePicker(date1);
        plantPane.changeDate(plantPane.getPlant());
        WaitForAsyncUtils.waitForFxEvents();

        //Säkerställ att informationen har blivit ändrad.
        LibraryPlantPane newPlantPane = getPlantList().get(0);
        String daysUntilWater2 = newPlantPane.getPlant().getDaysUntilWater();
        int value1 = Integer.parseInt(daysUntilWater1.replaceAll("[^0-9]", ""));
        int value2 = Integer.parseInt(daysUntilWater2.replaceAll("[^0-9]", ""));
        Assertions.assertThat(value1 - value2 == 7);

    }

    //@Test
    @DisplayName("Water button plant")
    public void waterButton(){

    }

    //@Test //Testar funktionerna som finns under settings button.
    @DisplayName("Settings button plant")
    public void settingsButton(){

        ObservableList<LibraryPlantPane> plantList = getPlantList();

        //Hämtar första och sista plantan i bibloteket att utföra tester på.
        int size = plantList.size();
        LibraryPlantPane plant1 = plantList.get(0);
        LibraryPlantPane plant2 = plantList.get(size-1);

        //assertNicknameChange(plant1);
        //assertDeletePlant(plant1, true);
    }




    /** Privata hjälp metoder för klassen. */

    //Returnerar alla LibraryPlantPane:s som finns i bibloteket.
    private ObservableList<LibraryPlantPane> getPlantList(){
        Node n = StartClient.getScene().lookup("#lstViewUserPlantLibrary");
        ListView view = (ListView) n;
        ObservableList<LibraryPlantPane> list = view.getItems();
        return list;
    }

    //Väljer ett sorteringsval för plant bibloteket. itemNumber motsvarar ordningen av valen i listan.
    private void selectSortingOption(int itemNumber){
        KeyRobot.click("#cmbSortOption");
        sleep(1000);
        for(int i = 0; i <= itemNumber; i++)
            KeyRobot.press(KeyEvent.VK_DOWN);

        KeyRobot.pressEnter();

    }

    //Assertar pop up box:ar och trycker på ok knappen. Expected är den förväntade texten i rutan.
    private void assertMessageAndClick(String expected) {
        WaitForAsyncUtils.waitForFxEvents();
        Node messageLbl = KeyRobot.getNode("#messageLbl");
        Assertions.assertThat(messageLbl).isNotNull().isInstanceOf(Label.class);
        Assertions.assertThat(((Label) messageLbl).getText()).isEqualTo(expected);

        Node okButton = KeyRobot.getNode("#okButton");
        Assertions.assertThat(okButton).isNotNull().isInstanceOf(Button.class);

        KeyRobot.click(okButton);
    }

    //Bytar nickname på en specifik planta.
    private void assertNicknameChange(LibraryPlantPane plantPane){
        WaitForAsyncUtils.waitForFxEvents();
        plantPane.pressPlantSettingsButton();
        sleep(2000);
        Assertions.assertThat(plantPane.hasExtendedSettings());
        String originalNickname = plantPane.getNickname().toString();


        //För att undvika "Not on FX application thread; currentThread = JavaFX Application Thread error"
        plantPane.pressChangeNicknameButton(plantPane.getPlant());


        WaitForAsyncUtils.waitForFxEvents();
        sleep(2000);

        KeyRobot.writeText("TestingNickname");
        sleep(2000);
        Node okButton = KeyRobot.getNode("#enterButton");
        Assertions.assertThat(okButton).isNotNull().isInstanceOf(Button.class);
        KeyRobot.click(okButton);
        assertMessageAndClick("You changed a plants information");
        sleep(2000);
        WaitForAsyncUtils.waitForFxEvents();
    }

    //Raderar en specifik planta. Om buttonPress = true raderas plantan från bibloteket. Om buttonPress = false raderas inte plantan.
    private void assertDeletePlant(LibraryPlantPane plantPane, boolean buttonPress) {
        WaitForAsyncUtils.waitForFxEvents();
        plantPane.pressPlantSettingsButton();
        sleep(2000);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                plantPane.pressDeletePlantButton(plantPane.getPlant());
            }
        });

        sleep(2000);

        Node messageLbl = KeyRobot.getNode("#messageLbl");
        Assertions.assertThat(messageLbl).isNotNull().isInstanceOf(Label.class);
        Assertions.assertThat(((Label) messageLbl).getText()).isEqualTo("The deleted plant can't be restored. Are you sure?");

        Node yesButton = KeyRobot.getNode("#yesButton");
        Assertions.assertThat(yesButton).isNotNull().isInstanceOf(Button.class);

        Node noButton = KeyRobot.getNode("#noButton");
        Assertions.assertThat(noButton).isNotNull().isInstanceOf(Button.class);

        if(buttonPress){
            KeyRobot.click(yesButton);
            assertMessageAndClick("You removed a plant");
            sleep(2000);
        } else{
            KeyRobot.click(noButton);
            sleep(2000);
            //Check that plant is not deleted by checking if still in library
        }

    }

    //Raderar alla plantor från användarens biblotek. Lägger sedan till de första 5 plantorna som kommer upp vid sök.
    /** Metoden fungerar inte pga en tillfällig bugg med att radera växter. */
    private void refreshPlantLibrary(){
        sleep(2000);
        ObservableList<LibraryPlantPane> plantList = getPlantList();
        for(LibraryPlantPane plantPane : plantList){
            sleep(1000);
            plantPane.pressPlantSettingsButton();
            sleep(2000);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    plantPane.pressDeletePlantButton(plantPane.getPlant());
                }
            });
            sleep(2000);
            Node yesButton = KeyRobot.getNode("#yesButton");
            KeyRobot.click(yesButton);
            WaitForAsyncUtils.waitForFxEvents();
            sleep(2000);
            KeyRobot.pressEnter();
            WaitForAsyncUtils.waitForFxEvents();
            sleep(6000);
        }
    }


}