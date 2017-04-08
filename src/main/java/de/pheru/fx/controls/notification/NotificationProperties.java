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
    private final ObjectProperty<Duration> animationDuration = new SimpleObjectProperty<>(Duration.millis(300));
    private final BooleanProperty fadeOut = new SimpleBooleanProperty(true);
    private final ObjectProperty<Duration> fadeOutDuration = new SimpleObjectProperty<>(Duration.millis(500));
    private final BooleanProperty closable = new SimpleBooleanProperty(true);
    private final BooleanProperty hideOnMouseClicked = new SimpleBooleanProperty(false);
    private final BooleanProperty playSound = new SimpleBooleanProperty(false);
    private final ObjectProperty<StyleByType> styleByType = new SimpleObjectProperty<>(StyleByType.ICON_AND_STYLE);
    private final ObservableList<String> styleSheets = FXCollections.observableArrayList();

    public NotificationProperties() {
    }

    public NotificationProperties(NotificationProperties copy) {
        screen.set(copy.getScreen());
        window.set(copy.getWindow());
        position.set(copy.getPosition());
        duration.set(copy.getDuration());
        animateShow.set(copy.isAnimateShow());
        animationDuration.set(copy.getAnimationDuration());
        fadeOut.set(copy.isFadeOut());
        fadeOutDuration.set(copy.getFadeOutDuration());
        closable.set(copy.isClosable());
        hideOnMouseClicked.set(copy.isHideOnMouseClicked());
        playSound.set(copy.isPlaySound());
        styleByType.set(copy.getStyleByType());
        styleSheets.addAll(copy.getStyleSheets());
    }

    public enum StyleByType {
        ICON(true, false),
        STYLE(false, true),
        ICON_AND_STYLE(true, true);

        private final boolean showIcon;
        private final boolean applyStyle;

        StyleByType(final boolean showIcon, final boolean applyStyle) {
            this.showIcon = showIcon;
            this.applyStyle = applyStyle;
        }

        public boolean isShowIcon() {
            return showIcon;
        }

        public boolean isApplyStyle() {
            return applyStyle;
        }
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

    public Duration getAnimationDuration() {
        return animationDuration.get();
    }

    public ObjectProperty<Duration> animationDurationProperty() {
        return animationDuration;
    }

    public void setAnimationDuration(Duration animationDuration) {
        this.animationDuration.set(animationDuration);
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

    public Duration getFadeOutDuration() {
        return fadeOutDuration.get();
    }

    public ObjectProperty<Duration> fadeOutDurationProperty() {
        return fadeOutDuration;
    }

    public void setFadeOutDuration(Duration fadeOutDuration) {
        this.fadeOutDuration.set(fadeOutDuration);
    }

    public boolean isClosable() {
        return closable.get();
    }

    public BooleanProperty closableProperty() {
        return closable;
    }

    public void setClosable(final boolean closable) {
        this.closable.set(closable);
    }

    public boolean isHideOnMouseClicked() {
        return hideOnMouseClicked.get();
    }

    public BooleanProperty hideOnMouseClickedProperty() {
        return hideOnMouseClicked;
    }

    public void setHideOnMouseClicked(boolean hideOnMouseClicked) {
        this.hideOnMouseClicked.set(hideOnMouseClicked);
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

    public StyleByType getStyleByType() {
        return styleByType.get();
    }

    public ObjectProperty<StyleByType> styleByTypeProperty() {
        return styleByType;
    }

    public void setStyleByType(final StyleByType styleByType) {
        this.styleByType.set(styleByType);
    }

    public ObservableList<String> getStyleSheets() {
        return styleSheets;
    }
}
