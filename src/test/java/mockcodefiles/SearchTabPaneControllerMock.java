package mockcodefiles;

import se.myhappyplants.shared.PlantDepricated;

import java.sql.Date;
import java.util.ArrayList;

public class SearchTabPaneControllerMock {

    private String searchText = "";
    private ArrayList<PlantDepricated> searchResults;
    private PlantLibraryMock plantLibraryMock = new PlantLibraryMock();

    private PlantDepricated validPlantDepricated = new PlantDepricated("TestPlanta", "3", new Date(System.currentTimeMillis()));

    public boolean searchButtonPressed()
    {
        plantLibraryMock.addPlantToCurrentUserLibrary(validPlantDepricated,true);
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
