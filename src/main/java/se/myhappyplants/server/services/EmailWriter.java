package se.myhappyplants.server.services;

import jakarta.mail.MessagingException;
import se.myhappyplants.shared.Email;
import se.myhappyplants.shared.User;

public class EmailWriter
{
    private User client;
    private String notification;

    public EmailWriter()
    {
        this.notification = "";
    }

    public void writeNotification()
    {
        notification = String.format("");
    }

    public void sendNotification() throws MessagingException
    {
        Email.createEmail(client.getEmail(), "mau@diggins.se","MyHappyPlants: Notification",notification);
    }

}
