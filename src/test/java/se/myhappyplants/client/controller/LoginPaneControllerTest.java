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
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static se.myhappyplants.shared.MessageBoxTest.assertMessageAndClick;

/**
 * Test till --> 'ANV11F - Glömt lösenord (Must) #19'
 * Av Sossio
 */

class LoginPaneControllerTest extends ApplicationTest {

    private final String email = "verify@email.test";

    @Override
    public void start(Stage stage) throws Exception {
        URL fxml = LoginPaneController.class.getResource("loginPane" + ".fxml");
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
        KeyRobot.setText("#passFldPassword", "password");
        KeyRobot.click("#test", 60);

        tryAndWait(() -> assertMessageAndClick("Now logged in as " + "username"), "Failed to login");
        tryAndWait(() -> KeyRobot.click("#settingsTab"), "Failed to click settings tab");
    }

    void goToForgotPassword() throws InterruptedException {
        KeyRobot.click("#forgotPassword", 2000);
        KeyRobot.setText("#txtFldEmail", email);
        KeyRobot.click("#verifyButton", 2000);

        tryAndWait(() -> assertMessageAndClick("()"), "()");


    }

    private void assertUserNotExists() {
        String query = String.format("SELECT * FROM tuser WHERE email = '%s'", email);
        try(ResultSet result = DBRobot.getQuery(query)) {
            Assertions.assertThat(result.next()).isFalse();
        } catch(SQLException e) {
            Assertions.fail(e.getMessage());
        }
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

//    @Test
//    void notificationToOn() throws InterruptedException {
//        setDBToFalse();
//        login();
//
//        KeyRobot.click("#tglBtnChangeNotification");
//        assertMessageAndClick("Notification settings has been changed.");
//
//        Callable<Boolean> callable = () -> {
//            ToggleButton node = (ToggleButton) KeyRobot.getNode("#tglBtnChangeNotification");
//            return node.getText().equals("On");
//        };
//
//
//        tryAndWait(callable, "Failed to change notification button to On.");
//        Node node = KeyRobot.getNode("#tglBtnChangeNotification");
//        Assertions.assertThat(node).isNotNull().isInstanceOf(ToggleButton.class);
//        Assertions.assertThat(((ToggleButton) node).getText()).isEqualTo("On");
//
//        try {
//            ResultSet resultSet = DBRobot.getQuery("SELECT notification_activated FROM tuser WHERE username='settingspanetest';");
//            resultSet.next();
//            Assertions.assertThat(resultSet.getBoolean("notification_activated")).isTrue();
//        } catch(SQLException e) {
//            Assertions.fail("Failed to get result from database: " + e.getMessage());
//        }
//    }

    @Test
    void verifyMail() throws InterruptedException {
        goToForgotPassword();
    }

//    @Test
//    void generateVerificationCode() {
//
//    }

//    @Test
//    void verifyMatchingVerCode() {
//
//    }
}