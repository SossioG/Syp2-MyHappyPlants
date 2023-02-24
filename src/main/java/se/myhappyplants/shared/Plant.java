package se.myhappyplants.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Plant implements Serializable {
    private int id;
    private String common_name;
    private String[] scientific_name;
    private String[] sunlight;
    private String watering;
    @JsonProperty("default_image")
    private DefaultImage defaultImage;
    private String nickname;
    private LocalDate last_watered;

    public Plant(){
    }

    public Plant(String uniqueNickName, int id, LocalDate date, String imageURL) {
        this.nickname = uniqueNickName;
        this.id = id;
        this.last_watered = date;
        DefaultImage defaultImage = new DefaultImage();
        defaultImage.setThumbnail(imageURL);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String[] getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String[] scientific_name) {
        this.scientific_name = scientific_name;
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

    public LocalDate getLast_watered() {
        return last_watered;
    }

    public void setLast_watered(LocalDate last_watered) {
        this.last_watered = last_watered;
    }

    public long getMillisFrequency(){
        long amountOfDaysMillis;
        if (watering == "frequenct"){
            amountOfDaysMillis = 86400000;
        } else if (watering == "average"){
            amountOfDaysMillis = 259200000;
        } else if (watering == "minimum"){
            amountOfDaysMillis = 864000000;
        } else {
            amountOfDaysMillis = -1; // No watering needed
        }
        return amountOfDaysMillis;
    }

    public String getDaysUntilWater() {
        if(getMillisFrequency() == -1){
            return "Din planta behöver ej vatten";
        } else {
           // long diffInMillies = Math.abs(last_watered() - LocalDateTime.now().getHour() - getMillisFrequency());
            long diff = TimeUnit.DAYS.convert(86400000, TimeUnit.MILLISECONDS);
            return "Antal dagar till vattning: " + String.valueOf(diff);
        }
    }

    /**
     * TODO: Fixa metoden här
     * @return
     */
    public double getProgress() {
        return 1.0;
    }
}
