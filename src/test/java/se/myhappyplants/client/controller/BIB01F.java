package se.myhappyplants.client.controller;

import mockcodefiles.PlantLibraryMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.PlantDepricated;

import java.sql.Date;

class BIB01F {
    /**
     * Reqid: BIB01F
     * Tillägg i bibliotek: En användare ska kunna lägga till en växt i sitt bibliotek.
     */
    private PlantDepricated validPlantDepricated = new PlantDepricated("TestPlanta", "3", new Date(System.currentTimeMillis()));

    @Test
    @DisplayName("If the database adds plant under valid conditions")
    void validAdd(){
        PlantLibraryMock mock = new PlantLibraryMock();
        Assertions.assertTrue(mock.addPlantToCurrentUserLibrary(validPlantDepricated, true));
    }

    @Test
    @DisplayName("If the database add plant despite not adding a nickname")
    void noSelectedNickname(){
        PlantLibraryMock mock = new PlantLibraryMock();
        Assertions.assertTrue(mock.addPlantToCurrentUserLibrary(validPlantDepricated, false));
    }
}