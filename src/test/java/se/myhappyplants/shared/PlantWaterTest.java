package se.myhappyplants.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class PlantWaterTest {

    @Test
    void checkReturn(){
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);
        Plant plant = new Plant("Lisa", "1", date, 2332800);
        Assertions.assertEquals("", plant.getDaysUntilWater());
    }

}