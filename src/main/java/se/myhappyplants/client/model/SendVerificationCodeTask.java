package se.myhappyplants.client.model;

import jakarta.mail.MessagingException;
import se.myhappyplants.shared.Email;

public class SendVerificationCodeTask implements Runnable
{
    private int code;
    private String mail;

    public SendVerificationCodeTask(int code, String mail)
    {
        this.code = code;
        this.mail = mail;
    }

    @Override
    public void run()
    {
        sendVerificationCode(code,mail);
    }

    //Send a generated Verification code to user
    public void sendVerificationCode(int code,String mail)
    {
        String codeMsg = String.format("Your Verification code is: " + "%s",code);
        try
        {
            Email.postEmail(mail,"VerificationCode",codeMsg);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}
