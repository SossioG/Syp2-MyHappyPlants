package se.myhappyplants.client.model;

import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class JukeBox {

    private int volume;
    private boolean muted;

    private Slider soundSlider;

    private MediaPlayer mediaPlayer;

    private ImageView muteButton;
    private Media media;

    public JukeBox(Slider soundSlider, ImageView muteButton ){
        this.volume = 50;
        this.soundSlider = soundSlider;
        this.muteButton = muteButton;
        this.soundSlider.setValue(volume);
        //media = new Media("Sounds/Elevator_music.mp3");
        this.mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setMute(muted);
    }

    public void changeVolume(){
        if(!muted)
            mediaPlayer.setVolume(soundSlider.getValue());
    }

    public void mute(){
        if(volume == 0)
            mediaPlayer.setMute(true);
        else{
            mediaPlayer.setMute(false);
        }
    }
}
