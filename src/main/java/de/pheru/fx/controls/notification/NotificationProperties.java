package de.pheru.fx.controls.notification;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * Created by Philipp on 23.03.2016.
 */
public class NotificationProperties {

    private final ObjectProperty<Screen> screen = new SimpleObjectProperty<>(Screen.getPrimary());
    private final ObjectProperty<Window> window = new SimpleObjectProperty<>(null);
    private final ObjectProperty<Pos> position = new SimpleObjectProperty<>(Pos.BOTTOM_RIGHT);
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(Duration.INDEFINITE);
    private final BooleanProperty animateShow = new SimpleBooleanProperty(true);
    private final BooleanProperty fadeOut = new SimpleBooleanProperty(true);
    private final BooleanProperty closeButtonVisible = new SimpleBooleanProperty(true);
    private final BooleanProperty playSound = new SimpleBooleanProperty(false);
    private final BooleanProperty styleByType = new SimpleBooleanProperty(true);
    private final ObservableList<String> styleSheets = FXCollections.observableArrayList();

    public NotificationProperties() {
    }

    public NotificationProperties(NotificationProperties copy) {
        screen.set(copy.getScreen());
        window.set(copy.getWindow());
        position.set(copy.getPosition());
        duration.set(copy.getDuration());
        animateShow.set(copy.isAnimateShow());
        fadeOut.set(copy.isFadeOut());
        closeButtonVisible.set(copy.isCloseButtonVisible());
        playSound.set(copy.isPlaySound());
        styleByType.set(copy.isStyleByType());
        styleSheets.addAll(copy.getStyleSheets());
    }

    public Screen getScreen() {
        return screen.get();
    }

    public ObjectProperty<Screen> screenProperty() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen.set(screen);
    }

    public Window getWindow() {
        return window.get();
    }

    public ObjectProperty<Window> windowProperty() {
        return window;
    }

    public void setWindow(Window window) {
        this.window.set(window);
    }

    public Pos getPosition() {
        return position.get();
    }

    public ObjectProperty<Pos> positionProperty() {
        return position;
    }

    public void setPosition(Pos position) {
        this.position.set(position);
    }

    public Duration getDuration() {
        return duration.get();
    }

    public ObjectProperty<Duration> durationProperty() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration.set(duration);
    }

    public boolean isAnimateShow() {
        return animateShow.get();
    }

    public BooleanProperty animateShowProperty() {
        return animateShow;
    }

    public void setAnimateShow(boolean animateShow) {
        this.animateShow.set(animateShow);
    }

    public boolean isFadeOut() {
        return fadeOut.get();
    }

    public BooleanProperty fadeOutProperty() {
        return fadeOut;
    }

    public void setFadeOut(boolean fadeOut) {
        this.fadeOut.set(fadeOut);
    }

    public boolean isCloseButtonVisible() {
        return closeButtonVisible.get();
    }

    public BooleanProperty closeButtonVisibleProperty() {
        return closeButtonVisible;
    }

    public void setCloseButtonVisible(boolean closeButtonVisible) {
        this.closeButtonVisible.set(closeButtonVisible);
    }

    public boolean isPlaySound() {
        return playSound.get();
    }

    public BooleanProperty playSoundProperty() {
        return playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound.set(playSound);
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

    public ObservableList<String> getStyleSheets() {
        return styleSheets;
    }
}
