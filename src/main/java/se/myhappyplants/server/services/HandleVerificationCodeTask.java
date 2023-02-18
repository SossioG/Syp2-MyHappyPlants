package se.myhappyplants.server.services;

import java.security.SecureRandom;

public class HandleVerificationCodeTask implements Runnable
{
    private int code;
    private SecureRandom secureRandom;

    public HandleVerificationCodeTask()
    {
        this.secureRandom = new SecureRandom();
    }


    @Override
    public void run()
    {
        code = secureRandom.nextInt(100000,999999);
    }
}
