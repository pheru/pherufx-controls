package de.eru.pherufxcontrols.notifications;

import java.security.KeyException;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Philipp Bruckner
 */
public abstract class Notification implements Initializable {

    protected final IntegerProperty timer = new SimpleIntegerProperty(4);
    protected final StringProperty title = new SimpleStringProperty("Benachrichtigung");
    protected Parent root;

    public void show() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        Scene scene = new Scene(root);
        scene.setFill(null);
        scene.getStylesheets().add(getClass().getResource("/css/notification/notification.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle(title.get());
        if (this instanceof InfoNotification) {
            stage.getIcons().add(((InfoNotification) this).getImage());
        }
        root.getScene().getWindow().setOnHidden((WindowEvent event) -> {
            Notifications.removeNotification(this);
        });

        KeyValue fadeOutBegin = new KeyValue(root.opacityProperty(), 1.0);
        KeyValue fadeOutEnd = new KeyValue(root.opacityProperty(), 0.0);

        KeyFrame kfBegin = new KeyFrame(Duration.ZERO, fadeOutBegin);
        KeyFrame kfEnd = new KeyFrame(Duration.millis(750), fadeOutEnd);
        Timeline timeline = new Timeline(kfBegin, kfEnd);
        timeline.setOnFinished((ActionEvent event) -> {
            root.getScene().getWindow().hide();
        });
        timer.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() == 0) {
                timeline.play();
            }
        });

        stage.show();
        Notifications.addNotification(this);
        startTimer();
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
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public Integer getTimer() {
        return timer.get();
    }

    public Notification setTimer(final Integer timer) {
        this.timer.set(timer);
        return this;
    }

    public IntegerProperty timerProperty() {
        return timer;
    }

    public void setY(final double position, boolean animated) {
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

    public double getY() {
        return root.getScene().getWindow().getY();
    }

    public ReadOnlyDoubleProperty yProperty() {
        return root.getScene().getWindow().yProperty();
    }

    public void setX(final double position) {
        root.getScene().getWindow().setX(position);
    }

    public double getX() {
        return root.getScene().getWindow().getX();
    }

    public ReadOnlyDoubleProperty xProperty() {
        return root.getScene().getWindow().xProperty();
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public double getHeight() {
        return root.getScene().getWindow().getHeight();
    }

    public String getTitle() {
        return title.get();
    }

    public Notification setTitle(final String title) {
        this.title.set(title);
        return this;
    }

    public StringProperty titleProperty() {
        return title;
    }

}
