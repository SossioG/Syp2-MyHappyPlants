package se.myhappyplants.server.services;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.myhappyplants.shared.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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






    /*
    public PlantDetails getPlantDetails(Plant plant) {
        String query = "SELECT genus, scientific_name, light, water_frequency, family FROM species WHERE id = '" + plantDepricated.getPlantId() + "';";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                String genus = resultSet.getString("genus");
                String scientificName = resultSet.getString("scientific_name");
                String lightText = resultSet.getString("light");
                String waterText = resultSet.getString("water_frequency");
                String family = resultSet.getString("family");

                int light = (isNumeric(lightText)) ? Integer.parseInt(lightText) : -1;
                int water = (isNumeric(waterText)) ? Integer.parseInt(waterText) : -1;

                plantDetails = new PlantDetails(genus, scientificName, light, water, family);
            }
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.fillInStackTrace());
        }
        return plantDetails;
    }
    */


    //DB-metoder GAMLA

    /*
    public ArrayList<PlantDepricated> getResult(String plantSearch) {
        ArrayList<PlantDepricated> plantDepricatedList = new ArrayList<>();
        String query = "SELECT id, common_name, scientific_name, family, image_url FROM species WHERE scientific_name LIKE ('%" + plantSearch + "%') OR common_name LIKE ('%" + plantSearch + "%');";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                String plantId = resultSet.getString("id");
                String commonName = resultSet.getString("common_name");
                String scientificName = resultSet.getString("scientific_name");
                String familyName = resultSet.getString("family");
                String imageURL = resultSet.getString("image_url");
                plantDepricatedList.add(new PlantDepricated(plantId, commonName, scientificName, familyName, imageURL));
            }
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.fillInStackTrace());
            plantDepricatedList = null;
        }
        return plantDepricatedList;
    } */

    /*
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    } */

   /* public long getWaterFrequency(String plantId) throws IOException, InterruptedException {
        long waterFrequency = -1;
        String query = "SELECT water_frequency FROM species WHERE id = '" + plantId + "';";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                String waterText = resultSet.getString("water_frequency");
                int water = (isNumeric(waterText)) ? Integer.parseInt(waterText) : -1;
                waterFrequency = WaterCalculator.calcWaterFreqForWatering(water);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return waterFrequency;
    } */
}
