package se.myhappyplants.client.controller;

import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.PlantDepricated;

class MyPlantsTabPaneControllerTest
{

    MyPlantsTabPaneController tbc = new MyPlantsTabPaneController();
    long millis=System.currentTimeMillis();
    java.sql.Date date=new java.sql.Date(millis);
    PlantDepricated testPlantDepricated = new PlantDepricated("testplant","1",date);

    @Test
    void addplantTest()
    {
        //Assertions.assertEquals(false,tbc.addPlantToCurrentUserLibrary(testPlant, testPlant.getNickname()));
    }
}