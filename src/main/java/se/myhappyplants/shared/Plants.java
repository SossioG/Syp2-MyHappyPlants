package se.myhappyplants.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Plants{
    @JsonProperty("data")
    private ArrayList<Plant> plants;

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public void setPlants(ArrayList<Plant> plants) {
        this.plants = plants;
    }
}
