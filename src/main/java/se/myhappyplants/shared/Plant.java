package se.myhappyplants.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

    @Override
    public String toString() {
        return "Plant{\n" + "id=" + id + ",\n common_name='" + commonName + '\'' + ",\n scientific_name=" + Arrays.toString(scientificName) + ",\n sunlight=" + Arrays.toString(sunlight) + ",\n watering='" + watering + '\'' + ",\n defaultImage=" + defaultImage + ",\n nickname='" + nickname + '\'' + ",\n last_watered=" + lastWatered + "\n}";
    }
}
