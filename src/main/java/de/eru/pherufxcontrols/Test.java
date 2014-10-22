package de.eru.pherufxcontrols;

import de.eru.pherufxcontrols.dialogs.Dialogs;
import de.eru.pherufxcontrols.notifications.Notifications;
import de.eru.pherufxcontrols.utils.ConfirmType;
import de.eru.pherufxcontrols.utils.InfoType;
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
        
        int response = Dialogs.createConfirmDialog()
                .setHeader("Löschen bestätigen")
                .setText("Soll diese Datei wirklich gelöscht werden?")
                .setType(ConfirmType.WARNING)
                .setTitle("Datei löschen")
                .showAndWait();
        if (response == 1) {
            response = Dialogs.createInfoDialog()
                    .setType(InfoType.INFO)
                    .setTitle("Datei wurde gelöscht")
                    .setHeader("Datei wurde gelöscht")
                    .setText("Die Datei wurde erfolgreich gelöscht.")
                    .showAndWait();
            if(response == 1){
                System.out.println("OKAY!");
            }else{
                System.out.println("NICHT OKAY");
            }
        }
        Notifications.createInfoNotification().setTitle("Warnung!").setHeader("Warnung").setText("Das ist eine Warnung. Das solltest du dir mal anschauen! Das könnte nämlich vielleicht doch ne ernste Sache sein! Also so vielleicht...").setType(InfoType.WARNING).setTimer(8).show();
        Notifications.createInfoNotification().bindDontShowAgainProperty(dsa).setHeader("Info").setText("Das ist hier bloß eine Information. Nicht sooooo wichtig...").show();
        Notifications.createInfoNotification().setDontShowAgain(true).setHeader("Fehler!").setText("Es ist ein schwerer Fehler aufgetreten!").setType(InfoType.ERROR).setTimer(6).show();
    }
}
