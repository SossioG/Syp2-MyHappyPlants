package se.myhappyplants.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that can be used for communication between Client/Server.
 * Client/Server via TCP.
 * Created by: Christopher O'Driscoll.
 * Updated by: Linn Borgström 2021-05-13.
 */
public class Message implements Serializable {

    private MessageType messageType;
    private boolean notifications;
    private String messageText;
    private User user;
    private boolean success;
    private Date date;
    private ArrayList<Plant> plantArray;
    private Plant plant;
    private String newNickname;
    private PlantDetails plantDetails;

    /**
     * Create a message that can be used to send a boolean value.
     * @param success if message passed.
     */
    public Message(boolean success) {
        this.success = success;
    }

    /**
     * Creates a message which can be used to send a user.
     * @param messageType of the message.
     * @param user user.
     */
    public Message(MessageType messageType, User user) {
        this.messageType = messageType;
        this.user = user;
    }

    /**
     * Creates a message that can be used to send a user and a plant object
     * @param messageType of the message.
     * @param user user.
     * @param plant plant.
     */
    public Message(MessageType messageType, User user, Plant plant) {
        this(messageType, user);
        this.plant = plant;
    }

    /**
     * Create a message that can be used to send a plant object.
     * @param messageType of the message.
     * @param plant plant.
     */
    public Message(MessageType messageType, Plant plant) {
        this.messageType = messageType;
        this.plant = plant;
    }

    /**
     * Creates a message that can be used to send a notification setting and a user.
     * @param messageType of the message.
     * @param notifications notifications.
     * @param user user.
     */
    public Message(MessageType messageType, boolean notifications, User user) {
        this(messageType, user);
        this.notifications = notifications;
    }

    /**
     * Creates a message that can be used to send a user, a plant and a date.
     * @param messageType of the message.
     * @param user user.
     * @param plant plant.
     * @param date current date.
     */
    public Message(MessageType messageType, User user, Plant plant, Date date) {
        this(messageType, user, plant);
        this.date = date;
    }

    /**
     * Creates a message that can be used to send a user, a plant and it's new nickname.
     * @param messageType of the message.
     * @param user user.
     * @param plant plant.
     * @param newNickname of the user.
     */
    public Message(MessageType messageType, User user, Plant plant, String newNickname) {
        this(messageType, user, plant);
        this.newNickname = newNickname;
    }

    /**
     * Creates a message that can be used to send an array of plants.
     * @param success message.
     */
    public Message(ArrayList<Plant> plantArray, boolean success) {
        this.plantArray = plantArray;
        this.success = success;
    }



    /**
     * Creates a message which can be used to send text.
     * @param messageType of the message.
     * @param messageText of the message.
     */
    public Message(MessageType messageType, String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }

    /**
     * Creates a message which can be used to send a user and a boolean value.
     * @param user user.
     * @param success message.
     */
    public Message(User user, boolean success) {
        this.user = user;
        this.success = success;
    }

    /**
     * Creates a message that can be used to send further information about a plant.
     * @param plantDetails details of plants.
     * @param success message.
     */
    public Message(PlantDetails plantDetails, boolean success) {
        this.plantDetails = plantDetails;
        this.success = success;
    }

    // getters and setters
    public String getNewNickname() {
        return newNickname;
    }
    public MessageType getMessageType() {
        return messageType;
    }
    public String getMessageText() {
        return messageText;
    }
    public User getUser() {
        return user;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public ArrayList<Plant> getPlantArray() {
        return plantArray;
    }
    public Plant getPlant() {
        return plant;
    }
    public Date getDate() {
        return date;
    }
    public boolean getNotifications() {
        return notifications;
    }
    public PlantDetails getPlantDetails() {
        return plantDetails;
    }
}
