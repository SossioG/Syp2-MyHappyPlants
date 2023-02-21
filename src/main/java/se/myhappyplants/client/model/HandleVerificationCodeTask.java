package se.myhappyplants.client.model;

import java.util.concurrent.ThreadLocalRandom;

public class HandleVerificationCodeTask implements Runnable
{
    private int code;

    public HandleVerificationCodeTask()
    {
    }

    //todo Generate code valid for 10 min
    // listen for user input on a socket
    // return true/false if code is correct

    @Override
    public void run()
    {
        code = ThreadLocalRandom.current().nextInt(100000,999999 + 1);
    }

    public int getCode() {
        return code;
    }
}
