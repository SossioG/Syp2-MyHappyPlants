package se.myhappyplants.client.controller;

import mockcodefiles.SearchTabPaneControllerMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;
import java.sql.SQLException;

class SearchTabPaneControllerMockTest {

    SearchTabPaneControllerMock searchTabMock = new SearchTabPaneControllerMock();

    @Test
    void test() throws UnknownHostException, SQLException {
        searchTabMock.setSearchText("TestPlanta");
        Assertions.assertTrue(searchTabMock.searchButtonPressed());
    }
}