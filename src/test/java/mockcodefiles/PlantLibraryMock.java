package mockcodefiles;

import se.myhappyplants.shared.PlantDepricated;

import java.sql.Date;
import java.util.ArrayList;

public class PlantLibraryMock {
    private ArrayList<PlantDepricated> currentUserLibrary;

    public PlantLibraryMock(){
        currentUserLibrary = new ArrayList<>();
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);
        currentUserLibrary.add(new PlantDepricated("Bärta", "1",date));
        currentUserLibrary.add(new PlantDepricated("Märta", "2",date));
    }

    public boolean addPlantToCurrentUserLibrary(PlantDepricated plantDepricatedAdd, boolean wantToAdd) {
        String plantNickname = "commonName";

        //int answer = MessageBox.askYesNo(BoxTitle.Add, "Do you want to add a nickname for your plant?");
        if (wantToAdd == true) {
            plantNickname = "MinFavoritPlanta";//MessageBox.askForStringInput("Add a nickname", "Nickname:");
        }
        return addPlantToCurrentUserLibraryPanel(plantDepricatedAdd, plantNickname);
    }

    public boolean addPlantToCurrentUserLibraryPanel(PlantDepricated selectedPlantDepricated, String plantNickname) {
        int plantsWithThisNickname = 1;
        String uniqueNickName = plantNickname;
        for (PlantDepricated plantDepricated : currentUserLibrary) {
            if (plantDepricated.getNickname().equals(uniqueNickName)) {
                plantsWithThisNickname++;
                uniqueNickName = plantNickname + plantsWithThisNickname;
            }
        }
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);
        //String imageURL = PictureRandomizer.getRandomPictureURL();
        PlantDepricated plantDepricatedToAdd = new PlantDepricated(uniqueNickName, selectedPlantDepricated.getPlantId(), date);
        currentUserLibrary.add(plantDepricatedToAdd);
        //PopupBox.display(MessageText.sucessfullyAddPlant.toString());
        return true;
        //addPlantToDB(plantToAdd);
    }

    public ArrayList<PlantDepricated> getCurrentUserLibrary()
    {
        return currentUserLibrary;
    }
}
