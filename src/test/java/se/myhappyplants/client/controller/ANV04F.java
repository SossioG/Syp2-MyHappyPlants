package se.myhappyplants.client.controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.service.query.EmptyNodeQueryException;
import se.myhappyplants.robots.DBRobot;
import se.myhappyplants.robots.KeyRobot;
import se.myhappyplants.server.StartServer;
import se.myhappyplants.server.services.IQueryExecutor;
import se.myhappyplants.shared.User;

import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

import static se.myhappyplants.shared.MessageBoxTest.assertMessageAndClick;

public class ANV04F extends ApplicationTest {

    /**
     * Reqid: ANV04F
     * Borttagning av konto: En användare ska kunna ta bort sin konto från databasen
     * och då ska användarens uppgifter i form av e-postadress och lösenord raderas.
     */

    private final String email = "delete.account@test.com";
    private final String username = "MrDelete";
    private final String password = "Delete12";

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

    private boolean createUser() {
        boolean success = false;
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sqlSafeUsername = username.replace("'", "''");
        String query = String.format("INSERT INTO tuser (username, email, password, notification_activated, fun_facts_activated) VALUES ('%s', '%s', '%s',true,true);", sqlSafeUsername, email, hashedPassword);
        try {
            DBRobot.runQuery(query);
            success = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return success;
    }

    public boolean deleteAccount() {
        boolean accountDeleted = false;
        try {
            String querySelect = String.format("SELECT id from tuser WHERE email = '%s';", email);
            DBRobot.runQuery(querySelect);

            ResultSet resultSet = DBRobot.getQuery("querySelect");
            resultSet.next();

            int id = resultSet.getInt(1);

            if(id != 0) {
                String queryDeletePlants = String.format("DELETE FROM plant WHERE user_id = %d;", id);
                DBRobot.runQuery(queryDeletePlants);
                String queryDeleteUser = String.format("DELETE FROM tuser WHERE id = %d;", id);
                DBRobot.runQuery(queryDeleteUser);
            }

            accountDeleted = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return accountDeleted;
    }

    void login() throws InterruptedException {
        KeyRobot.setText("#txtFldEmail", email);
        KeyRobot.setText("#passFldPassword", password);
        KeyRobot.click("#test", 60);

        tryAndWait(() -> assertMessageAndClick("Now logged in as " + username), "Failed to login");
        tryAndWait(() -> KeyRobot.click("#settingsTab"), "Failed to click settings tab");
    }

    @AfterAll
    static void shutDown() throws SQLException {
        //DBRobot.runQuery("DELETE FROM tuser WHERE email = 'delete.account@test.com';");
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
    @DisplayName("Check delete with empty password field")
    public void validDelete() throws InterruptedException {
        //createUser();
        login();
        KeyRobot.click("#btnDeleteAccount");
        assertMessageAndClick("All your personal information will be deleted and cannot be restored.");

        //deleteAccount();

    }
}
