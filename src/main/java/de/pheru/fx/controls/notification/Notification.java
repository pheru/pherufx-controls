package de.pheru.fx.controls.notification;

import com.sun.javafx.stage.StageHelper;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

/**
 * @author Philipp Bruckner
 */
public class Notification {

    public static final double WIDTH = 350.0;

    private static Stage notificationStage;

    @FXML
    private GridPane root;
    @FXML
    private HBox contentBox;
    @FXML
    private CheckBox dontShowAgainBox;
    @FXML
    private Button exitButton;

    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(NotificationManager.getDefaultDuration());
    private final Timeline durationTimeline = new Timeline();
    private Timeline xTimeline;
    private Timeline yTimeline;

    public Notification(Type type, Node content) {
        duration.addListener((observable, oldValue, newValue) -> durationTimeline.playFromStart());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NotificationManager.class.getResource("notification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
            dontShowAgainBox.setVisible(false);
            dontShowAgainBox.setManaged(false);
            contentBox.getChildren().add(content);
            if (type != Type.NONE && NotificationManager.isStyleByType()) {
                root.getStyleClass().add(type.getStyleClass());
            }
            //TODO Über NotificationManager einstellen können (#28)
//            root.setOnMouseEntered(event -> durationTimeline.pause());
//            root.setOnMouseExited(event -> durationTimeline.play());
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
        show(NotificationManager.getNotifications().size());
    }

    public void show(int index) {
        if (NotificationManager.isPlaySound()) {
            Toolkit.getDefaultToolkit().beep();
        }
        showImpl(index);
    }

    public void show(boolean playSound) {
        show(NotificationManager.getNotifications().size(), playSound);
    }

    public void show(int index, boolean playSound) {
        if (playSound) {
            Toolkit.getDefaultToolkit().beep();
        }
        showImpl(index);
    }

    private void showImpl(int index) {
        int i = index;
        if (i > NotificationManager.getNotifications().size()) {
            i = NotificationManager.getNotifications().size();
        }
        Popup popup = initPopup();
        //TODO Wie sieht es bei 2 Bildschirmen aus?
        //X-Koordinate vor show, damit das Popup zu Beginn außerhalb der Bildschirms ist
        popup.setX(NotificationManager.getScreen().getVisualBounds().getMaxX());

        popup.show(getNotificationStage());

        //Y-Koordinate nach show, da evtl. die Höhe des Popups gebraucht wird
        double targetY = NotificationManager.getTargetY(i);
        switch (NotificationManager.getPosition().getVpos()) {
            case TOP: //targetY passt
                break;
            case CENTER:
                targetY -= (popup.getHeight() + NotificationManager.NOTIFICATION_VERTICAL_SPACING) / 2;
                break;
            case BOTTOM:
            default:
                targetY -= popup.getHeight() + NotificationManager.NOTIFICATION_VERTICAL_SPACING;
                break;
        }
        popup.setY(targetY);

        popup.heightProperty().addListener((observable, oldValue, newValue) -> {
            NotificationManager.arrangeNotifications(true);
        });

        NotificationManager.getNotifications().add(i, this);

        if (duration.get() != Duration.INDEFINITE) {
            durationTimeline.getKeyFrames().add(new KeyFrame(duration.get(), (ActionEvent event) -> {
                hide(true);
            }));
            durationTimeline.playFromStart();
        }
    }

    private Popup initPopup() {
        Popup popup = new Popup();
        popup.setAutoFix(false);
        popup.getContent().add(root);
        popup.setOnHidden((WindowEvent event) -> {
            NotificationManager.getNotifications().remove(this);
        });
        return popup;
    }

    private static Stage getNotificationStage() {
        if (notificationStage == null) {
            notificationStage = new Stage(StageStyle.UTILITY);
            notificationStage.setOpacity(0.0);
            notificationStage.show();
            StageHelper.getStages().addListener((ListChangeListener<Stage>) c -> {
                if (c.getList().size() == 1 && c.getList().get(0) == notificationStage) {
                    Platform.runLater(() -> {
                        notificationStage.hide();
                    });
                }
            });
        }
        return notificationStage;
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
                hideImpl();
            });
            fadeTransition.play();
        } else {
            hideImpl();
        }
    }

    private void hideImpl() {
        if (root.getScene() != null) {
            root.getScene().getWindow().hide();
            dontShowAgainBox.selectedProperty().unbind();
        }
    }

    public void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        root.setOnMouseClicked(value);
    }

    public void bindDontShowAgainProperty(Property<Boolean> property) {
        dontShowAgainBox.selectedProperty().bindBidirectional(property);
        dontShowAgainBox.setVisible(true);
        dontShowAgainBox.setManaged(true);
    }

    public void hideOnMouseClicked(boolean fadeOut) {
        setOnMouseClicked((MouseEvent event) -> {
            hide(fadeOut);
        });
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

    protected void setY(final double position, boolean animated) {
        if (!animated) {
            root.getScene().getWindow().setY(position);
        } else {
            DoubleProperty windowY = new SimpleDoubleProperty(root.getScene().getWindow().getY());
            windowY.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                root.getScene().getWindow().setY(newValue.doubleValue());
            });
            if (yTimeline != null) {
                yTimeline.stop();
            }
            yTimeline = new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(windowY, position)));
            yTimeline.play();
        }
    }

    protected void setX(final double position, boolean animated) {
        if (!animated) {
            root.getScene().getWindow().setX(position);
        } else {
            DoubleProperty windowX = new SimpleDoubleProperty(root.getScene().getWindow().getX());
            windowX.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                root.getScene().getWindow().setX(newValue.doubleValue());
            });
            if (xTimeline != null) {
                xTimeline.stop();
            }
            xTimeline = new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(windowX, position)));
            xTimeline.play();
        }
    }

    protected double getHeight() {
        return root.getHeight();
    }

    protected double getWidth() {
        return root.getWidth();
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
