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

//    public static void main(String[] args) {
//        launch(args);
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BooleanProperty dsa = new SimpleBooleanProperty(true);

        Notifications.setDefaultTimer(10);
        Notifications.setAlignment(Notifications.Alignment.BOTTOM_RIGHT);
        VBox box = new VBox(new Label("Liar Liar"), new Label("Haven"), new Label("Kamelot"));
        VBox box2 = new VBox(new Label("Liar Liar"), new Label("Haven"), new Label("Kamelot"));
        Notifications.createCustomNotification(box).setTimer(14).setTitle("Aktueller Titel").setExitButtonVisible(true).show();
        Notifications.createCustomNotification(box2).setTimer(14).setTitle("Aktueller Titel").show();
//        Notifications.createNotification(Notification.Type.INFO).setText("Test").setTimer(15).show();
//        Notifications.createNotification(Notification.Type.INFO).setText("Test").bindDontShowAgainProperty(dsa).setTimer(12).show();
//        Notifications.createNotification(Notification.Type.INFO).setText("Test").setTimer(5).show();
        Notifications.createNotification(Notification.Type.INFO).setText("Test"
                + "Das ist ein sehr langer Text, nicht wahr? Das will ich aber so!"
                + "Deshalb bleibt der auch so lange!").setTimer(13).setExitButtonVisible(false).bindDontShowAgainProperty(dsa).show();
        Notifications.createNotification(Notification.Type.INFO).setText("Test"
                + "Das ist ein sehr langer Text, nicht wahr? Das will ich aber so!"
                + "Deshalb bleibt der auch so lange!").setTimer(13).setExitButtonVisible(false).show();
        Notifications.createNotification(Notification.Type.INFO).setText("Test"
                + "Das ist ein sehr langer Text, nicht wahr? Das will ich aber so!"
                + "Deshalb bleibt der auch so lange!").setTimer(13).show();
//        Notifications.createNotification(Notification.Type.INFO).setText("Test").setTimer(8).show();
//        Notifications.createNotification(Notification.Type.INFO).setText("Test").setTimer(9).show();
//        Notification n1 = Notifications.createNotification(Notification.Type.ERROR)
//                .setTitle("Error!")
//                .setHeader("Fehler!")
//                .setTimer(2)
//                .setText("Das ist ein Fehler. Das solltest du dir mal anschauen! Das könnte nämlich vielleicht doch ne ernste Sache sein! Also so vielleicht schreib ich mir mal noch viel mehr Text. Mich würde nämlich interessieren, wie viel Text in eine Notification passt.");
//        n1.hide();
//        Notifications.createNotification(Notification.Type.INFO)
//                .bindDontShowAgainProperty(dsa)
//                .setHeader("Info")
//                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
//                .show();
//        Notifications.createNotification(Notification.Type.INFO)
//                .bindDontShowAgainProperty(dsa)
//                .setHeader("Info")
//                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
//                .show();
//        Notifications.createNotification(Notification.Type.INFO)
//                .bindDontShowAgainProperty(dsa)
//                .setHeader("Info")
//                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
//                .show();
//        Notifications.createNotification(Notification.Type.INFO)
//                .bindDontShowAgainProperty(dsa)
//                .setHeader("Info")
//                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
//                .show();
//        Notifications.createNotification(Notification.Type.INFO)
//                .bindDontShowAgainProperty(dsa)
//                .setHeader("Info")
//                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
//                .show();
//        Notifications.createNotification(Notification.Type.INFO)
//                .bindDontShowAgainProperty(dsa)
//                .setHeader("Info")
//                .setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...")
//                .show();
//        Notifications.createNotification(Notification.Type.ERROR)
//                .setHeader("Fehler!")
//                .setText("Es ist ein schwerer Fehler aufgetreten!")
//                .show();
//        Notifications.createNotification(Notification.Type.INFO)
//                .bindDontShowAgainProperty(dsa)
//                .setHeader("Blubb!")
//                .setText("Es ist ein schwerer Fehler aufgetreten! Oder irgendwas anderes!")
//                .show();
//        Notifications.createNotification(Notification.Type.WARNING)
//                .bindDontShowAgainProperty(dsa)
//                .setHeader("Fehler!\nFehler!\nFehler!\n...Warnung?")
//                .setText("Es ist ein schwerer Fehler aufgetreten!\nZumindest gibt es eine Warnung dafür! Also ist doch was passiert, oder?")
//                .show();
//        Notifications.showNotification(Notification.Type.INFO, "Aktueller Titel:", ">Titel:\t\tThe Cage and the bibidy babidy buuuu\n>Album:\tEcliptica\n>Interpret:\tSonata Arctica", 0, null);
//        Notifications.createNotification(Notification.Type.INFO)
//                .setHeader("Aktueller Titel:")
//                //                .setText("The Cage and the bibidy babidy buuuu\nEcliptica\nSonata Arctica")
//                .setText("Titel:\t\tThe Cage and the bibidy babidy buuuu\nAlbum:\tEcliptica\nInterpret:\tSonata Arctica")
//                .setWrapText(false)
//                .bindDontShowAgainProperty(dsa)
//                .show();

    }
}
