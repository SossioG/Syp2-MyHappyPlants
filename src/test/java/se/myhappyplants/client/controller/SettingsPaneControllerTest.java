package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.concurrent.Callable;

import static se.myhappyplants.shared.MessageBoxTest.assertMessageAndClick;

public class SettingsPaneControllerTest extends ApplicationTest {
    private final String email = "settings@pane.test";
    private final String username = "settingspanetest";
    private final String password = "Kaffekopp1";

    @Override
    public void start(Stage stage) throws Exception {
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
    }

    void setDBToFalse() {
        try {
            DBRobot.runQuery("UPDATE tuser SET notification_activated=FALSE, fun_facts_activated=FALSE WHERE username='settingspanetest';");
            ResultSet resultSet = DBRobot.getQuery("SELECT notification_activated, fun_facts_activated FROM tuser WHERE username='settingspanetest';");
            resultSet.next();
            Assertions.assertThat(resultSet.getBoolean("notification_activated")).isFalse();
            Assertions.assertThat(resultSet.getBoolean("fun_facts_activated")).isFalse();
        } catch(SQLException e) {
            Assertions.fail("Failed to set DB to false: " + e.getMessage());
        }
    }

    void setDBToTrue() {
        try {
            DBRobot.runQuery("UPDATE tuser SET notification_activated=TRUE, fun_facts_activated=TRUE WHERE username='settingspanetest';");
            ResultSet resultSet = DBRobot.getQuery("SELECT notification_activated, fun_facts_activated FROM tuser WHERE username='settingspanetest';");
            resultSet.next();
            Assertions.assertThat(resultSet.getBoolean("notification_activated")).isTrue();
            Assertions.assertThat(resultSet.getBoolean("fun_facts_activated")).isTrue();
        } catch(SQLException e) {
            Assertions.fail("Failed to set DB to true: " + e.getMessage());
        }
    }

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
        for(int retries = 0; retries < 20; retries++) {
            try {
                runnable.run();
                break;
            } catch(EmptyNodeQueryException e) {
                if(retries == 19)
                    Assertions.fail(String.format("%s: %s", errorMessage.trim(), e.getMessage()));
                Thread.sleep(100);
            }
        }
    }

    void tryAndWait(Callable<Boolean> callable, String errorMessage) throws InterruptedException {
        boolean success = false;
        for(int retries = 0; retries < 20 && !success; retries++) {
            try {
                success = callable.call();
            } catch(EmptyNodeQueryException e) {
                if(retries == 19)
                    Assertions.fail(String.format("%s: %s", errorMessage.trim(), e.getMessage()));
            } catch(Exception e) {
                Assertions.fail(String.format("%s: %s", errorMessage.trim(), e.getMessage()));
                e.printStackTrace();
            }

            if(!success && retries == 19)
                Assertions.fail(errorMessage);
            else if(!success)
                Thread.sleep(100);
        }
    }

    @Test
    void notificationToOn() throws InterruptedException {
        setDBToFalse();
        login();

        KeyRobot.click("#tglBtnChangeNotification");
        assertMessageAndClick("Notification settings has been changed.");

        Callable<Boolean> callable = () -> {
            ToggleButton node = (ToggleButton) KeyRobot.getNode("#tglBtnChangeNotification");
            return node.getText().equals("On");
        };


        tryAndWait(callable, "Failed to change notification button to On.");
        Node node = KeyRobot.getNode("#tglBtnChangeNotification");
        Assertions.assertThat(node).isNotNull().isInstanceOf(ToggleButton.class);
        Assertions.assertThat(((ToggleButton) node).getText()).isEqualTo("On");

        try {
            ResultSet resultSet = DBRobot.getQuery("SELECT notification_activated FROM tuser WHERE username='settingspanetest';");
            resultSet.next();
            Assertions.assertThat(resultSet.getBoolean("notification_activated")).isTrue();
        } catch(SQLException e) {
            Assertions.fail("Failed to get result from database: " + e.getMessage());
        }
    }

    @Test
    void notificationToOff() throws InterruptedException {
        setDBToTrue();
        login();

        KeyRobot.click("#tglBtnChangeNotification");
        assertMessageAndClick("Notification settings has been changed.");

        Callable<Boolean> callable = () -> {
            ToggleButton node = (ToggleButton) KeyRobot.getNode("#tglBtnChangeNotification");
            return node.getText().equals("Off");
        };

        tryAndWait(callable, "Failed to change notification button to Off.");
        Node node = KeyRobot.getNode("#tglBtnChangeNotification");
        Assertions.assertThat(node).isNotNull().isInstanceOf(ToggleButton.class);
        Assertions.assertThat(((ToggleButton) node).getText()).isEqualTo("Off");

        try {
            ResultSet resultSet = DBRobot.getQuery("SELECT notification_activated FROM tuser WHERE username='settingspanetest';");
            resultSet.next();
            Assertions.assertThat(resultSet.getBoolean("notification_activated")).isFalse();
        } catch(SQLException e) {
            Assertions.fail("Failed to get result from database: " + e.getMessage());
        }
    }

    @Test
    void funFactsToOn() throws InterruptedException {
        setDBToFalse();
        login();

        KeyRobot.click("#tglBtnChangeFunFacts");
        assertMessageAndClick("Fun Facts settings has been changed.");

        Callable<Boolean> callable = () -> {
            ToggleButton node = (ToggleButton) KeyRobot.getNode("#tglBtnChangeFunFacts");
            return node.getText().equals("On");
        };


        tryAndWait(callable, "Failed to change fun facts button to On.");
        Node node = KeyRobot.getNode("#tglBtnChangeFunFacts");
        Assertions.assertThat(node).isNotNull().isInstanceOf(ToggleButton.class);
        Assertions.assertThat(((ToggleButton) node).getText()).isEqualTo("On");

        try {
            ResultSet resultSet = DBRobot.getQuery("SELECT fun_facts_activated FROM tuser WHERE username='settingspanetest';");
            resultSet.next();
            Assertions.assertThat(resultSet.getBoolean("fun_facts_activated")).isTrue();
        } catch(SQLException e) {
            Assertions.fail("Failed to get result from database: " + e.getMessage());
        }
    }

    @Test
    void funFactsToOff() throws InterruptedException {
        setDBToTrue();
        login();

        KeyRobot.click("#tglBtnChangeFunFacts");
        assertMessageAndClick("Fun Facts settings has been changed.");

        Callable<Boolean> callable = () -> {
            ToggleButton node = (ToggleButton) KeyRobot.getNode("#tglBtnChangeFunFacts");
            return node.getText().equals("Off");
        };

        tryAndWait(callable, "Failed to change notification button to Off.");
        Node node = KeyRobot.getNode("#tglBtnChangeFunFacts");
        Assertions.assertThat(node).isNotNull().isInstanceOf(ToggleButton.class);
        Assertions.assertThat(((ToggleButton) node).getText()).isEqualTo("Off");

        try {
            ResultSet resultSet = DBRobot.getQuery("SELECT fun_facts_activated FROM tuser WHERE username='settingspanetest';");
            resultSet.next();
            Assertions.assertThat(resultSet.getBoolean("fun_facts_activated")).isFalse();
        } catch(SQLException e) {
            Assertions.fail("Failed to get result from database: " + e.getMessage());
        }
    }
}
