package se.myhappyplants.robots;

import se.myhappyplants.server.services.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBRobot {
    private static DatabaseConnection connection = null;

    public static void closeConnection() {
        connection.closeConnection();
    }

    private static void connect() {
        connection = new DatabaseConnection("am3281");
    }

    public static ResultSet getQuery(String query) throws SQLException {
        if(connection == null) {
            connect();
        }

        Statement statement = connection.getConnection().createStatement();
        return statement.executeQuery(query);
    }

    public static void runQuery(String query) throws SQLException {
        if(connection == null) {
            connect();
        }

        try(Statement statement = connection.getConnection().createStatement()) {
            statement.execute(query);
        } finally {
            connection.closeConnection();
        }
    }
}
