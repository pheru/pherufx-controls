package de.pheru.fx.controls.notification;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

/**
 *
 * @author Philipp Bruckner
 */
public class CustomNotification implements Initializable {

    @FXML
    private GridPane root;
    @FXML
    private HBox contentBox;
    @FXML
    private CheckBox dontShowAgainBox;
    @FXML
    private Button exitButton;

    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>(Notifications.getDefaultDuration());
    final private Timeline durationTimeline = new Timeline();

    protected CustomNotification() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dontShowAgainBox.setVisible(false);
        dontShowAgainBox.setManaged(false);
    }

    //TODO Sound vor oder nach show?
    public void show(Window owner, boolean playSound) {
        if (playSound) {
            Toolkit.getDefaultToolkit().beep();
        }
        innerShow(owner);
    }

    public void show(Window owner) {
        if (Notifications.isPlaySound()) {
            Toolkit.getDefaultToolkit().beep();
        }
        innerShow(owner);
    }

    private void innerShow(Window owner) {
        Popup popup = initPopup();
        popup.show(owner);

        Notifications.addNotification(this);
        if (duration.get() != Duration.INDEFINITE) {
            durationTimeline.getKeyFrames().add(new KeyFrame(duration.get(), (ActionEvent event) -> {
                hide(true);
            }));
            durationTimeline.play();
        }
    }

    private Popup initPopup() {
        Popup popup = new Popup();
        root.getStylesheets().add(getClass().getResource("/css/notification/notification.css").toExternalForm());
        popup.getContent().add(root);
        root.getScene().getWindow().setOnHidden((WindowEvent event) -> {
            Notifications.removeNotification(this);
        });
        return popup;
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
                hide();
            });
            fadeOutTimeline.play();
        } else {
            hide();
        }
    }

    private void hide() {
        if (root.getScene() != null) {
            root.getScene().getWindow().hide();
            dontShowAgainBox.selectedProperty().unbind();
        }
    }

    public CustomNotification setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        root.setOnMouseClicked(value);
        return this;
    }

    public CustomNotification bindDontShowAgainProperty(Property<Boolean> property) {
        dontShowAgainBox.selectedProperty().bindBidirectional(property);
        dontShowAgainBox.setVisible(true);
        dontShowAgainBox.setManaged(true);
        return this;
    }

    public CustomNotification hideOnMouseClicked(boolean fadeOut) {
        return setOnMouseClicked((MouseEvent event) -> {
            hide(fadeOut);
        });
    }

    public Duration getDuration() {
        return duration.get();
    }

    public CustomNotification setDuration(final Duration duration) {
        this.duration.set(duration);
        return this;
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

    public CustomNotification setExitButtonVisible(boolean exitButtonVisible) {
        exitButton.setVisible(exitButtonVisible);
        exitButton.setManaged(exitButtonVisible);
        return this;
    }

    public boolean isExitButtonVisible() {
        return exitButton.isVisible();
    }

    protected void setContent(Node content) {
        contentBox.getChildren().clear();
        contentBox.getChildren().add(content);
    }

    protected double getY() {
        return root.getScene().getWindow().getY();
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

    protected ReadOnlyDoubleProperty yProperty() {
        return root.getScene().getWindow().yProperty();
    }

    protected double getX() {
        return root.getScene().getWindow().getX();
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

    protected ReadOnlyDoubleProperty xProperty() {
        return root.getScene().getWindow().xProperty();
    }

    protected double getHeight() {
        return root.getScene().getWindow().getHeight();
    }

    protected double getWidth() {
        return root.getScene().getWindow().getWidth();
    }

}
