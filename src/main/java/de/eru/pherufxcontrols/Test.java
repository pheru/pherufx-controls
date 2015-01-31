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
        Notifications.createInfoNotification(NotificationType.WARNING)
                .setTitle("Warnung!")
                .setHeader("Warnung")
                .setText("Das ist eine Warnung. Das solltest du dir mal anschauen! Das könnte nämlich vielleicht doch ne ernste Sache sein! Also so vielleicht schreib ich mir mal noch viel mehr Text. Mich würde nämlich interessieren, wie viel Text in eine Notification passt.")
                .show();
        Notifications.createInfoNotification(NotificationType.INFO)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Info")
                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
                .setTimer(6)
                .show();
        Notifications.createInfoNotification(NotificationType.ERROR)
                .setDontShowAgain(true)
                .setHeader("Fehler!")
                .setText("Es ist ein schwerer Fehler aufgetreten!")
                .setTimer(7)
                .show();
        Notifications.createInfoNotification(NotificationType.INFO)
                .setDontShowAgain(true)
                .setHeader("Blubb!")
                .setText("Es ist ein schwerer Fehler aufgetreten! Oder irgendwas anderes!")
                .setTimer(6)
                .show();
        Notifications.createInfoNotification(NotificationType.WARNING)
                .setDontShowAgain(false)
                .setHeader("Fehler!\nFehler!\nFehler!\n...Warnung?")
                .setText("Es ist ein schwerer Fehler aufgetreten!\nZumindest gibt es eine Warnung dafür! Also ist doch was passiert, oder?")
                .setTimer(6)
                .show();
    }
}
