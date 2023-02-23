package se.myhappyplants.robots;

import se.myhappyplants.server.services.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBRobot {
    private static DatabaseConnection connection = null;

    private DBRobot() {}

    public static void closeConnection() {
        if(connection != null) {
            connection.closeConnection();
            connection = null;
        }
    }

    private static void connect() {
        if(connection == null) connection = new DatabaseConnection("am3281");
    }

    public static ResultSet getQuery(String query) throws SQLException {
        connect();

        Statement statement = connection.getConnection().createStatement();
        return statement.executeQuery(query);
    }

    public static void runQuery(String query) throws SQLException {
        connect();

        try(Statement statement = connection.getConnection().createStatement()) {
            statement.execute(query);
        }
    }
}
