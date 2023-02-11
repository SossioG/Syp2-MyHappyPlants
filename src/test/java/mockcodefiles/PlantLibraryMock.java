package mockcodefiles;

import se.myhappyplants.shared.Plant;

import java.sql.Date;
import java.util.ArrayList;

public class PlantLibraryMock {
    private ArrayList<Plant> currentUserLibrary;

    public PlantLibraryMock(){
        currentUserLibrary = new ArrayList<>();
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);
        currentUserLibrary.add(new Plant("Bärta", "1",date));
        currentUserLibrary.add(new Plant("Märta", "2",date));
    }

    public boolean addPlantToCurrentUserLibrary(Plant plantAdd, boolean wantToAdd) {
        String plantNickname = "commonName";

        //int answer = MessageBox.askYesNo(BoxTitle.Add, "Do you want to add a nickname for your plant?");
        if (wantToAdd == true) {
            plantNickname = "MinFavoritPlanta";//MessageBox.askForStringInput("Add a nickname", "Nickname:");
        }
        return addPlantToCurrentUserLibraryPanel(plantAdd, plantNickname);
    }

    public boolean addPlantToCurrentUserLibraryPanel(Plant selectedPlant, String plantNickname) {
        int plantsWithThisNickname = 1;
        String uniqueNickName = plantNickname;
        for (Plant plant : currentUserLibrary) {
            if (plant.getNickname().equals(uniqueNickName)) {
                plantsWithThisNickname++;
                uniqueNickName = plantNickname + plantsWithThisNickname;
            }
        }
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);
        //String imageURL = PictureRandomizer.getRandomPictureURL();
        Plant plantToAdd = new Plant(uniqueNickName, selectedPlant.getPlantId(), date);
        currentUserLibrary.add(plantToAdd);
        //PopupBox.display(MessageText.sucessfullyAddPlant.toString());
        return true;
        //addPlantToDB(plantToAdd);
    }

    public ArrayList<Plant> getCurrentUserLibrary()
    {
        return currentUserLibrary;
    }
}
