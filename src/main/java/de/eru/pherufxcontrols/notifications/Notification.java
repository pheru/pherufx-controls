package de.eru.pherufxcontrols.notifications;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Philipp Bruckner
 */
public abstract class Notification implements Initializable {

    protected final BooleanProperty dontShowAgain = new SimpleBooleanProperty(false);
    protected final IntegerProperty timer = new SimpleIntegerProperty(4);
    protected final IntegerProperty position = new SimpleIntegerProperty(-1);
    protected Parent root;

    public void show() {
        timer.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() == 0) {
                Platform.runLater(() -> {
                    root.getScene().getWindow().hide();
                });
            }
        });
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Notifications.add(this);
        startTimer();
        root.getScene().getWindow().setOnHidden((WindowEvent event) -> {
            Notifications.remove(this);
        });
    }

    public void startTimer() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000 * timer.get());
                timer.set(0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public Boolean isDontShowAgain() {
        return dontShowAgain.get();
    }

    public void setDontShowAgain(final Boolean dontShowAgain) {
        this.dontShowAgain.set(dontShowAgain);
    }

    public BooleanProperty dontShowAgainProperty() {
        return dontShowAgain;
    }

    public Integer getTimer() {
        return timer.get();
    }

    public void setTimer(final Integer timer) {
        this.timer.set(timer);
    }

    public IntegerProperty timerProperty() {
        return timer;
    }

    public Integer getPosition() {
        return position.get();
    }

    public void setPosition(final Integer position) {
        root.getScene().getWindow().setY(position * 400);
        this.position.set(position);
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }
}
