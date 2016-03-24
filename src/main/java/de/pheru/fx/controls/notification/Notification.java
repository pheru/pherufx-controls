package de.pheru.fx.controls.notification;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author Philipp Bruckner
 */
public class Notification {

    public static final double WIDTH = 350.0;

    private static NotificationDefaults defaults = new NotificationDefaults();

    @FXML
    private GridPane root;
    @FXML
    private HBox contentBox;
    @FXML
    private CheckBox dontShowAgainBox;
    @FXML
    private Button exitButton;

    private Popup popup;

    private Pos position = defaults.getPosition();
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(defaults.getDuration());
    private final Timeline durationTimeline = new Timeline();
    private Timeline xTimeline;
    private Timeline yTimeline;

    public Notification(Type type, Node content) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NotificationManager.class.getResource("notification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();

            duration.addListener((observable, oldValue, newValue) -> durationTimeline.playFromStart());
            dontShowAgainBox.setVisible(false);
            dontShowAgainBox.setManaged(false);
            contentBox.getChildren().add(content);
            if (type != Type.NONE && defaults.isStyleByType()) {
                root.getStyleClass().add(type.getStyleClass());
            }
            root.setOnMouseEntered(event -> durationTimeline.stop());
            root.setOnMouseExited(event -> durationTimeline.play());
        } catch (IOException e) {
            throw new RuntimeException("TODO", e); //TODO Exc
        }
    }

    public Notification(Node content) {
        this(Type.NONE, content);
    }

    public Notification(Type type, String text, String header) {
        this(type, new NotificationContent(type, text, header).getRoot());
    }

    public Notification(Type type, String text) {
        this(type, new NotificationContent(type, text, "").getRoot());
    }

    public Notification(Type type, StringProperty textProperty, StringProperty headerProperty) {
        this(type, new NotificationContent(type, textProperty, headerProperty).getRoot());
    }

    public Notification(Type type, StringProperty textProperty, String header) {
        this(type, new NotificationContent(type, textProperty, header).getRoot());
    }

    public Notification(Type type, StringProperty textProperty) {
        this(type, new NotificationContent(type, textProperty, "").getRoot());
    }

    public void show() {
        show(defaults.isPlaySound(), GlobalNotificationManager.getNotificationStage());
    }

    public void show(boolean playSound) {
        show(playSound, GlobalNotificationManager.getNotificationStage());
    }

    public void show(Window window) {
        show(defaults.isPlaySound(), window);
    }

    public void show(boolean playSound, Window window) {
        NotificationManager.getInstanceForOwner(window).show(playSound, this);
        if (duration.get() != Duration.INDEFINITE) {
            durationTimeline.getKeyFrames().clear();
            durationTimeline.getKeyFrames().add(new KeyFrame(duration.get(), (ActionEvent event) -> {
                hide(true);
            }));
            durationTimeline.playFromStart();
        }
    }

    @FXML
    private void closeNotification() {
        hide(false);
    }

    public void hide(boolean fadeOut) {
        durationTimeline.stop();
        if (fadeOut) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished((ActionEvent event) -> {
                hide();
            });
            fadeTransition.play();
        } else {
            hide();
        }
    }

    private void hide() {
        if (root.getScene() != null) {
            popup.hide();
            dontShowAgainBox.selectedProperty().unbind();
        }
    }

    public static void hideAll() {
        hideAll(GlobalNotificationManager.getNotificationStage());
    }

    public static void hideAll(Window owner) {
        NotificationManager.getInstanceForOwner(owner).hideAll();
    }

    protected Popup getPopup() {
        if (popup == null) {
            popup = new Popup();
            popup.setAutoFix(false);
            popup.getContent().add(root);
            popup.setOnHidden((WindowEvent event) -> {
                NotificationManager.getInstanceForOwner(popup.getOwnerWindow()).removeNotification(this);
            });
        }
        return popup;
    }

    protected void setY(final double position, boolean animated) {
        if (!animated) {
            popup.setY(position);
        } else {
            DoubleProperty popupY = new SimpleDoubleProperty(popup.getY());
            popupY.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                popup.setY(newValue.doubleValue());
            });
            if (yTimeline != null) {
                yTimeline.stop();
            }
            yTimeline = new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(popupY, position)));
            yTimeline.play();
        }
    }

    protected void setX(final double position, boolean animated) {
        if (!animated) {
            popup.setX(position);
        } else {
            DoubleProperty popupX = new SimpleDoubleProperty(popup.getX());
            popupX.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                popup.setX(newValue.doubleValue());
            });
            if (xTimeline != null) {
                xTimeline.stop();
            }
            xTimeline = new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(popupX, position)));
            xTimeline.play();
        }
    }

    public void setOnMouseClicked(EventHandler<? super MouseEvent> eventHandler, boolean hide) { //TODO überarbeiten
        root.setOnMouseClicked(event -> {
            eventHandler.handle(event);
            if (hide) {
                hide(false);
            }
        });
    }

    public void hideOnMouseClicked() {
        root.setOnMouseClicked((MouseEvent event) -> {
            hide(false);
        });
    }

    public void bindDontShowAgainProperty(Property<Boolean> property) {
        dontShowAgainBox.selectedProperty().bindBidirectional(property);
        dontShowAgainBox.setVisible(true);
        dontShowAgainBox.setManaged(true);
    }


    protected GridPane getRoot() {
        return root;
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

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return durationTimeline.currentTimeProperty();
    }

    public Duration getCurrentTime() {
        return durationTimeline.getCurrentTime();
    }

    public Notification setExitButtonVisible(boolean exitButtonVisible) {
        exitButton.setVisible(exitButtonVisible);
        exitButton.setManaged(exitButtonVisible);
        return this;
    }

    public boolean isExitButtonVisible() {
        return exitButton.isVisible();
    }

    public Pos getPosition() {
        return position;
    }

    public void setPosition(Pos position) {
        this.position = position;
    }

    public static NotificationDefaults getDefaults() {
        return defaults;
    }

    public static void setDefaults(NotificationDefaults defaults) {
        Notification.defaults = defaults;
    }

    public enum Type {

        INFO("img/Info.png", "info"),
        WARNING("img/Warning.png", "warning"),
        ERROR("img/Error.png", "error"),
        NONE("", "");

        private final String imagePath;
        private final String styleClass;

        Type(final String imagePath, String styleClass) {
            this.imagePath = imagePath;
            this.styleClass = styleClass;
        }

        protected String getImagePath() {
            return imagePath;
        }

        protected String getStyleClass() {
            return styleClass;
        }
    }
}
