package se.myhappyplants.shared;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.testfx.assertions.api.Assertions;
import se.myhappyplants.robots.KeyRobot;

public class MessageBoxTest {
    public static void assertMessageAndClick(String expected) {
        Node messageLbl = KeyRobot.getNode("#messageLbl");
        Assertions.assertThat(messageLbl).isNotNull().isInstanceOf(Label.class);
        Assertions.assertThat(((Label) messageLbl).getText()).isEqualTo(expected);

        Node okButton = KeyRobot.getNode("#okButton");
        Assertions.assertThat(okButton).isNotNull().isInstanceOf(Button.class);

        KeyRobot.click(okButton);
    }
}
