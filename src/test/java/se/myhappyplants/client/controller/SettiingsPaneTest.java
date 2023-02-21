package se.myhappyplants.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;

import java.net.URL;

public class SettiingsPaneTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        //mainNode låter applikationen refresha efter varje test.
        URL fxml = RegisterPaneController.class.getResource("settingsPane" + ".fxml");
        Assertions.assertThat(fxml).isNotNull();
        Parent mainNode = FXMLLoader.load(fxml);
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
        // StartClient.setScene(stage.getScene());
    }
}
