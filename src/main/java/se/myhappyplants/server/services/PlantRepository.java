package se.myhappyplants.server.services;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.myhappyplants.server.PasswordsAndKeys;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.Plants;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Class responsible for calling the database about plants.
 * Created by: Frida Jacobsson 2021-03-30
 * Updated by: Christopher O'Driscoll
 */
public class PlantRepository {

    static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String token = PasswordsAndKeys.APIToken;
    private IQueryExecutor database;

    public PlantRepository(IQueryExecutor database) {
        this.database = database;
    }

    public ArrayList<Plant> getPlantlist(String searchtext, int pageNumber) throws URISyntaxException, IOException, InterruptedException {
        String uritext;
        if(!searchtext.isEmpty()){
            uritext = "https://perenual.com/api/species-list?key=" + token + "&q=" + searchtext + "&page=" + pageNumber;
        } else{
            uritext = "https://perenual.com/api/species-list?page=1&key=" + token + "&page=" + pageNumber;
        }
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(uritext))
                .header("Content-Type","application/json")
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(getResponse.body());
        ArrayList<Plant> plants = mapper.readValue(getResponse.body(), Plants.class).getPlants();
        return plants;
    }
}
