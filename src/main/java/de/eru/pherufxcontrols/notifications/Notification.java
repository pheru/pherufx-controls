package de.eru.pherufxcontrols.notifications;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 *
 * @author Philipp Bruckner
 */
public abstract class Notification implements Initializable {

    protected final IntegerProperty timer = new SimpleIntegerProperty(4);
    protected final StringProperty title = new SimpleStringProperty("Benachrichtigung");
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
        stage.setAlwaysOnTop(true);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title.get());
        if (this instanceof InfoNotification) {
            stage.getIcons().add(((InfoNotification) this).getImage());
        }
        stage.show();
        root.getScene().getWindow().setOnHidden((WindowEvent event) -> {
            Notifications.removeNotification(this);
        });
        Notifications.addNotification(this);
        startTimer();
    }

    private void startTimer() {
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

    public void setY(final double position) {
        Window window = root.getScene().getWindow();
        window.setY(position);
    }

    public void setX(final double position) {
        Window window = root.getScene().getWindow();
        window.setX(position);
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
