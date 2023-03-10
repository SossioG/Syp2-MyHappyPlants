package se.myhappyplants.server.services;

import org.mindrot.jbcrypt.BCrypt;
import se.myhappyplants.shared.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Class responsible for calling the database about users.
 * Created by: Frida Jacobsson 2021-03-30
 * Updated by: Frida Jacobsson 2021-05-21
 */
public class UserRepository {

    private IQueryExecutor database;

    public UserRepository(IQueryExecutor database){
       this.database = database;
    }

    /**
     * Method to save a new user using BCrypt.
     * @param user An instance of a newly created User that should be stored in the database.
     * @return A boolean value, true if the user was stored successfully
     */
    public boolean saveUser(User user) {
        boolean success = false;
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String sqlSafeUsername = user.getUsername().replace("'", "''");
        String query = String.format("INSERT INTO tuser (username, email, password, notification_activated, fun_facts_activated) VALUES ('%s', '%s', '%s',true,true);", sqlSafeUsername, user.getEmail(), hashedPassword);
        try {
            database.executeUpdate(query);
            success = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return success;
    }

    /**
     * Method to check if a user exists in database.
     * Purpose of method is to make it possible for user to log in
     * @param email    typed email from client and the application
     * @param password typed password from client and the application
     * @return A boolean value, true if the user exist in database and the password is correct
     */
    public boolean checkLogin(String email, String password) {
        boolean isVerified = false;
        String query = String.format("SELECT password FROM tuser WHERE email = '%s';", email);
        try {
            ResultSet resultSet = database.executeQuery(query);
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString(1);
                isVerified = BCrypt.checkpw(password, hashedPassword);
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isVerified;
    }

    //checks if database has entered mail adress
    public boolean checkMatchingMail(String email)
    {
        String query = String.format("SELECT EXISTS(SELECT * FROM tuser WHERE email = '%s');", email);
        boolean result = false;
        try {
            ResultSet resultSet = database.executeQuery(query);

            if(resultSet.next())
            {
                result = resultSet.getBoolean(1);
            }
            if (result) {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    public boolean updateUserPassword(String password, String mail) {
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = String.format("UPDATE tuser SET password = '%s' WHERE email = '%s';",password, mail);
        try
        {
            database.executeUpdate(query);
            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * Method to get information (id, username and notification status) about a specific user
     * @param email ??
     * @return a new instance of USer
     */
    public User getUserDetails(String email) {
        User user = null;
        int uniqueID = 0;
        String username = null;
        boolean notificationActivated = false;
        boolean funFactsActivated = false;
        String query = String.format("SELECT id, username, notification_activated, fun_facts_activated FROM tuser WHERE email = '%s';", email);
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                uniqueID = resultSet.getInt(1);
                username = resultSet.getString(2);
                notificationActivated = resultSet.getBoolean(3);
                funFactsActivated = resultSet.getBoolean(4);
            }
            user = new User(uniqueID, email, username, notificationActivated, funFactsActivated);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return user;
    }

    /**
     * Method to delete a user and all plants in user library at once
     * author: Frida Jacobsson
     * @param email
     * @param password
     * @return boolean value, false if transaction is rolled back
     * @throws SQLException
     */
    public boolean deleteAccount(String email, String password) {
        boolean accountDeleted = false;
        if (checkLogin(email, password)) {
            String querySelect = String.format("SELECT id from tuser WHERE email = '%s';", email);
            try {
                Statement statement = database.beginTransaction();
                ResultSet resultSet = statement.executeQuery(querySelect);
                if (!resultSet.next()) {
                    throw new SQLException();
                }
                int id = resultSet.getInt(1);
                String queryDeletePlants = String.format("DELETE FROM plant WHERE user_id = %d;", id);
                statement.executeUpdate(queryDeletePlants);
                String queryDeleteUser = String.format("DELETE FROM tuser WHERE id = %d;", id);
                statement.executeUpdate(queryDeleteUser);
                database.endTransaction();
                accountDeleted = true;
            }
            catch (SQLException sqlException) {
                try {
                   database.rollbackTransaction();
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return accountDeleted;
    }

    public boolean changeNotifications(User user, boolean notifications) {
        boolean notificationsChanged = false;
        boolean notificationsActivated = false;
        if (notifications) {
            notificationsActivated = true;
        }
//      String query = "UPDATE [User] SET notification_activated = " + notificationsActivated + " WHERE email = '" + user.getEmail() + "';";
        String query = String.format("UPDATE tuser SET notification_activated = %s WHERE email = '%s';", notificationsActivated, user.getEmail());

        try {
            database.executeUpdate(query);
            notificationsChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return notificationsChanged;
    }

    public boolean changeFunFacts(User user, Boolean funFactsActivated) {
        boolean funFactsChanged = false;
        boolean funFactsBitValue = false;
        if (funFactsActivated) {
            funFactsBitValue = true;
        }
//      String query = "UPDATE [User] SET fun_facts_activated = " + funFactsBitValue + " WHERE email = '" + user.getEmail() + "';";
        String query = String.format("UPDATE tuser SET fun_facts_activated = %s WHERE email = '%s';", funFactsBitValue, user.getEmail());
        try {
            database.executeUpdate(query);
            funFactsChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return funFactsChanged;
    }

    public HashMap<Integer,String> getUsers() {

        HashMap<Integer,String> users= new HashMap<>();

        String query = "SELECT id, email;";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                users.put(resultSet.getInt(1), resultSet.getString(2));
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return users;
    }
}

