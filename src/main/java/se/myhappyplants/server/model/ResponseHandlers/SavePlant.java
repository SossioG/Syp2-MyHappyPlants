package se.myhappyplants.server.model.ResponseHandlers;

import se.myhappyplants.server.model.IResponseHandler;
import se.myhappyplants.server.services.UserPlantRepository;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.PlantDepricated;
import se.myhappyplants.shared.User;
/**
 * Class that saved a users plant
 */
public class SavePlant implements IResponseHandler {
    private UserPlantRepository userPlantRepository;

    public SavePlant(UserPlantRepository userPlantRepository) {
        this.userPlantRepository = userPlantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        PlantDepricated plantDepricated = request.getPlant();
        if (userPlantRepository.savePlant(user, plantDepricated)) {
            response = new Message(true);

        } else {
            response = new Message(false);
        }
        return response;
    }
}
