package se.myhappyplants.client.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se.myhappyplants.client.controller.MyPlantsTabPaneController;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.model.PictureRandomizer;
import se.myhappyplants.shared.Plant;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple pane that displays a DBPlant's information
 * Created by: Christopher O'Driscoll
 * Updated by: Frida Jacobsson
 */
public class LibraryPlantPane extends Pane implements PlantPane {

    private MyPlantsTabPaneController myPlantsTabPaneController;
    private Plant plant;
    private ImageView image;
    private Label nickname;
    private Label lastWateredLabel;
    private ProgressBar progressBar;
    private Button infoButton;
    private Button waterButton;
    private Button plantSettings;
    private Button changeNicknameButton;
    private Button changePictureButton;
    private Button deleteButton;
    private DatePicker datePicker;
    private Button changeOKWaterButton;
    private ListView listViewMoreInfo;
    private Label daysUntilWaterlbl;
    public boolean extended;
    private boolean gotInfoOnPlant;

    public boolean extendedSettings;
    public boolean gotSettingsInfo;

    /**
     * Creates a simple pane with loading image
     * while waiting for response from database
     */
    public LibraryPlantPane() {
        File fileImg = new File("resources/images/img.png");
        Image img = new Image(fileImg.toURI().toString());
        image = new ImageView(img);
        image.setFitHeight(45.0);
        image.setFitWidth(45.0);
        image.setLayoutX(50.0);
        image.setLayoutY(14.0);

        nickname = new Label("Your plants are being loaded from the database..");
        nickname.setLayoutX(100);
        nickname.setLayoutY(25);
        nickname.setPrefWidth(300);
        nickname.setAlignment(Pos.CENTER);

        this.getChildren().addAll(image, nickname);
    }

    /**
     * Creates a pane using information from a user's
     * plant library
     *
     * @param myPlantsTabPaneController MyPlantsTabController which contains logic for elements to use
     * @param plant             plant object from user's library
     */
    public LibraryPlantPane(MyPlantsTabPaneController myPlantsTabPaneController, Plant plant) {
        this.myPlantsTabPaneController = myPlantsTabPaneController;
        this.plant = plant;
        this.image = new ImageView();
        Image img = new Image(plant.getDefaultImage().getThumbnail());
        initImages(img);
        initNicknameLabel(plant);
        initLastWateredLabel(plant);
        initProgressBar(plant);
        initWaterButton(plant);
        initPlantSetttings(plant);
        initInfoButton();
        initChangeNicknameButton(plant);
        initChangePictureButton();
        initChangeWaterOKButton(plant);
        initDatePicker();
        initDeleteButton(plant);
        initListView();
    }

    /**
     * Constructor to initialize some variables and initiate library
     * @param myPlantsTabPaneController
     */
    public LibraryPlantPane(MyPlantsTabPaneController myPlantsTabPaneController) {
        this.myPlantsTabPaneController = myPlantsTabPaneController;
        initEmptyLibraryLabel();

    }

    /**
     * Method to show the user a message that the library is empty
     */
    private void initEmptyLibraryLabel () {

        this.image = new ImageView();
        Image img = PictureRandomizer.getRandomPicture();
        initImages(img);
        Label lblEmptyInfo = new Label("Your library is currently empty \nClick here to search for plants to add    --------->");
        lblEmptyInfo.setLayoutX(150.0);
        lblEmptyInfo.setLayoutY(28.0);
        Button btnSearchPlants = new Button("Search for plants");
        btnSearchPlants.setOnAction(action -> myPlantsTabPaneController.getMainPaneController().changeToSearchTab());
        btnSearchPlants.setLayoutX(500.0);
        btnSearchPlants.setLayoutY(40.0);
        this.getChildren().addAll(image, lblEmptyInfo, btnSearchPlants);
    }


    public void updateImage() {
        Image img = new Image(plant.getDefaultImage().getThumbnail());
        image.setImage(img);
    }

    /**
     * Method to initiate the image View
     * @param img
     */
    private void initImages(Image img) {

        image.setFitHeight(70.0);
        image.setFitWidth(70.0);
        image.setLayoutX(50.0);
        image.setLayoutY(5.0);
        image.setPickOnBounds(true);
        image.setPreserveRatio(true);
        image.setImage(img);
    }

    /**
     * Method to initiate the nickname label
     * @param plant
     */
    private void initNicknameLabel(Plant plant) {
        nickname = new Label(plant.getNickname());
        nickname.setLayoutX(0);
        nickname.setLayoutY(70);
        nickname.setPrefWidth(145);
        nickname.setAlignment(Pos.CENTER);
    }

