package de.pheru.fx.controls.notification;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.util.Duration;

/**
 * Created by Philipp on 23.03.2016.
 */
public final class NotificationDefaults {

    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(Duration.INDEFINITE);
    private final BooleanProperty styleByType = new SimpleBooleanProperty(true);
    private final BooleanProperty playSound = new SimpleBooleanProperty(false);
    private final ObjectProperty<Pos> position = new SimpleObjectProperty<>(Pos.BOTTOM_RIGHT);
    
    public NotificationDefaults(){
    }

    public Pos getPosition() {
        return position.get();
    }

    public void setPosition(Pos position) {
        this.position.set(position);
    }

    public ObjectProperty<Pos> positionProperty() {
        return position;
    }

    public Duration getDuration() {
        return duration.get();
    }

    public void setDuration(final Duration duration) {
        this.duration.set(duration);
    }

    public ObjectProperty<Duration> durationProperty() {
        return duration;
    }

    public boolean isPlaySound() {
        return playSound.get();
    }

    public void setPlaySound(final boolean playSound) {
        this.playSound.set(playSound);
    }

    public BooleanProperty playSoundProperty() {
        return playSound;
    }

    public boolean isStyleByType() {
        return styleByType.get();
    }

    public BooleanProperty styleByTypeProperty() {
        return styleByType;
    }

    public void setStyleByType(boolean styleByType) {
        this.styleByType.set(styleByType);
    }
}
