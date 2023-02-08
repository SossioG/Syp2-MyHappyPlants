package mockcodefiles;

import javafx.application.Platform;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.Plant;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchTabPaneControllerMock {

    public static void main (String[]args){
        
    }
    private String searchText = "";
    private ArrayList<Plant> searchResults;

    public boolean searchButtonPressed()
    {
            Message apiRequest = new Message(MessageType.search, searchText);
            ServerConnection connection = ServerConnection.getClientConnection();
            Message apiResponse = connection.makeRequest(apiRequest);

            if (apiResponse != null) {
                if (apiResponse.isSuccess()) {
                    searchResults = apiResponse.getPlantArray();
                    if(searchResults.size() == 0) {
                        return true;
                    }
                }
            }
            else {
                return false;
            }

        System.out.println();
        return true;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
