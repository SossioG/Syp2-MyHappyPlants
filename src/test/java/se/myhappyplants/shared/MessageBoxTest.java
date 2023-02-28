package se.myhappyplants.shared;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.testfx.assertions.api.Assertions;
import org.testfx.service.query.EmptyNodeQueryException;
import se.myhappyplants.robots.KeyRobot;

public class MessageBoxTest {
    public static void assertMessageAndClick(String expected) {
        Runnable runnable = () -> {
            Node messageLbl = KeyRobot.getNode("#messageLbl");
            Assertions.assertThat(messageLbl).isNotNull().isInstanceOf(Label.class);
            Assertions.assertThat(((Label) messageLbl).getText()).isEqualTo(expected);
        };

        for(int retries = 0; retries < 20; retries++) {
            try {
                runnable.run();
                break;
            } catch(EmptyNodeQueryException e) {
                if(retries == 19)
                    Assertions.fail(String.format("Failed to find #messageLbl: %s", e.getMessage()));
                else
                    try {
                        Thread.sleep(100);
                    } catch(InterruptedException ex) {
                        Assertions.fail("Failed to sleep: " + ex.getMessage());
                    }
            }
        }



        Node okButton = KeyRobot.getNode("#okButton");
        Assertions.assertThat(okButton).isNotNull().isInstanceOf(Button.class);

        KeyRobot.click(okButton);
    }
}