    /**
     * Method to initiate the last watered label
     * @param plant
     */
    private void initLastWateredLabel(Plant plant) {
        this.lastWateredLabel = new Label();
        lastWateredLabel.setLayoutY(226);
        lastWateredLabel.setLayoutX(10);
        LocalDate lastWateredDate = plant.getLastWatered();
        lastWateredLabel.setText("Last watered: " + lastWateredDate.toString());
    }

    /**
     * Method to initiate the progressbar
     * @param plant
     */
    private void initProgressBar(Plant plant) {
        daysUntilWaterlbl = new Label();
        String daysUntilWaterText = plant.getDaysUntilWater();

        daysUntilWaterlbl.setText(daysUntilWaterText);
        daysUntilWaterlbl.setLayoutX(350.0);
        daysUntilWaterlbl.setLayoutY(5.0);

        this.progressBar = new ProgressBar(plant.getProgress());
        setColorProgressBar(plant.getProgress());
        progressBar.setLayoutX(150.0);
        progressBar.setLayoutY(28.0);
        progressBar.setPrefHeight(18.0);
        progressBar.setPrefWidth(575.0);
    }

    /**
     * Mehtosd to get the progressbar
     * @return
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }


    private void initPlantSetttings(Plant plant) {
        this.plantSettings = new Button("Settings");
        plantSettings.setLayoutX(450.0);
        plantSettings.setLayoutY(55.0);
        plantSettings.setMnemonicParsing(false);
        plantSettings.setOnAction(action -> {
            pressPlantSettingsButton();
        });
    }

    public void pressPlantSettingsButton() {
        plantSettings.setDisable(true);
        if (!extendedSettings) {
            expandPlantSettings();
        }
        else {
            collapsePlantSettings();
        }
    }

    public boolean hasExtendedSettings(){
        return extendedSettings;
    }

    /**
     * Method to initiate the water button
     * @param plant
     */
    private void initWaterButton(Plant plant) {
        this.waterButton = new Button("Water");
        waterButton.setLayoutX(300.0);
        waterButton.setLayoutY(55.0);
        waterButton.setMnemonicParsing(false);
        LocalDate date = LocalDate.now();
        waterButton.setOnAction(action -> {
            progressBar.setProgress(100);
            setColorProgressBar(100);
            myPlantsTabPaneController.changeLastWateredInDB(plant, date);
            setColorProgressBar(100);
        });
    }
    /**
     * Method to initiate the info button
     */
    private void initInfoButton() {
        this.infoButton = new Button("Show info");
        infoButton.setLayoutX(150.0);
        infoButton.setLayoutY(55.0);
        infoButton.setMnemonicParsing(false);
        infoButton.setOnAction(onPress -> {
            pressInfoButton();
        });
    }

    /**
     * Method to set off what happens when a user presses the info button.
     */
    public void pressInfoButton() {
        infoButton.setDisable(true);
        if (!extended) {
            if (!gotInfoOnPlant) {
                //PlantDetails plantDetails = myPlantsTabPaneController.getPlantDetails(plant);
                //long waterInMilli = WaterCalculator.calcWaterFreqForWatering(plantDetails.getWaterFrequency());
                //String waterText = WaterTextFormatter.getWaterString(waterInMilli);
                //String lightText = LightTextFormatter.getLightTextString(plantDetails.getLight());


                ObservableList<String> plantInfo = FXCollections.observableArrayList();
                //long difference = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()- plantDepricated.getLastWatered().getTime());
                //String days = difference > 0 ? String.valueOf(difference) + " Days" : "Today";
                plantInfo.add("Common name: " + plant.getCommonName());
                //ändra här senare
         //       plantInfo.add("Scientific name: " + Arrays.stream(new String[]{plant.getScientificName()}).findFirst());
                plantInfo.add("Scientific name: " + Arrays.stream(new String[]{plant.getScientificName()})
                        .findFirst()
                        .orElse("No Scientific information available"));

                // plantInfo.add("Sunlight: " + Arrays.stream(plant.getSunlight()).findFirst());
                plantInfo.add("Sunlight: " + Optional.ofNullable(plant.getSunlight())
                        .map(str -> String.join(", ", str))
                        .orElse("No sunlight information available"));

                plantInfo.add("Water: " + plant.getWatering());
                plantInfo.add("Last watered: " + plant.getLastWatered().toString());
                listViewMoreInfo.setItems(plantInfo);
            }
            expand();
        }
        else {
            collapse();
        }
    }

    public ListView getListViewMoreInfo(){
        return listViewMoreInfo;
    }

    public boolean isExtended() {
        return extended;
    }

