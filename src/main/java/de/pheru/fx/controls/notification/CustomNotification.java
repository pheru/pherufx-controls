package de.pheru.fx.controls.notification;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

    private final IntegerProperty timer = new SimpleIntegerProperty(Notifications.getDefaultTimer());

    protected CustomNotification() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dontShowAgainBox.setVisible(false);
        dontShowAgainBox.setManaged(false);
    }

    public void show(Window owner, Boolean playSound) {
        Popup popup = initPopup();
        initFadeOutTimeline();

        popup.show(owner);
        if ((playSound == null && Notifications.isPlaySound())
                || (playSound != null && playSound)) {
            Toolkit.getDefaultToolkit().beep();
        }
        Notifications.addNotification(this);
        if (timer.get() != Notifications.TIMER_INDEFINITE) {
            startTimer();
        }
    }

    public void show(Window owner) {
        show(owner, null);
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

    private void initFadeOutTimeline() {
        KeyValue fadeOutBegin = new KeyValue(root.opacityProperty(), 1.0);
        KeyValue fadeOutEnd = new KeyValue(root.opacityProperty(), 0.0);
        KeyFrame kfBegin = new KeyFrame(Duration.ZERO, fadeOutBegin);
        KeyFrame kfEnd = new KeyFrame(Duration.millis(500), fadeOutEnd);
        Timeline fadeOutTimeline = new Timeline(kfBegin, kfEnd);
        fadeOutTimeline.setOnFinished((ActionEvent event) -> {
            hide();
        });
        timer.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.doubleValue() <= 0.0) {
                fadeOutTimeline.play();
            }
        });
    }

    private void startTimer() {
        Thread t = new Thread(() -> {
            try {
                while (timer.get() > 0) {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        timer.set(timer.get() - 1);
                    });
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Timer interrupted!", e);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void closeNotification() {
        hide();
    }

    public void hide() {
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

    public CustomNotification hideOnMouseClicked() {
        return setOnMouseClicked((MouseEvent event) -> {
            hide();
        });
    }

    public int getTimer() {
        return timer.get();
    }

    public CustomNotification setTimer(final int timer) {
        this.timer.set(timer);
        return this;
    }

    public IntegerProperty timerProperty() {
        return timer;
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
