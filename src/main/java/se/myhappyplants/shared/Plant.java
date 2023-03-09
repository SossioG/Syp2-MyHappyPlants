package se.myhappyplants.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.DAYS;

public class Plant implements Serializable {
    private int id;
    @JsonProperty("common_name")
    private String commonName;
    @JsonProperty("scientific_name")
    private String[] scientificName;
    private String[] sunlight;
    private String watering;
    @JsonProperty("default_image")
    private DefaultImage defaultImage;
    private String nickname;
    @JsonProperty("last_watered")
    private LocalDate lastWatered;

    public Plant(){
    }

    public Plant(String uniqueNickName, int id, LocalDate date, String imageURL) {
        this.nickname = uniqueNickName;
        this.id = id;
        this.lastWatered = date;
        DefaultImage defaultImage = new DefaultImage();
        defaultImage.setThumbnail(imageURL);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String common_name) {
        this.commonName = common_name;
    }

    public String[] getScientificName() {
        return scientificName;
    }

    public void setScientificName(String[] scientific_name) {
        this.scientificName = scientific_name;
    }

    public String[] getSunlight() {
        return sunlight;
    }

    public void setSunlight(String[] sunlight) {
        this.sunlight = sunlight;
    }

    public DefaultImage getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(DefaultImage defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(LocalDate last_watered) {
        this.lastWatered = last_watered;
    }

    public long getMillisFrequency(){
        long amountOfDaysMillis = 0;
        if (watering == "Frequent"){
            amountOfDaysMillis = 86400000;
        } else if (watering == "Average"){
            amountOfDaysMillis = 259200000;
        } else if (watering == "Minimum"){
            amountOfDaysMillis = 864000000;
        } else if (watering == "None"){
            amountOfDaysMillis = 803520000; // No watering needed
        }
        return amountOfDaysMillis;
    }


    public String getDaysUntilWater() {

        int days = getDaysUntilWatering();
        if(days == 0)
            return "Din planta behöver vattnas!";
        else
            return "Antal dagar till vattning: " + days;
    }

    public double getProgress() {

        int daysUntilWater = getDaysUntilWatering();
        double progress = 1.0;

        switch (watering){
            case "Frequent": //Var 3:de dag.
                progress = daysUntilWater / (double) 3;
                if(daysUntilWater < 0){
                    progress = 0.0;
                }
                break;

            case "Average": //Var 7:de dag.
                progress = daysUntilWater / (double) 7;
                if(daysUntilWater < 0){
                    progress = 0.0;
                }

                break;

            case "Minimum": //Var 21:de dag.
                progress = daysUntilWater / (double) 21;
                if(daysUntilWater < 0){
                    progress = 0.0;
                }

                break;

            case "None": //Var 93:de dag.
                progress = daysUntilWater / (double) 93;
                if(daysUntilWater < 0){
                    progress = 0.0;
                }

                break;

        }

        return progress;
    }

    private int getDaysUntilWatering(){

        LocalDate today = LocalDate.now();
        LocalDate needsWater;
        int daysTillWater = 0;

        switch (watering) {
            case "Frequent": //Var 3:de dag.
                needsWater = lastWatered.plusDays(3);
                daysTillWater = Long.valueOf(DAYS.between(today, needsWater)).intValue();
                if(daysTillWater <= 0)
                    daysTillWater = 0;
                break;

            case "Average": //Var 7:de dag.
                needsWater = lastWatered.plusDays(7);
                daysTillWater = Long.valueOf(DAYS.between(today, needsWater)).intValue();
                if(daysTillWater <= 0)
                    daysTillWater = 0;
                break;

            case "Minimum": //Var 21:de dag.
                needsWater = lastWatered.plusDays(21);
                daysTillWater = Long.valueOf(DAYS.between(today, needsWater)).intValue();
                if(daysTillWater <= 0)
                    daysTillWater = 0;
                break;

            case "None": //Var 93:de dag.
                needsWater = lastWatered.plusDays(93);
                daysTillWater = Long.valueOf(DAYS.between(today, needsWater)).intValue();
                if(daysTillWater <= 0)
                    daysTillWater = 0;
                break;

        }
        return daysTillWater;

    }

    @Override
    public String toString() {
        return "Plant{\n" + "id=" + id + ",\n common_name='" + commonName + '\'' + ",\n scientific_name=" + Arrays.toString(scientificName) + ",\n sunlight=" + Arrays.toString(sunlight) + ",\n watering='" + watering + '\'' + ",\n defaultImage=" + defaultImage + ",\n nickname='" + nickname + '\'' + ",\n last_watered=" + lastWatered + "\n}";
    }
}