    /**
     * Den här metoden beräknar (ungefär) hur många dagar sedan en planta var vattnad.
     * @return Days passed after last watering time.
     */
    private String calcLastTimeWatered() {
        LocalDate localDate = java.time.LocalDate.now();
        String lclTime = localDate.toString();
        String lastWatered = plant.getLastWatered().toString();
        long calcAproxDay = 0;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(lclTime);
            Date date2 = format.parse(lastWatered);
            long diffDay = date1.getDate() - date2.getDate();
            long diffMonth = date1.getMonth() - date2.getMonth();
            long diffYear = date1.getYear() - date2.getYear();
            calcAproxDay = diffDay + (diffMonth * 30);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.toString(calcAproxDay);
    }
    /**
     * Method to initiate the change nickname-button
     * @param plant to change the nickname on
     */
    private void initChangeNicknameButton(Plant plant) {
        this.changeNicknameButton = new Button("Change nickname");
        changeNicknameButton.setLayoutX(333.0);
        changeNicknameButton.setLayoutY(110.0);
        changeNicknameButton.setMnemonicParsing(false);
        changeNicknameButton.setOnAction(onPress -> {
            changeNickname(plant);
        });
    }
    /**
     * Method to initiate the change last watered-button
     * @param plant to change the nickname on
     */
    private void initChangeWaterOKButton(Plant plant) {
        this.changeOKWaterButton = new Button("Change");
        changeOKWaterButton.setLayoutX(215.0);
        changeOKWaterButton.setLayoutY(250.0);
        changeOKWaterButton.setMnemonicParsing(false);
        changeOKWaterButton.setOnAction(onPress -> {
            changeDate(plant);
            datePicker.setPromptText("Change last watered");
        });
    }
    /**
     * Method to initiate the change picture-button
     */
    private void initChangePictureButton() {
        this.changePictureButton = new Button("Change picture");
        changePictureButton.setLayoutX(488.0);
        changePictureButton.setLayoutY(110.0);
        changePictureButton.setMnemonicParsing(false);
        changePictureButton.setOnAction(action ->
                myPlantsTabPaneController.setNewPlantPicture(this));
    }

    /**
     * Method to initiate the "date picker"
     */
    private void initDatePicker() {
        this.datePicker = new DatePicker();
        datePicker.setLayoutX(10.0);
        datePicker.setLayoutY(250.0);
        datePicker.setEditable(false);
        datePicker.setPrefWidth(200);
        datePicker.setPromptText("Change last watered");
    }

    /**
     * Method to initialize the delete button
     * @param plant he plant to delete
     */
    private void initDeleteButton(Plant plant) {
        this.deleteButton = new Button("Delete plant");
        deleteButton.setLayoutX(625.0);
        deleteButton.setLayoutY(110.0);
        deleteButton.setMnemonicParsing(false);
        deleteButton.setOnAction(onPress -> {
            removePlant(plant);
        });
    }

    /**
     * Method to initialize and set the listView with the extended information about the plants
     */
    private void initListView() {
        listViewMoreInfo = new ListView();
        listViewMoreInfo.setLayoutX(this.getWidth() + 10.0);
        listViewMoreInfo.setLayoutY(this.getHeight() + 100.0);
        listViewMoreInfo.setPrefWidth(725.0);
        listViewMoreInfo.setPrefHeight(140.0);
        /*PlantDetails plantDetails = myPlantsTabPaneController.getPlantDetailsMyPlantsTab(plantDepricated);
        long waterInMilli = WaterCalculator.calcWaterFreqForWatering(plantDetails.getWaterFrequency());
        String waterText = WaterTextFormatter.getWaterString(waterInMilli);
        String lightText = LightTextFormatter.getLightTextString(plantDetails.getLight()); */
        ObservableList<String> plantInfo = FXCollections.observableArrayList();
        plantInfo.add("Common name: " + plant.getCommonName());

      //  plantInfo.add("Scientific name: " + Arrays.stream(new String[]{plant.getScientificName()}).findFirst());
        plantInfo.add("Scientific name: " + Arrays.stream(new String[]{plant.getScientificName()})
                .findFirst()
                .orElse("No scientific name available"));

       // plantInfo.add("Sunlight: " + Arrays.stream(new String[]{plant.getSunlight()}).findFirst() );
        //har ändrat här
        plantInfo.add("Sunlight: " + Optional.ofNullable(plant.getSunlight())
                .map(str -> String.join(", ", str))
                .orElse("No sunlight information available"));

        plantInfo.add("Water: " + plant.getWatering());
        plantInfo.add("Last watered: " + plant.getLastWatered().toString());
        listViewMoreInfo.setItems(plantInfo);

        this.setPrefHeight(92.0);
        this.getChildren().addAll(image, nickname, daysUntilWaterlbl, progressBar, plantSettings, waterButton, infoButton);
        listViewMoreInfo.setItems(plantInfo);

    }

