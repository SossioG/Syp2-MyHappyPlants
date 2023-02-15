package se.myhappyplants.shared;

import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;


public class Email
{

    //example
       /*
      try {
    MimeMessage msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress("from@example.com"));
    InternetAddress[] address = {new InternetAddress("to@example.com")};
    msg.setRecipients(Message.RecipientType.TO, address);
    msg.setSubject("Jakarta Mail APIs Test");
    msg.addHeader("x-cloudmta-class", "standard");
    msg.addHeader("x-cloudmta-tags", "demo, example");
    msg.setText("Test Message Content");

    Transport.send(msg);

    System.out.println("Message Sent.");
} catch (javax.mail.MessagingException ex) {
    throw new RuntimeException(ex);
}
      */


    public static void main(String[]args) throws MessagingException {
        Email.createEmail("dannygazic@gmail.com","Evout1935@einrot.com","notificationtest","testing");
    }

    public static MimeMessage createEmail(String toEmailAddress,
                                          String fromEmailAddress,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        jakarta.mail.Authenticator auth = new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("kaffekopp1", "Kaffekopp1");
            }
        };

        Session session = Session.getInstance(props, auth);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(fromEmailAddress)); //input the applications mail
        email.addRecipient(jakarta.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));   //the user to send mail to
        email.setSubject(subject); //subject = notification
        email.setText(bodyText); //what the notification mail should say
        Transport.send(email); //send the mail
        return email;
    }


}
