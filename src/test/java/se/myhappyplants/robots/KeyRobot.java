package se.myhappyplants.robots;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
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
        WaitForAsyncUtils.waitForFxEvents();
        return fxRobot.lookup(query).query();
    }

    private static void mouseGlide(double x, double y, int n) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

        double x1 = mouseLocation.getX();
        double y1 = mouseLocation.getY();
        if (n == 0) n = 1;
        int t = 60;
        double dx = (x - x1) / ((double) n);
        double dy = (y - y1) / ((double) n);
        double dt = t / ((double) n);

        for (int step = 1; step <= n; step++) {
            try {
                Thread.sleep((int) dt);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }

            robot.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
        }
    }

    public static void click(String query) {
        click(getNode(query));
    }

     public static void click(String query, int delay) {
        click(getNode(query), delay);
    }

    public static void click(Node node) {
        WaitForAsyncUtils.waitForFxEvents();
        Bounds bounds = fxRobot.bounds(node).query();

        double x = bounds.getMaxX() - bounds.getWidth() / 2;
        double y = bounds.getMaxY() - bounds.getHeight() / 2;

        robot.mouseMove((int)x, (int)y);
        robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
    }

    public static void click(Node node, int delay) {
        WaitForAsyncUtils.waitForFxEvents();
        Bounds bounds = fxRobot.bounds(node).query();

        double x = bounds.getMaxX() - bounds.getWidth() / 2;
        double y = bounds.getMaxY() - bounds.getHeight() / 2;

        mouseGlide(x, y, delay);
        robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
    }

    public static void writeText(String text) {
        writeText(text, 0);
    }

    public static void writeText(String text, int delay) {
        WaitForAsyncUtils.waitForFxEvents();
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
        clickAndWrite(getNode(query), text);
    }

    public static void clickAndWrite(Node node, String text) {
        click(node);
        writeText(text);
    }

    public static void clickAndWrite(String query, String text, int delay) {
        clickAndWrite(getNode(query), text, delay);
    }

    public static void clickAndWrite(Node node, String text, int delay) {
        click(node, delay);
        writeText(text, delay);
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
        WaitForAsyncUtils.waitForFxEvents();
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

    public static void setText(String query, String text) throws IllegalArgumentException {
        Node node = getNode(query);

        if(node instanceof TextField) {
            setText((TextField) node, text);
        } else {
            throw new IllegalArgumentException("Node is not a TextField");
        }
    }

    public static void setText(TextField label, String text) {
        // WaitForAsyncUtils.waitForFxEvents();
        label.setText(text);
    }

    public static void pressEnter() {
        WaitForAsyncUtils.waitForFxEvents();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    //Press and realse a button.
    public static void press(int keyEvent) {
        WaitForAsyncUtils.waitForFxEvents();
        robot.keyPress(keyEvent);
        robot.keyRelease(keyEvent);
    }
}
