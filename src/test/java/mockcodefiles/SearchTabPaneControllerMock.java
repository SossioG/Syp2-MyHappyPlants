package mockcodefiles;

import javafx.application.Platform;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.Plant;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchTabPaneControllerMock {

    private String searchText = "";
    private ArrayList<Plant> searchResults;
    private PlantLibraryMock plantLibraryMock = new PlantLibraryMock();

    private Plant validPlant = new Plant("TestPlanta", "3", new Date(System.currentTimeMillis()));

    public boolean searchButtonPressed()
    {
        plantLibraryMock.addPlantToCurrentUserLibrary(validPlant,true);
        searchResults = plantLibraryMock.getCurrentUserLibrary();//apiResponse.getPlantArray();
        if(searchResults.contains(searchText))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
