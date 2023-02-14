package se.myhappyplants.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;

class INF10F {
    /**
     * Reqid: INF10F
     * Visa Antal dagar sedan senaste vattning
     */

    @Test
    @DisplayName("Checks if watering assesment matches the waterfrequence of the plant")
    void waterfrequenceCalculations(){
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);

        // 86400000 = millis in one day * 10 = 864000000
        Plant plant = new Plant("Lisa", "1", date, 864000000L);
        Assertions.assertEquals("Needs water in 10 days", plant.getDaysUntilWater());
    }

    @Test
    @DisplayName("Checks if wateringfrequence can exceed the recommended frequency of watering (for ex cactuses)")
    void aboveRecommendedWaterfrequence(){
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);

        // 86400000 = millis in one day * 100 = 8640000000L
        Plant plant = new Plant("Lisa", "1", date, 8640000000L);
        Assertions.assertEquals("Needs water in 100 days", plant.getDaysUntilWater());
    }

    @Test
    @DisplayName("Checks if a plant that was last watered today and dont have a " +
            "specified waterfrequency dont need to get watered now")
    void noSpecifiedWaterfrequency(){
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);

        Plant plant = new Plant("Lisa", "1", date);
        Assertions.fail("You need to water this plant now!");
    }

    @Test
    @DisplayName("A need to water warning will show up if a plant needs to get watered")
    void plantWaterWarning(){
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);

        // 86400000 = millis in one day * 100 = 8640000000L
        Plant plant = new Plant("Lisa", "1", date, 10000);
        Assertions.assertEquals("You need to water this plant now!", plant.getDaysUntilWater());
    }
}