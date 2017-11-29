package de.pheru.fx.controls.marquee;

import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Duration;

public class Marquee extends Control {

    private final StringProperty text = new SimpleStringProperty();
    private final ObjectProperty<Duration> animationStartDelay = new SimpleObjectProperty<>(Duration.millis(1000));
    private final ObjectProperty<Duration> animationEndDelay = new SimpleObjectProperty<>(Duration.millis(1000));
    private final DoubleProperty animationSpeed = new SimpleDoubleProperty(1.0);
    private final BooleanProperty infiniteScroll = new SimpleBooleanProperty(false);
    private final StringProperty infiniteScrollSeparator = new SimpleStringProperty("   -   ");

    public Marquee(){
        initChangeListeners();
    }

    public Marquee(final String text) {
        this();
        this.text.set(text);
    }

    public Marquee(final String text, final boolean infiniteScroll) {
        this(text);
        this.infiniteScroll.set(infiniteScroll);
    }

    private void initChangeListeners() {
        animationSpeed.addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < 0.1) {
                animationSpeed.set(0.1);
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

    public boolean isInfiniteScroll() {
        return infiniteScroll.get();
    }

    public BooleanProperty infiniteScrollProperty() {
        return infiniteScroll;
    }

    public void setInfiniteScroll(final boolean infiniteScroll) {
        this.infiniteScroll.set(infiniteScroll);
    }

    public String getInfiniteScrollSeparator() {
        return infiniteScrollSeparator.get();
    }

    public StringProperty infiniteScrollSeparatorProperty() {
        return infiniteScrollSeparator;
    }

    public void setInfiniteScrollSeparator(final String infiniteScrollSeparator) {
        this.infiniteScrollSeparator.set(infiniteScrollSeparator);
    }
}
