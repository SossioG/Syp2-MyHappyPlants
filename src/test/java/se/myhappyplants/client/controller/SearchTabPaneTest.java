package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.ListViewMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.robots.DBRobot;
import se.myhappyplants.robots.KeyRobot;
import se.myhappyplants.server.StartServer;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;

import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.testfx.api.FxAssert.verifyThat;


/**
        @author Yara Rajjoub
 */



public class SearchTabPaneTest extends ApplicationTest {

    private Button btnSearch;
    private ListView<Plant> listViewResult;
    private TextField txtFldSearchText;
    private TextField txtNbrOfResults;
    private ProgressIndicator progressIndicator;
    private ArrayList<Plant> searchResults;

    private final String email = "settings@pane.test";
    private final String username = "settingspanetest";
    private final String password = "Kaffekopp1";

    @Override
    public void start(Stage stage) throws Exception {
        Message loginMessage = new Message(MessageType.login, new User(email, password));
        ServerConnection connection = ServerConnection.getClientConnection();
        Message loginResponse = connection.makeRequest(loginMessage);

        Assertions.assertThat(loginResponse).isNotNull();
        Assertions.assertThat(loginResponse.isSuccess()).isTrue();
        LoggedInUser.getInstance().setUser(loginResponse.getUser());

        //mainNode låter applikationen refresha efter varje test.
        URL fxml = RegisterPaneController.class.getResource("searchTabPane" + ".fxml");
        Assertions.assertThat(fxml).isNotNull();
        Parent mainNode = FXMLLoader.load(fxml);
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
        StartClient.setScene(stage.getScene());
    }

    @BeforeAll
    static void startUp() {
        try {
            StartServer.main(null);
        } catch(UnknownHostException | SQLException e) {
            Assertions.fail("Failed to start server: " + e.getMessage());
        }
    }
    @BeforeEach
    void login() {
        // Insert a test user into the test database
        String query = String.format("INSERT INTO tuser (id,username,email,password, notification_activated, fun_facts_activated) VALUES ('30', 'testuser', 'testuser@example.com', 'testuser123', true, true);" );
        try {
            DBRobot.runQuery(query);
        }catch (SQLException e) {
            throw new RuntimeException("Database connection failed. Failed to add test user.", e);
        }

        /**  Simulate user actions to log in */
        KeyRobot.clickAndWrite("#txtFldEmail", "testuser@example.com",1);
        KeyRobot.clickAndWrite("#passFldNewPassword", "testpassword",1);
      //KeyRobot.clickAndWrite("#txtFldNewUsername","testuser"); behövs ej nu
        KeyRobot.click("#test");

        /** Wait for the search screen to appear / tills FX är färdig*/
     // await().until(scene.lookup("#txtFldSearchText").isVisible());
        WaitForAsyncUtils.waitForFxEvents();

        // Wait for the search screen to appear
        btnSearch = lookup("#btnSearch").query();
        listViewResult = lookup("#listViewResult").query();
        txtFldSearchText = lookup("#txtFldSearchText").query();
        txtNbrOfResults = lookup("#txtNbrOfResults").query();
        progressIndicator = lookup("#progressIndicator").query();

    }
    @AfterEach
    void deleteUser() {
        String query = String.format("DELETE FROM tuser WHERE id = '30';");
        try {
            DBRobot.runQuery(query);
        } catch(SQLException e) {
            Assertions.fail("Database connection failed. Failed to delete test user.");
        }

        clickOn("#btnLogout");
        WaitForAsyncUtils.waitForFxEvents();
        //await().until(scene.lookup("#txtFldEmail").isVisible());

    }
    @AfterEach // helping resource management, run after each test and close the application
    public void tearDown() throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{}); //release the mouse and keyboard
        release(new MouseButton[]{}); //release the mouse and keyboard
    }
    @AfterAll
    static void shutDown() {
        DBRobot.closeConnection();
    }

    //TEST the search method
    @Test
    void searchButtonPressed() {
        // Simulate user actions to perform a search
        clickOn("#txtFldSearchText").write("Meadowsweet"); //write in the search field
        clickOn("#btnSearch");


        // Wait for the search to complete and results to be displayed
        // Awaitility.await().until(listViewResult.getItems().sizeProperty().isEqualTo(1));
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat(txtNbrOfResults, TextInputControlMatchers.hasText("1 results"));
        verifyThat(listViewResult, ListViewMatchers.hasItems(1));

        // Assert that the search results are correct
        Plant result = listViewResult.getItems().get(0);

        String plantId =result.getPlantId();
        Assertions.assertThat(plantId).isEqualTo("262017");

        String commonName = result.getCommonName();
        Assertions.assertThat(commonName).isEqualTo("Meadowsweet");

        String scientificName = result.getScientificName();
        Assertions.assertThat(scientificName).isEqualTo("Filipendula ulmaria");

        // Extract familyName from the toString method
        String toStringFamilyName = result.toString();
        String[] toStringParts = toStringFamilyName.split("\t");
        String familyName = toStringParts[1].substring("Family name: ".length());
        Assertions.assertThat(familyName).isEqualTo("Rosaceae");

       String imageURL = result.getImageURL();
       Assertions.assertThat(imageURL).isEqualTo("https://bs.plantnet.org/image/o/53c73903dc455a3d734b193dad7d9d8c4ec0e324");
    }

    //TESTS
    @Test
    public void addPlantToCurrentUserLibrary() {
        //To test if successfully added the plant to library
    }
    @Test
    public void showFunFact() {
    }



}
