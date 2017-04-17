package de.pheru.fx.controls.marquee;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Duration;

public class Marquee extends Control {

    private final StringProperty text = new SimpleStringProperty();
    private final ObjectProperty<Duration> animationStartDelay = new SimpleObjectProperty<>(Duration.millis(1000));
    private final ObjectProperty<Duration> animationEndDelay = new SimpleObjectProperty<>(Duration.millis(1000));
    private final DoubleProperty animationSpeed = new SimpleDoubleProperty(1.0);

    public Marquee(final String text) {
        setText(text);
        initChangeListeners();
    }

    private void initChangeListeners() {
        animationSpeedProperty().addListener((observable, oldValue, newValue) -> {
            final double newValueAsDouble = newValue.doubleValue();
            if (newValueAsDouble < 0.1) {
                setAnimationSpeed(0.1);
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MarqueeSkin(this);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(final String text) {
        this.text.set(text);
    }

    public Duration getAnimationStartDelay() {
        return animationStartDelay.get();
    }

    public ObjectProperty<Duration> animationStartDelayProperty() {
        return animationStartDelay;
    }

    public void setAnimationStartDelay(final Duration animationStartDelay) {
        this.animationStartDelay.set(animationStartDelay);
    }

    public Duration getAnimationEndDelay() {
        return animationEndDelay.get();
    }

    public ObjectProperty<Duration> animationEndDelayProperty() {
        return animationEndDelay;
    }

    public void setAnimationEndDelay(final Duration animationEndDelay) {
        this.animationEndDelay.set(animationEndDelay);
    }

    public double getAnimationSpeed() {
        return animationSpeed.get();
    }

    public DoubleProperty animationSpeedProperty() {
        return animationSpeed;
    }

    public void setAnimationSpeed(final double animationSpeed) {
        this.animationSpeed.set(animationSpeed);
    }

}
