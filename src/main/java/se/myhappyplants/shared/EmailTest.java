package se.myhappyplants.shared;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailTest implements Runnable
{
    private String toEmailAddress;
    private String subject;
    private String bodyText;

    private Properties props;
    private Session session;
    private MimeMessage email;

    public EmailTest(String toEmailAddress, String subject, String bodyText)
    {
        this.toEmailAddress = toEmailAddress;
        this.subject = subject;
        this.bodyText = bodyText;
    }

    @Override
    public void run()
    {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "mailcluster.loopia.se");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("mau@diggins.se", "Gubbelgum123");
            }
        });

        email = new MimeMessage(session);
        try
        {
            email.setFrom(new InternetAddress("mau@diggins.se")); //input the applications mail
            email.addRecipient(jakarta.mail.Message.RecipientType.TO,
                    new InternetAddress(toEmailAddress));   //the user to send mail to
            email.setSubject(subject); //subject = notification
            email.setText(bodyText); //what the notification mail should say
            Transport.send(email);
        } catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}
