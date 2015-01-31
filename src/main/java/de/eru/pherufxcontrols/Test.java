package de.eru.pherufxcontrols;

import de.eru.pherufxcontrols.notifications.NotificationAlignment;
import de.eru.pherufxcontrols.notifications.Notifications;
import de.eru.pherufxcontrols.utils.NotificationType;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

/**
 *
 * @author Philipp Bruckner
 */
public class Test extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BooleanProperty dsa = new SimpleBooleanProperty(true);

        Notifications.setAlignment(NotificationAlignment.BOTTOM_RIGHT);
        Notifications.createInfoNotification()
                .setTitle("Warnung!")
                .setHeader("Warnung")
                .setText("Das ist eine Warnung. Das solltest du dir mal anschauen! Das könnte nämlich vielleicht doch ne ernste Sache sein! Also so vielleicht...")
                .setType(NotificationType.WARNING)
                .setTimer(3)
                .show();
        Notifications.createInfoNotification()
                .bindDontShowAgainProperty(dsa)
                .setHeader("Info")
                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
                .setTimer(6)
                .show();
        Notifications.createInfoNotification()
                .setDontShowAgain(true)
                .setHeader("Fehler!")
                .setText("Es ist ein schwerer Fehler aufgetreten!")
                .setType(NotificationType.ERROR)
                .setTimer(8)
                .show();
    }
}
