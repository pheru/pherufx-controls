package de.pheru.fx.controls.notification;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author Philipp Bruckner
 */
public class Notification extends NotificationProperties {

    public static final double WIDTH = 350.0;

    private static NotificationProperties defaults = new NotificationProperties();

    @FXML
    private GridPane root;
    @FXML
    private HBox contentBox;
    @FXML
    private CheckBox dontShowAgainBox;
    @FXML
    private Button closeButton;

    private final EventHandler<MouseEvent> hideEventHandler = event -> hide();

    private Popup popup;
    private final Timeline durationTimeline = new Timeline();
    private Timeline xTimeline;
    private Timeline yTimeline;

    public Notification(Type type, Node content) {
        super(defaults);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NotificationManager.class.getResource("notification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load fxml!", e);
        }

        durationProperty().addListener((observable, oldValue, newValue) -> durationTimeline.playFromStart());
        closeButton.managedProperty().bind(closeButtonVisibleProperty());
        closeButton.visibleProperty().bind(closeButtonVisibleProperty());
        contentBox.getChildren().add(content);
        if (type != Type.NONE && defaults.isStyleByType()) {
            root.getStyleClass().add(type.getStyleClass());
        }
        root.setOnMouseEntered(event -> durationTimeline.stop());
        root.setOnMouseExited(event -> durationTimeline.play());
        if (isHideOnMouseClicked()) {
            root.setOnMouseClicked(hideEventHandler);
        }
        hideOnMouseClickedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (root.getOnMouseClicked() == null) {
                    root.setOnMouseClicked(hideEventHandler);
                }
            } else {
                if (root.getOnMouseClicked() == hideEventHandler) {
                    root.setOnMouseClicked(null);
                }
            }
        });
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
        root.getStylesheets().addAll(getStyleSheets());
        getNotificationManagerInstance().show(isAnimateShow(), this);
        if (getDuration() != Duration.INDEFINITE) {
            durationTimeline.getKeyFrames().clear();
            durationTimeline.getKeyFrames().add(new KeyFrame(getDuration(), (ActionEvent event) -> hide()));
            durationTimeline.playFromStart();
        }
    }

    @FXML
    private void closeNotification() {
        hide();
    }

    public void hide() {
        durationTimeline.stop();
        if (isFadeOut()) {
            FadeTransition fadeTransition = new FadeTransition(getFadeOutDuration(), root);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished((ActionEvent event) -> hidePopup());
            fadeTransition.play();
        } else {
            hidePopup();
        }
    }

    private void hidePopup() {
        if (root.getScene() != null) {
            popup.hide();
        }
    }

    public static void hideAll(Screen screen) {
        NotificationManager.getInstanceForScreen(screen).hideAll();
    }

    public static void hideAll(Window owner) {
        NotificationManager.getInstanceForWindow(owner).hideAll();
    }

    protected Popup getPopup() {
        if (popup == null) {
            popup = new Popup();
            popup.setAutoFix(false);
            popup.getContent().add(root);
            popup.setOnHidden((WindowEvent event) -> getNotificationManagerInstance().removeNotification(this));
        }
        return popup;
    }

    private NotificationManager getNotificationManagerInstance() {
        if (getWindow() != null) {
            return NotificationManager.getInstanceForWindow(getWindow());
        } else {
            return NotificationManager.getInstanceForScreen(getScreen());
        }
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

    public void setOnMouseClicked(EventHandler<? super MouseEvent> eventHandler) {
        root.setOnMouseClicked(event -> {
            eventHandler.handle(event);
            if (isHideOnMouseClicked()) {
                hideEventHandler.handle(event);
            }
        });
    }

    public void bindDontShowAgainProperty(Property<Boolean> property) { //TODO Durch getter&setter ersetzen?
        dontShowAgainBox.selectedProperty().bindBidirectional(property);
        dontShowAgainBox.setVisible(true);
        dontShowAgainBox.setManaged(true);
    }

    protected GridPane getRoot() {
        return root;
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return durationTimeline.currentTimeProperty();
    }

    public Duration getCurrentTime() {
        return durationTimeline.getCurrentTime();
    }

    @Override
    public void setPosition(Pos position) {
        // Die Position wird vom NotificationManager gebraucht und
        // darf daher nicht geaendert werden, solange die Notification sichtbar ist
        if (popup != null && popup.isShowing()) {
            throw new IllegalStateException("The position can not be changed while notification is showing!");
        }
        super.setPosition(position);
    }

    @Override
    public void setWindow(Window window) {
        // Das Window wird vom NotificationManager gebraucht und
        // darf daher nicht geaendert werden, solange die Notification sichtbar ist
        if (popup != null && popup.isShowing()) {
            throw new IllegalStateException("The window can not be changed while notification is showing!");
        }
        super.setWindow(window);
    }

    @Override
    public void setScreen(Screen screen) {
        // Der Screen wird vom NotificationManager gebraucht und
        // darf daher nicht geaendert werden, solange die Notification sichtbar ist
        if (popup != null && popup.isShowing()) {
            throw new IllegalStateException("The screen can not be changed while notification is showing!");
        }
        super.setScreen(screen);
    }

    public static NotificationProperties getDefaults() {
        return defaults;
    }

    public static void setDefaults(NotificationProperties defaults) {
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
