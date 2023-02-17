package se.myhappyplants.server.model.ResponseHandlers;

import se.myhappyplants.server.model.IResponseHandler;
import se.myhappyplants.server.services.UserRepository;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;

public class ForgotPassword implements IResponseHandler {

    private UserRepository userRepository;
    public ForgotPassword(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public Message getResponse(Message request) {

        Message response;
        String mail = request.getMessageText();

        if () {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}