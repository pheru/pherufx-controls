package de.eru.pherufx.notifications;

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
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BooleanProperty dsa = new SimpleBooleanProperty(true);

        Notifications.setAlignment(Notifications.Alignment.BOTTOM_RIGHT);
        Notifications.createInfoNotification(InfoNotification.Type.ERROR)
                .setTitle("Error!")
                .setHeader("Fehler!")
                .setText("Das ist ein Fehler. Das solltest du dir mal anschauen! Das könnte nämlich vielleicht doch ne ernste Sache sein! Also so vielleicht schreib ich mir mal noch viel mehr Text. Mich würde nämlich interessieren, wie viel Text in eine Notification passt.")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.INFO)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Info")
                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.INFO)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Info")
                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.INFO)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Info")
                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.INFO)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Info")
                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.INFO)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Info")
                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.INFO)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Info")
                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.ERROR)
                .setHeader("Fehler!")
                .setText("Es ist ein schwerer Fehler aufgetreten!")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.INFO)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Blubb!")
                .setText("Es ist ein schwerer Fehler aufgetreten! Oder irgendwas anderes!")
                .show();
        Notifications.createInfoNotification(InfoNotification.Type.WARNING)
                .bindDontShowAgainProperty(dsa)
                .setHeader("Fehler!\nFehler!\nFehler!\n...Warnung?")
                .setText("Es ist ein schwerer Fehler aufgetreten!\nZumindest gibt es eine Warnung dafür! Also ist doch was passiert, oder?")
                .show();
    }
}
