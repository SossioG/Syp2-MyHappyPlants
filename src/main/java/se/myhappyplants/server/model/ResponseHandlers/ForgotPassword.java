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

    //if Server contains matching mail, return a true response
    @Override
    public Message getResponse(Message request) {
        Message response;
        String mail = request.getMessageText();

        System.out.println("Check the email: " + mail);
        if (userRepository.checkMatchingMail(mail)) {
            request.setSuccess(true);
        } else {
            request.setSuccess(false);
        }
        response = request;
        return response;
    }
}
