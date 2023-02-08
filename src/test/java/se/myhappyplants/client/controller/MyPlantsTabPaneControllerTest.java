package se.myhappyplants.client.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.PlantDetails;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MyPlantsTabPaneControllerTest
{

    MyPlantsTabPaneController tbc = new MyPlantsTabPaneController();
    long millis=System.currentTimeMillis();
    java.sql.Date date=new java.sql.Date(millis);
    Plant testPlant = new Plant("testplant","1",date);

    @Test
    void addplantTest()
    {
        //Assertions.assertEquals(false,tbc.addPlantToCurrentUserLibrary(testPlant, testPlant.getNickname()));
    }
}