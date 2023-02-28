package se.myhappyplants.shared;
import java.io.Serializable;

public class DefaultImage implements Serializable {
    private String thumbnail;

    public DefaultImage() {

    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
