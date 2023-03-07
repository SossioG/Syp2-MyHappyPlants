package se.myhappyplants.server.services;

import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static se.myhappyplants.server.services.PlantRepository.mapper;

/**
 * Class responsible for calling the database about a users library.
 * Created by: Linn Borgström
 * Updated by: Frida Jacobsson 2021-05-21
 */
public class UserPlantRepository {

    private PlantRepository plantRepository;
    private IQueryExecutor database;
    //Min egen token
    private final String token = "sk-824P6401d8db6d09d154";

    /**
     * Constructor that creates a connection to the database.
     * @throws SQLException
     * @throws UnknownHostException
     */
    public UserPlantRepository(PlantRepository plantRepository, IQueryExecutor database) throws UnknownHostException, SQLException {
        this.plantRepository = plantRepository;
        this.database = database;

    }


    public ArrayList<Plant> getUserLibrary(User user) throws URISyntaxException, IOException, InterruptedException {
        ArrayList<Plant> userPlantLibrary = new ArrayList<>();
        String query = String.format("SELECT plant_id, nickname, last_watered from plant_person where tuser_id = %s;", user.getUniqueId());
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()){
                int plantid = resultSet.getInt("plant_id");
                String nickname = resultSet.getString("nickname");
                Date lastWatered = resultSet.getDate("last_watered");

                HttpRequest getRequest = HttpRequest.newBuilder()
                        .uri(new URI("https://perenual.com/api/species/details/" + plantid + "?key=" + token))
                        .header("Content-Type","application/json")
                        .build();

                HttpClient httpClient = HttpClient.newHttpClient();
                HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
                Plant plant = mapper.readValue(getResponse.body(), Plant.class);
                plant.setNickname(nickname);
                plant.setLastWatered(lastWatered.toLocalDate());


                userPlantLibrary.add(plant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(userPlantLibrary);
        return userPlantLibrary;
    }


    public ArrayList<Plant> getAllUserLibrary() throws URISyntaxException, IOException, InterruptedException {
        ArrayList<Plant> userPlantLibrary = new ArrayList<>();
        String query = "SELECT plant_id, nickname, last_watered from plant_person;";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()){
                int plantid = resultSet.getInt("plant_id");
                String nickname = resultSet.getString("nickname");
                Date lastWatered = resultSet.getDate("last_watered");

                HttpRequest getRequest = HttpRequest.newBuilder()
                        .uri(new URI("https://perenual.com/api/species/details/" + plantid + "?key=" + token))
                        .header("Content-Type","application/json")
                        .build();

                HttpClient httpClient = HttpClient.newHttpClient();
                HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
                Plant plant = mapper.readValue(getResponse.body(), Plant.class);
                plant.setNickname(nickname);
                plant.setLastWatered(lastWatered.toLocalDate());
                plant.setTuserid(resultSet.getInt("tuser_id"));


                userPlantLibrary.add(plant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(userPlantLibrary);
        return userPlantLibrary;
    }

    public boolean changeNickname(User user, String nickname, String newNickname) {
        boolean nicknameChanged = false;
        String sqlSafeNickname = nickname.replace("'", "''");
        String sqlSafeNewNickname = newNickname.replace("'", "''");
//        String query = "UPDATE [Plant] SET nickname = '" + sqlSafeNewNickname + "' WHERE user_id = " + user.getUniqueId() + " AND nickname = '" + sqlSafeNickname + "';";
        String query = String.format("UPDATE plant_person SET nickname = '%s' WHERE tuser_id = %d AND nickname = '%s';", sqlSafeNewNickname, user.getUniqueId(), sqlSafeNickname);
        try {
            database.executeUpdate(query);
            nicknameChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return nicknameChanged;
    }
/**ALTER TABLE plant ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;
 */
    /**
     * Method that makes a query to delete a specific plant from table plant_person
     * @param user     the user that owns the plant
     * @param nickname nickname of the plant
     * @return boolean result depending on the result, false if exception
     */
    public boolean deletePlant(User user, String nickname) {
        boolean plantDeleted = false;
        String sqlSafeNickname = nickname.replace("'", "''");
//        String query = "DELETE FROM [plant] WHERE user_id =" + user.getUniqueId() + "AND nickname = '" + sqlSafeNickname + "';";
        String query = String.format("DELETE FROM plant_person WHERE tuser_id = %d AND nickname = '%s';" + user.getUniqueId(), sqlSafeNickname);
        try {
            database.executeUpdate(query);
            plantDeleted = true;
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException);
        }
        return plantDeleted;
    }

    /**
     * Method to save a new plant in database
     * Author: Frida Jacobsson
     * Updated Frida Jacobsson 2021-04-29
     * @param plant an instance of a newly created plant by user
     * @return a boolean value, true if the plant was stored successfully
     */

    public boolean savePlant(User user, Plant plant) {
        boolean success = false;
        String sqlSafeNickname = plant.getNickname().replace("'", "''");
//      String query = "INSERT INTO Plant (user_id, nickname, plant_id, last_watered, image_url) values (" + user.getUniqueId() + ", '" + sqlSafeNickname + "', '" + plant.getPlantId() + "', '" + plant.getLastWatered() + "', '" + plant.getImageURL() + "');";
        String query = String.format("INSERT INTO plant_person (tuser_id, plant_id, nickname, last_watered) values (%s, %s, '%s', '%s');", user.getUniqueId(), plant.getId(), sqlSafeNickname, plant.getLastWatered());
        System.out.println(query);
        try {
            database.executeUpdate(query);
            success = true;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return success;
    }

    /**
     * Method that makes a query to change the last watered date of a specific plant in table Plant
     * @param user     the user that owns the plant
     * @param nickname nickname of the plant
     * @param date     new data to change to
     * @return boolean result depending on the result, false if exception
     */
    public boolean changeLastWatered(User user, String nickname, Date date) {
        boolean dateChanged = false;
        String sqlSafeNickname = nickname.replace("'", "''");
//        String query = "UPDATE [Plant] SET last_watered = '" + date + "' WHERE user_id = " + user.getUniqueId() + " AND nickname = '" + sqlSafeNickname + "';";
        String query = String.format("UPDATE plant_person SET last_watered = '%s' WHERE tuser_id = %d AND nickname = '%s';", date, user.getUniqueId(), sqlSafeNickname);
        try {
            database.executeUpdate(query);
            dateChanged = true;
            System.out.println("Updated last watered with date: " + date.toString() + " plant: " + nickname);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return dateChanged;
    }

    /**
     * Method that makes a qyery to change all of plants watering date
     * @param user
     * @return
     */
    public boolean changeAllToWatered(User user) {
        boolean dateChanged = false;
        LocalDate date = java.time.LocalDate.now();
//        String query = "UPDATE [Plant] SET last_watered = '" + date + "' WHERE user_id = " + user.getUniqueId() + ";";
        String query = String.format("UPDATE plant_person SET last_watered = '%s' WHERE tuser_id = %d;", date, user.getUniqueId());
        try {
            database.executeUpdate(query);
            dateChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return dateChanged;
    }



    //GAMLA METODER obehandlade metoder



    /**
     * Method that returns all the plants connected to the logged in user.
     * Author: Linn Borgström,
     * Updated by: Frida Jacobsson
     * @return an arraylist if plants stored in the database
     */
    /*
    public ArrayList<PlantDepricated> getUserLibrary(User user) {
        ArrayList<PlantDepricated> plantDepricatedList = new ArrayList<PlantDepricated>();
//      String query = "SELECT nickname, plant_id, last_watered, image_url FROM [Plant] WHERE user_id =" + user.getUniqueId() + ";";
        String query = String.format("SELECT nickname, plant_id, last_watered, image_url FROM plant WHERE user_id = %s;", user.getUniqueId());
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                String nickname = resultSet.getString("nickname");
                String plantId = resultSet.getString("plant_id");
                Date lastWatered = resultSet.getDate("last_watered");
                String imageURL = resultSet.getString("image_url");
                long waterFrequency = plantRepository.getWaterFrequency(plantId);
                plantDepricatedList.add(new PlantDepricated(nickname, plantId, lastWatered, waterFrequency, imageURL));
            }
        }
        catch (SQLException | IOException | InterruptedException exception) {
            System.out.println(exception.fillInStackTrace());
        }
        return plantDepricatedList;
    }
    */



    /**
     * Method that returns one specific plant based on nickname.
     * @param
     * @return an instance of a specific plant from the database, null if no plant with the specific nickname exists
     */
    /*
    public PlantDepricated getPlant(User user, String nickname) {
        PlantDepricated plantDepricated = null;
        String sqlSafeNickname = nickname.replace("'", "''");
        String query = String.format("SELECT nickname, plant_id, last_watered, image_url FROM plant WHERE user_id = %d AND nickname = '%s';", user.getUniqueId(), sqlSafeNickname);
        try {
            ResultSet resultSet = database.executeQuery(query);
            String plantId = resultSet.getString("plant_id");
            Date lastWatered = resultSet.getDate("last_watered");
            String imageURL = resultSet.getString("image_url");
            long waterFrequency = plantRepository.getWaterFrequency(plantId);
            plantDepricated = new PlantDepricated(nickname, plantId, lastWatered, waterFrequency, imageURL);
        }
        catch (SQLException | IOException | InterruptedException sqlException) {
            System.out.println(sqlException.fillInStackTrace());
        }
        return plantDepricated;
    }
*/


    /*
    public boolean changePlantPicture(User user, PlantDepricated plantDepricated) {
        boolean pictureChanged = false;
        String nickname = plantDepricated.getNickname();
        String sqlSafeNickname = nickname.replace("'", "''");
//        String query = "UPDATE [Plant] SET image_url = '" + plant.getImageURL() + "' WHERE user_id = " + user.getUniqueId() + " AND nickname = '" + sqlSafeNickname + "';";
        String query = String.format("UPDATE plant SET image_url = '%s' WHERE user_id = %d AND nickname = '%s';", plantDepricated.getImageURL(), user.getUniqueId(), sqlSafeNickname);
        try {
            database.executeUpdate(query);
            pictureChanged = true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return pictureChanged;
    } */
}