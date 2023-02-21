package se.myhappyplants.server.services;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class HandleVerificationCodeTask implements Runnable
{
    private int code;
    private boolean validCode;

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public HandleVerificationCodeTask()
    {
        this.validCode = false;
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
