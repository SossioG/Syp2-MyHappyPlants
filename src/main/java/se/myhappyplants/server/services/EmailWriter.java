package se.myhappyplants.server.services;

import com.google.api.services.gmail.model.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;

/* Class to demonstrate the use of Gmail Create Message API */
public class EmailWriter
{

    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException        - if service account credentials file not found.
     * @throws MessagingException - if a wrongly formatted address is encountered.
     */
    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}
