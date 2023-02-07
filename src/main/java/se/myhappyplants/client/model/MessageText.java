package se.myhappyplants.client.model;

/**
 * An enum class to set the messages in the PopupBox to show the user the execution status
 * Created by: Linn Borgström, 2021-05-17
 * Updated by: Linn Borgström, 2021-05-17
 */
public enum MessageText {
    sucessfullyAddPlant("You added a plant"),
    sucessfullyChangedPlant("You changed a plants information"),
    sucessfullyChangedDate("You changed the water date"),
    removePlant("You removed a plant"),
    holdOnGettingInfo("Hold on while we are getting your information");


    private final String name;

    /**
     * Constreuctor to set the text
     * @param name
     */
    MessageText(String name) {
        this.name = name;
    }

    /**
     * ToString method with the text
     * @return
     */
    public String toString() {
        return name;
    }
}
