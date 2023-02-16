package se.myhappyplants.robots;

import se.myhappyplants.server.services.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBRobot {
    private static DatabaseConnection connection;

    public static void closeConnection() {
        connection.closeConnection();
    }

    public static ResultSet getQuery(String query) throws SQLException {
        connection = new DatabaseConnection("am3281");
        Statement statement = connection.getConnection().createStatement();
        return statement.executeQuery(query);
    }

    public static void runQuery(String query) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection("am3281");

        try(Statement statement = connection.getConnection().createStatement()) {
            statement.execute(query);
        } finally {
            connection.closeConnection();
        }
    }
}
