package se.myhappyplants.client.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.myhappyplants.server.StartServer;
import se.myhappyplants.server.controller.ResponseController;
import se.myhappyplants.server.services.*;

import java.net.UnknownHostException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SearchTabPaneControllerMockTest {

    SearchTabPaneControllerMock searchTabMock = new SearchTabPaneControllerMock();
    //start Server for test to work

    //todo kolla om strängen från DB är rätt.
    @Test
    void test() throws UnknownHostException, SQLException {
        searchTabMock.setSearchText("");
        startServer();
        Assertions.assertEquals(true,searchTabMock.searchButtonPressed());
    }

    public static void startServer() throws UnknownHostException, SQLException {
        IDatabaseConnection connectionMyHappyPlants = new DatabaseConnection("am3281");
        IDatabaseConnection connectionSpecies = new DatabaseConnection("am3281");
        IQueryExecutor databaseMyHappyPlants = new QueryExecutor(connectionMyHappyPlants);
        IQueryExecutor databaseSpecies = new QueryExecutor(connectionSpecies);
        UserRepository userRepository = new UserRepository(databaseMyHappyPlants);
        PlantRepository plantRepository = new PlantRepository(databaseSpecies);
        UserPlantRepository userPlantRepository = new UserPlantRepository(plantRepository, databaseMyHappyPlants);
        ResponseController responseController = new ResponseController(userRepository,userPlantRepository,plantRepository);
        new Server(2550, responseController);
    }
}