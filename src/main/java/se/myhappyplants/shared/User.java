package se.myhappyplants.shared;

import java.io.*;

/**
 * Container class that defines a User.
 * Created by: Linn Borgström.
 * Updated by: Linn Borgström, 2021-05-17.
 */
public class User implements Serializable {

    private int uniqueId;
    private String email;
    private String username;
    private String password;
    private String avatarURL;
    private boolean isNotfActivated = true;
    private boolean funFactsActivated = true;

    /**
     * Constructor used when registering a new user account
     *
     * @param email for user email.
     * @param username for user username.
     * @param password for user password.
     * @param isNotfActivated if notifications are activated or not.
     */
    public User(String email, String username, String password, boolean isNotfActivated) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isNotfActivated = isNotfActivated;
    }

    /**
     * Constructor 1 for login requests.
     * @param email for user email.
     * @param password for user password.
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor 2 for login requests.
     * @param uniqueID of the user.
     * @param email of the user.
     * @param username of the user.
     * @param notfActivated true or false.
     */
    public User(int uniqueID, String email, String username, boolean notfActivated) {
        this.uniqueId = uniqueID;
        this.email = email;
        this.username = username;
        this.isNotfActivated = notfActivated;
    }

    /**
     * Constructor used to return a users details from the database
     * @param uniqueId A user's unique id in the database
     * @param email email address of the user.
     * @param username of the user.
     * @param isNotfActivated true if notifications are wanted.
     * @param funFactsActivated true if fun facts wanted.
     */
    public User(int uniqueId, String email, String username, boolean isNotfActivated, boolean funFactsActivated) {
        this.uniqueId = uniqueId;
        this.email = email;
        this.username = username;
        this.isNotfActivated = isNotfActivated;
        this.funFactsActivated = funFactsActivated;
    }

    // getters and setters
    public int getUniqueId() {
        return uniqueId;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public boolean areNotificationsActivated() {
        return isNotfActivated;
    }

    public void setIsNotificationsActivated(boolean notificationsActivated) {
        this.isNotfActivated = notificationsActivated;
    }
    public void setFunFactsActivated(boolean funFactsActivated) {
        this.funFactsActivated = funFactsActivated;
    }

    public String getAvatarURL() {
        return avatarURL;
    }
    public void setAvatar(String pathToImg) {
        this.avatarURL = new File(pathToImg).toURI().toString();
    }

    public boolean areFunFactsActivated() {
        return funFactsActivated;
    }
}
