package se.myhappyplants.server.model.ResponseHandlers;

import se.myhappyplants.server.model.IResponseHandler;
import se.myhappyplants.server.services.UserRepository;
import se.myhappyplants.shared.Message;

public class UpdatePassword implements IResponseHandler
{
    private UserRepository userRepository;

    public UpdatePassword(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    //if Server contains matching mail, return a true response
    @Override
    public Message getResponse(Message request) {
        Message response;
        String password = request.getMessageText();
        String mail = request.getSecondString();

        if (userRepository.updateUserPassword(password,mail)) { //write method here for calling method to db
            request.setSuccess(true);
        } else {
            request.setSuccess(false);
        }
        response = request;
        return response;
    }
}
