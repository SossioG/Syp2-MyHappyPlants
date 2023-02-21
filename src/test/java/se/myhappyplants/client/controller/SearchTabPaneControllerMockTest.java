package se.myhappyplants.client.controller;

import mockcodefiles.SearchTabPaneControllerMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.net.UnknownHostException;
import java.sql.SQLException;

/**
 * ReqID: SÖK01F
 * En användare ska kunna söka på olika växter.
 */

public class SearchTabPaneControllerMockTest {

    SearchTabPaneControllerMock searchTabMock = new SearchTabPaneControllerMock();

    @Test
    void testSearchForPlant() throws UnknownHostException, SQLException {
        searchTabMock.setSearchText("TestPlanta");
        Assertions.assertTrue(searchTabMock.searchButtonPressed());
    }
}