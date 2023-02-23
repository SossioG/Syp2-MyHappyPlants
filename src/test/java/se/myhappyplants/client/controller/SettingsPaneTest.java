package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.service.query.EmptyNodeQueryException;
import se.myhappyplants.robots.DBRobot;
import se.myhappyplants.robots.KeyRobot;
import se.myhappyplants.server.StartServer;

import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static se.myhappyplants.shared.MessageBoxTest.assertMessageAndClick;

public class SettingsPaneTest extends ApplicationTest {
    private final String email = "settings@pane.test";
    private final String username = "settingspanetest";
    private final String password = "Kaffekopp1";

    @Override
    public void start(Stage stage) throws Exception {
        // Message loginMessage = new Message(MessageType.login, new User(email, password));
        // ServerConnection connection = ServerConnection.getClientConnection();
        // Message loginResponse = connection.makeRequest(loginMessage);
        //
        // Assertions.assertThat(loginResponse).isNotNull();
        // Assertions.assertThat(loginResponse.isSuccess()).isTrue();
        // LoggedInUser.getInstance().setUser(loginResponse.getUser());
        //
        // URL fxml = RegisterPaneController.class.getResource("settingsTabPane" + ".fxml");
        URL fxml = RegisterPaneController.class.getResource("loginPane" + ".fxml");
        Assertions.assertThat(fxml).isNotNull();
        Parent mainNode = FXMLLoader.load(fxml);
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
        StartClient.setScene(stage.getScene());
    }

    @BeforeAll
    static void startUp() throws SQLException {
        try {
            StartServer.main(null);
        } catch(UnknownHostException | SQLException e) {
            Assertions.fail("Failed to start server: " + e.getMessage());
        }

        DBRobot.runQuery("UPDATE tuser SET notification_activated=FALSE, fun_facts_activated=FALSE WHERE username='settingspanetest';");
        ResultSet resultSet = DBRobot.getQuery("SELECT notification_activated, fun_facts_activated FROM tuser WHERE username='settingspanetest';");
        resultSet.next();
        Assertions.assertThat(resultSet.getBoolean("notification_activated")).isFalse();
        Assertions.assertThat(resultSet.getBoolean("fun_facts_activated")).isFalse();
    }

    @BeforeEach
    void login() throws InterruptedException {
        KeyRobot.setText("#txtFldEmail", email);
        KeyRobot.setText("#passFldPassword", password);
        KeyRobot.click("#test", 60);

        tryAndWait(() -> assertMessageAndClick("Now logged in as " + username), "Failed to login");
        tryAndWait(() -> KeyRobot.click("#settingsTab"), "Failed to click settings tab");
    }

    @AfterAll
    static void shutDown() {
        DBRobot.closeConnection();
    }

    void tryAndWait(Runnable runnable, String errorMessage) throws InterruptedException {
        int retries = 20;
        while(retries-- >= 0) {
            try {
                runnable.run();
                break;
            } catch(EmptyNodeQueryException e) {
                if(retries == 0)
                    Assertions.fail(String.format("%s: %s", errorMessage.trim(), e.getMessage()));
                Thread.sleep(100);
            }
        }
    }

    @Test
    void notificationToOn() throws InterruptedException {
        KeyRobot.click("#tglBtnChangeNotification");

        // assertMessageAndClick("Notification settings has been changed.");

        tryAndWait(() -> assertMessageAndClick("Notification settings has been changed."), "Failed to click settings tab:");
        Node node = KeyRobot.getNode("#tglBtnChangeNotification");
        Assertions.assertThat(node).isNotNull().isInstanceOf(ToggleButton.class);
        Runnable runnable = () -> {
            if(((ToggleButton) node).getText().equals("On"))
                throw new EmptyNodeQueryException("Notification is still on");
        };
        tryAndWait(runnable, "Failed to change notification to off");
        Assertions.assertThat(((ToggleButton) node).getText()).isEqualTo("Off");

        try {
            ResultSet resultSet = DBRobot.getQuery("SELECT notification_activated, fun_facts_activated FROM tuser WHERE username='settingspanetest';");
            resultSet.next();
            Assertions.assertThat(resultSet.getBoolean("notification_activated")).isTrue();
        } catch(SQLException e) {
            Assertions.fail("Failed to get result from database: " + e.getMessage());
        }
    }
}
