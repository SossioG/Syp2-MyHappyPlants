package se.myhappyplants.robots;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class KeyRobot {
    private static final FxRobot fxRobot = new FxRobot();
    private static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch(AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static Node getNode(String query) {
        return fxRobot.lookup(query).query();
    }

    public static void click(String query) {
        WaitForAsyncUtils.waitForFxEvents();
        Bounds bounds = fxRobot.bounds(getNode(query)).query();

        double x = bounds.getMaxX() - bounds.getWidth() / 2;
        double y = bounds.getMaxY() - bounds.getHeight() / 2;

        robot.mouseMove((int)x, (int)y);
        robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
    }

    public static void writeText(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public static void clickAndWrite(String query, String text) {
        click(query);
        writeText(text);
    }
}
