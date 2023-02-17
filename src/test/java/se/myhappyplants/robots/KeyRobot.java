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
        click(query, 0);
    }

     public static void click(String query, int delay) {
        Node node = getNode(query);
        click(node, delay);
    }

    public static void click(Node node) {
        click(node, 0);
    }

    public static void click(Node node, int delay) {
        if(delay > 0) {
            try {
                Thread.sleep(delay);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        WaitForAsyncUtils.waitForFxEvents();
        Bounds bounds = fxRobot.bounds(node).query();

        double x = bounds.getMaxX() - bounds.getWidth() / 2;
        double y = bounds.getMaxY() - bounds.getHeight() / 2;

        robot.mouseMove((int)x, (int)y);
        robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
    }

    public static void writeText(String text) {
        writeText(text, 0);
    }

    public static void writeText(String text, int delay) {
        if(delay > 0) {
            try {
                Thread.sleep(delay);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

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

    public static void clickAndWrite(Node node, String text) {
        click(node);
        writeText(text);
    }

    public static void clearText(String query) {
        clearText(query, 0);
    }

    public static void clearText(String query, int delay) {
        Node node = getNode(query);
        clearText(node, delay);
    }

    public static void clearText(Node node) {
        clearText(node, 0);
    }

    public static void clearText(Node node, int delay) {
        if(delay > 0) {
            try {
                Thread.sleep(delay);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        click(node);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_DELETE);
        robot.keyRelease(KeyEvent.VK_DELETE);
    }
}
