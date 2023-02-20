package se.myhappyplants.server.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class HandleVerificationCodeTask implements Runnable {
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

    public int getCode() {
        return code;
    }
}
