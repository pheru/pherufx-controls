package de.pheru.fx.controls.notification;

import com.sun.javafx.stage.StageHelper;
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
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

/**
 * @author Philipp Bruckner
 */
public class Notification {

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
    final private Timeline durationTimeline = new Timeline();

    public Notification(Type type, Node content) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NotificationManager.class.getResource("notification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
            dontShowAgainBox.setVisible(false);
            dontShowAgainBox.setManaged(false);
            contentBox.getChildren().add(content);
            root.heightProperty().addListener((observable, oldValue, newValue) -> {
                NotificationManager.arrangeNotifications(true);
            });
            if (type != null && NotificationManager.isStyleByType()) {
                root.setStyle(type.getStyle());
            }
        } catch (IOException e) {
            throw new RuntimeException("TODO", e); //TODO Exc
        }
    }

    public Notification(Node content) {
        this(null, content);
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

    //TODO rearrange wenn durch owner-iconify nicht sichtbar
    //   oder owner nicht mehr (einzeln) bei show anbieten
    //   oder notification in owner-window anzeigen
    //TODO Sound vor oder nach show?
    public void show(Window owner, boolean playSound) {
        if (playSound) {
            Toolkit.getDefaultToolkit().beep();
        }
        showImpl(owner);
    }

    public void show(Window owner) {
        if (NotificationManager.isPlaySound()) {
            Toolkit.getDefaultToolkit().beep();
        }
        showImpl(owner);
    }

    public void show() {
        show(getNotificationStage());
    }

    public void show(boolean playSound) {
        show(getNotificationStage(), playSound);
    }

    private void showImpl(Window owner) {
        Popup popup = initPopup();
        NotificationManager.getNotifications().add(this);
        popup.show(owner);

        if (duration.get() != Duration.INDEFINITE) {
            durationTimeline.getKeyFrames().add(new KeyFrame(duration.get(), (ActionEvent event) -> {
                hide(true);
            }));
            durationTimeline.play();
        }
    }

    private Popup initPopup() {
        Popup popup = new Popup();
        popup.setAutoFix(false);
        root.getStylesheets().add(getClass().getResource("notification.css").toExternalForm());
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
            KeyValue fadeOutBegin = new KeyValue(root.opacityProperty(), 1.0);
            KeyValue fadeOutEnd = new KeyValue(root.opacityProperty(), 0.0);
            KeyFrame kfBegin = new KeyFrame(Duration.ZERO, fadeOutBegin);
            KeyFrame kfEnd = new KeyFrame(Duration.millis(500), fadeOutEnd);
            Timeline fadeOutTimeline = new Timeline(kfBegin, kfEnd);
            fadeOutTimeline.setOnFinished((ActionEvent event) -> {
                hideImpl();
            });
            fadeOutTimeline.play();
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
            new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(windowY, position))).play();
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
            new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(windowX, position))).play();
        }
    }

    protected double getHeight() {
        return root.getHeight();
    }

    protected double getWidth() {
        return root.getWidth();
    }

    public enum Type {

        INFO("img/Info.png", "-fx-background-color: rgba(0, 0, 50, 0.8);"),
        WARNING("img/Warning.png", "-fx-background-color: rgba(50, 50, 0, 0.8);"),
        ERROR("img/Error.png", "-fx-background-color: rgba(50, 0, 0, 0.8);");

        private final String imagePath;
        private final String style;

        Type(final String imagePath, String style) {
            this.imagePath = imagePath;
            this.style = style;
        }

        protected String getImagePath() {
            return imagePath;
        }

        protected String getStyle() {
            return style;
        }
    }
}
