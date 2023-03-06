package se.myhappyplants.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.model.SetAvatar;

import java.io.IOException;

public class MyWishlistPaneController {
    @FXML
    private Circle imgUserAvatar;
    @FXML
    public Label lblUsername;
    @FXML
    public BorderPane myPlantsTab;
    @FXML
    private MainPaneController mainPaneController;

    @FXML public void initialize() {
        LoggedInUser loggedInUser = LoggedInUser.getInstance();
        lblUsername.setText(loggedInUser.getUser().getUsername());
        imgUserAvatar.setFill(new ImagePattern(new Image(SetAvatar.setAvatarOnLogin(loggedInUser.getUser().getEmail()))));
        // cmbSortOption.setItems(ListSorter.sortOptionsSearch());
        // showFunFact(LoggedInUser.getInstance().getUser().areFunFactsActivated());
    }

    public void setMainController(MainPaneController mainPaneController) {
        this.mainPaneController = mainPaneController;
    }

    @FXML
    private void logoutButtonPressed() throws IOException {
        mainPaneController.logoutButtonPressed();
    }

    public void sortLibrary(ActionEvent actionEvent) {
    }

    public void expandAll(ActionEvent actionEvent) {
    }

    public void collapseAll(ActionEvent actionEvent) {
    }

    public void updateAvatar() {
        imgUserAvatar.setFill(new ImagePattern(new Image(LoggedInUser.getInstance().getUser().getAvatarURL())));
    }
}
