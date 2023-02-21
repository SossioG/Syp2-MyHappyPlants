package se.myhappyplants.client.controller;

import mockcodefiles.PlantLibraryMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.Plant;

import java.sql.Date;

public class BIB01F {
    /**
     * Reqid: BIB01F
     * Tillägg i bibliotek: En användare ska kunna lägga till en växt i sitt bibliotek.
     */
    private Plant validPlant = new Plant("TestPlanta", "3", new Date(System.currentTimeMillis()));

    @Test
    @DisplayName("If the database adds plant under valid conditions")
    void validAdd(){
        PlantLibraryMock mock = new PlantLibraryMock();
        Assertions.assertTrue(mock.addPlantToCurrentUserLibrary(validPlant, true));
    }

    @Test
    @DisplayName("If the database add plant despite not adding a nickname")
    void noSelectedNickname(){
        PlantLibraryMock mock = new PlantLibraryMock();
        Assertions.assertTrue(mock.addPlantToCurrentUserLibrary(validPlant, false));
    }
}