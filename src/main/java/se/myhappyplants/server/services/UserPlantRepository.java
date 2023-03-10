package se.myhappyplants.server.services;

import se.myhappyplants.server.PasswordsAndKeys;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final String token = PasswordsAndKeys.APIToken;

    /**
     * Constructor that creates a connection to the database.
     * @throws SQLException
     * @throws UnknownHostException
     */
    public UserPlantRepository(PlantRepository plantRepository, IQueryExecutor database) throws UnknownHostException, SQLException {
        this.plantRepository = plantRepository;
        this.database = database;

    }


    public ArrayList<Plant> getUserLibrary(User user) throws URISyntaxException, IOException, InterruptedException, SQLException {
        String query = String.format("SELECT * from plant_person where tuser_id = %s;", user.getUniqueId());
        return getPlantFromResultSet(query);
    }


    public ArrayList<Plant> getAllUserLibrary() throws URISyntaxException, IOException, InterruptedException, SQLException {
        String query = "SELECT pp.* FROM plant_person AS pp INNER JOIN tuser AS u ON pp.tuser_id = u.id WHERE notification_activated = TRUE;";
        return getPlantFromResultSet(query);
    }

    private ArrayList<Plant> getPlantFromResultSet(String query) throws SQLException, URISyntaxException, IOException, InterruptedException {
        ArrayList<Plant> plantLibrary = new ArrayList<>();

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
            plantLibrary.add(plant);
        }

        return plantLibrary;
    }

    public boolean changeNickname(User user, String nickname, String newNickname) {
        boolean nicknameChanged = false;
        String sqlSafeNickname = nickname.replace("'", "''");
        String sqlSafeNewNickname = newNickname.replace("'", "''");
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
        String query = String.format("DELETE FROM plant_person WHERE tuser_id = %d AND nickname = '%s';", user.getUniqueId(), sqlSafeNickname);
        try {
            database.executeUpdate(query);
            plantDeleted = true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
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
}
