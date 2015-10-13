package de.pheru.fx.controls.notification;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Philipp Bruckner
 */
public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
        BooleanProperty dsa = new SimpleBooleanProperty(true);
        Notifications.setDefaultTimer(5);
        
        Notifications.setAlignment(Notifications.Alignment.BOTTOM_RIGHT);
        VBox box = new VBox(new Label("Liar Liar"), new Label("Haven"), new Label("Kamelot"));
        VBox box2 = new VBox(new Label("Liar Liar"), new Label("Haven"), new Label("Kamelot"));
        Notifications.createCustomNotification(box).setTimer(14).setExitButtonVisible(true).show(primaryStage);
        Notifications.createCustomNotification(box2).setTimer(14).show(primaryStage);
        Notifications.createNotification(Notification.Type.INFO).setText("Test"
                + "Das ist ein sehr langer Text, nicht wahr? Das will ich aber so!"
                + "Deshalb bleibt der auch so lange!").setTimer(13)
                .setExitButtonVisible(false).bindDontShowAgainProperty(dsa).show(primaryStage);
        Notifications.createNotification(Notification.Type.INFO).setText("Test"
                + "Das ist ein sehr langer Text, nicht wahr? Das will ich aber so!"
                + "Deshalb bleibt der auch so lange!").setTimer(13).setExitButtonVisible(false).show(primaryStage);
        Notifications.createNotification(Notification.Type.INFO).setText("Test"
                + "Das ist ein sehr langer Text, nicht wahr? Das will ich aber so!"
                + "Deshalb bleibt der auch so lange!").setTimer(13).show(primaryStage);
    }
}
