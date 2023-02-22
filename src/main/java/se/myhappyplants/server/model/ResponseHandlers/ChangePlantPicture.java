package se.myhappyplants.server.model.ResponseHandlers;

import se.myhappyplants.server.model.IResponseHandler;
import se.myhappyplants.server.services.UserPlantRepository;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.PlantDepricated;
import se.myhappyplants.shared.User;

public class ChangePlantPicture implements IResponseHandler {

    private UserPlantRepository userPlantRepository;

    public ChangePlantPicture(UserPlantRepository userPlantRepository) {
        this.userPlantRepository = userPlantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        PlantDepricated plantDepricated = request.getPlant();
        if (userPlantRepository.changePlantPicture(user, plantDepricated)) {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}
