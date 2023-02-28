package se.myhappyplants.client.model;

import java.util.concurrent.ThreadLocalRandom;

public class HandleVerificationCodeTask implements Runnable
{
    private int code;

    public HandleVerificationCodeTask()
    {
    }

    @Override
    public void run()
    {
        code = ThreadLocalRandom.current().nextInt(100000,999999 + 1);
    }

    public int getCode() {
        return code;
    }
}