    public void expandPlantSettings() {
        AtomicReference<Double> height = new AtomicReference<>(this.getHeight());
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(8.5), event -> this.setPrefHeight(height.updateAndGet(v -> (double) (v + 6.25))))
        );
        timeline.setCycleCount(32);
        timeline.play();
        timeline.setOnFinished(action -> {
            plantSettings.setDisable(false);
            this.setPrefHeight(152.0);
            this.getChildren().addAll(changeNicknameButton, changePictureButton, deleteButton);
        });
        extendedSettings = true;

        if(extended) {
            collapse();
        }
    }

    public void collapsePlantSettings() {
        AtomicReference<Double> height = new AtomicReference<>(this.getHeight());
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(7.5), event -> this.setPrefHeight(height.updateAndGet(v -> (double) (v - 6.25))))
        );
        timeline.setCycleCount(32);
        timeline.play();
        this.getChildren().removeAll(changeNicknameButton, changePictureButton, deleteButton);
        timeline.setOnFinished(action -> {
            plantSettings.setDisable(false);
            this.setPrefHeight(92.0);
        });
        extendedSettings = false;
    }


    /**
     * Method for expanding tab with "more information"-buttons.
     */
    public void expand() {
        AtomicReference<Double> height = new AtomicReference<>(this.getHeight());
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(8.5), event -> this.setPrefHeight(height.updateAndGet(v -> (double) (v + 6.25))))
        );
        timeline.setCycleCount(32);
        timeline.play();
        timeline.setOnFinished(action -> {
            infoButton.setDisable(false);
            this.setPrefHeight(292.0);
            this.getChildren().addAll(listViewMoreInfo, datePicker, changeOKWaterButton);
        });
        extended = true;

        if(extendedSettings) {
            collapsePlantSettings();
        }

    }

    /**
     * Method for hiding tab with "more information"-buttons.
     */
    public void collapse() {
        AtomicReference<Double> height = new AtomicReference<>(this.getHeight());
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(7.5), event -> this.setPrefHeight(height.updateAndGet(v -> (double) (v - 6.25))))
        );
        timeline.setCycleCount(32);
        timeline.play();
        this.getChildren().removeAll(listViewMoreInfo, datePicker, changeOKWaterButton, lastWateredLabel);
        timeline.setOnFinished(action -> {
            infoButton.setDisable(false);
            this.setPrefHeight(92.0);
        });
        extended = false;
    }

    /**
     * Changes the colour of the progress bar
     *
     * @param progress How full the progress bar is(0-1.0)
     */
    public void setColorProgressBar(double progress) {
        if (progress < 0.15) {
            progressBar.setStyle("-fx-accent: #BE4052");
        }
        else {
            progressBar.setStyle("-fx-accent: 2D88AA");
        }
    }

    /**
     * Shows a confirmation box when called,
     * to double check the user really
     * wants to remove the plant
     *
     * @param plant selected plant
     */
    private void removePlant(Plant plant) {
        int answer = MessageBox.askYesNo(BoxTitle.Delete, "The deleted plant can't be restored. Are you sure?");
        if (answer == 1) {
            myPlantsTabPaneController.removePlantFromDB(plant);
        }
    }

    /**
     * Method to change the nickname of a plant
     * @param plant the selected plant
     */
    private void changeNickname(Plant plant) {
        boolean changeSuccess;
        String newNickname = MessageBox.askForStringInput("Change nickname", "New nickname:");

        if (!newNickname.equals("")) {
            changeSuccess = myPlantsTabPaneController.changeNicknameInDB(plant, newNickname);
            if (changeSuccess) {
                nickname.setText(newNickname);
            }
        }
    }

    /**
     * Method to change the date of the last watered date
     * @param plant the selected plant
     */
    public void changeDate(Plant plant) {
        LocalDate date = datePicker.getValue();

        plant.setLastWatered(date);
        String daysUntilWaterText = plant.getDaysUntilWater();
        daysUntilWaterlbl.setText(daysUntilWaterText);
        progressBar.setProgress(plant.getProgress());
        setColorProgressBar(plant.getProgress());
        myPlantsTabPaneController.changeLastWateredInDB(plant, date);
    }

    public void setDatePicker(LocalDate date){
        datePicker.setValue(date);
    }

    /**
     * Getter method to get the plant
     * @return
     */
    @Override
    public Plant getPlant() {
        return plant;
    }

    public void pressChangeNicknameButton(Plant plant) {
        if(hasExtendedSettings()){
            changeNickname(plant);
        }
    }

    public void pressDeletePlantButton(Plant plant) {
        if(hasExtendedSettings()){
            removePlant(plant);
        }
    }

    public Label getNickname() {
        return nickname;
    }
}
