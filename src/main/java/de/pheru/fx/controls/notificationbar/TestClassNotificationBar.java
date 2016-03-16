package de.pheru.fx.controls.notificationbar;

import de.pheru.fx.controls.notification.Notification;
import de.pheru.fx.controls.notification.NotificationManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * @author Philipp Bruckner
 */
public class TestClassNotificationBar extends Application {

    private NotificationBar notificationBar;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        notificationBar = new NotificationBar();

        TextField tf = new TextField();

        VBox vBox = new VBox();
        vBox.getChildren().add(notificationBar);
        vBox.getChildren().add(new Label("Test"));
        vBox.getChildren().add(new TableView<>());
        vBox.getChildren().add(tf);
        Button b = new Button("Add Message");
        b.setOnAction(event -> {
            notificationBar.getNotificationNodes().add(new Label(tf.getText()));
        });
        vBox.getChildren().add(b);
        primaryStage.setScene(new Scene(vBox));
        primaryStage.show();
    }

}
