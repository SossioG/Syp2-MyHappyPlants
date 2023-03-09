package se.myhappyplants.server;

import se.myhappyplants.server.controller.ResponseController;
import se.myhappyplants.server.services.DatabaseConnection;
import se.myhappyplants.server.services.IDatabaseConnection;
import se.myhappyplants.server.services.IQueryExecutor;
import se.myhappyplants.server.services.PlantRepository;
import se.myhappyplants.server.services.QueryExecutor;
import se.myhappyplants.server.services.RemainderTask;
import se.myhappyplants.server.services.Server;
import se.myhappyplants.server.services.UserPlantRepository;
import se.myhappyplants.server.services.UserRepository;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Class that starts the server.
 * Created by: Frida Jacobson, Eric Simonson, Anton Holm, Linn Borgström, Christopher O'Driscoll.
 * Updated by: Frida Jacobsson 2021-05-21.
 */
public class StartServer {
    public static void main(String[] args) throws UnknownHostException, SQLException {
        IDatabaseConnection connectionMyHappyPlants = new DatabaseConnection("am3281");
        IDatabaseConnection connectionSpecies = new DatabaseConnection("am3281");
        IQueryExecutor databaseMyHappyPlants = new QueryExecutor(connectionMyHappyPlants);
        IQueryExecutor databaseSpecies = new QueryExecutor(connectionSpecies);
        UserRepository userRepository = new UserRepository(databaseMyHappyPlants);
        PlantRepository plantRepository = new PlantRepository(databaseSpecies);
        UserPlantRepository userPlantRepository = new UserPlantRepository(plantRepository, databaseMyHappyPlants);
        ResponseController responseController = new ResponseController(userRepository,userPlantRepository,plantRepository);
        new Server(2550, responseController);

        RemainderTask remainderTask = new RemainderTask(userRepository, userPlantRepository);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(remainderTask, 1, 1, java.util.concurrent.TimeUnit.DAYS);
    }

}