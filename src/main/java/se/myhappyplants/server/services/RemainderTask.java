package se.myhappyplants.server.services;

import jakarta.mail.MessagingException;
import se.myhappyplants.shared.Email;
import se.myhappyplants.shared.Plant;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class hämta plantor från DB
 * hämta DB plantor från API (id)
 * kolla vattens intervall på plantorna och jämför med dagens datum
 * skicka mail till användaren om det är dags att vattna
 */
public class RemainderTask implements Runnable {
    private final UserPlantRepository plantRep;
    private final UserRepository usrRep;

    public RemainderTask(UserRepository usrRep, UserPlantRepository plantRep){
        this.usrRep = usrRep;
        this.plantRep = plantRep;
    }



    @Override
    public void run() {
        HashMap<Integer, ArrayList<Plant> >  plantsToNofify = new HashMap<>();

        try {
            ArrayList<Plant> plants = plantRep.getAllUserLibrary();
            for (Plant plant : plants) {
                if (plant.getDaysUntilWatering() == 0) {
                    int userId = plant.getTuserid();
                    if (!plantsToNofify.containsKey(userId)) {
                        plantsToNofify.put(userId, new ArrayList<>());
                    }
                    plantsToNofify.get(userId).add(plant);
                }
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<Integer, String> users = usrRep.getUsers();

        for (int userId : plantsToNofify.keySet()) {
            String email = users.get(userId);
            String subject = "Dags att vattna dina plantor!";
            StringBuilder body = new StringBuilder("Dina plantor som behöver vattnas är: \n");
            for(Plant plant : plantsToNofify.get(userId)) {
                body.append(plant.getNickname()).append("\n");
            }

            try {
                Email.postEmail(email, subject, body.toString());
            } catch(MessagingException e) {
                System.err.println("Failed to send email to " + email + "\nError: " + e.getMessage());
            }
        }
    }
}
