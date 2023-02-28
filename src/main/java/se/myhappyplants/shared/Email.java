package se.myhappyplants.shared;

import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

//todo hämta users mail adress och assign till mail
//   skapa en mailsender i servern som skickar ut notificationer
//   hämta notifications sträng från klienten som ska skrivas in i mailet
public class Email
{
    //for testing only
    public static void main(String[]args) throws MessagingException {
        Email.postEmail("dannygazic@gmail.com","notificationtest","hello");
    }

    public static void postEmail(String toEmailAddress,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "mailcluster.loopia.se");
        props.put("mail.smtp.port", "587");

        jakarta.mail.Authenticator auth = new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mau@diggins.se", "Gubbelgum123");
            }
        };

        Session session = Session.getInstance(props, auth);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress("mau@diggins.se")); //input the applications mail
        email.addRecipient(jakarta.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));   //the user to send mail to
        email.setSubject(subject); //subject
        email.setText(bodyText); //what the mail should say
        Transport.send(email); //send the mail
    }


}
