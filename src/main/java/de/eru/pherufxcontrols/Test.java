package de.eru.pherufxcontrols;

import de.eru.pherufxcontrols.dialogs.Dialogs;
import de.eru.pherufxcontrols.notifications.Notifications;
import de.eru.pherufxcontrols.utils.ConfirmType;
import de.eru.pherufxcontrols.utils.InfoType;
import javafx.application.Application;
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
//        int response = Dialogs.createConfirmDialog()
//                .setHeader("Löschen bestätigen")
//                .setText("Soll diese Datei wirklich gelöscht werden?")
//                .setType(ConfirmType.WARNING)
//                .setTitle("Datei löschen")
//                .showAndWait();
//        if (response == 1) {
//            response = Dialogs.createInfoDialog()
//                    .setType(InfoType.INFO)
//                    .setTitle("Datei wurde gelöscht")
//                    .setHeader("Datei wurde gelöscht")
//                    .setText("Die Datei wurde erfolgreich gelöscht.")
//                    .showAndWait();
//            if(response == 1){
//                System.out.println("OKAY!");
//            }else{
//                System.out.println("NICHT OKAY");
//            }
//        }
        Notifications.createInfoNotification().setHeader("HEader11").setText("Das ist text").show();
        Thread.sleep(2000);
        Notifications.createInfoNotification().setHeader("HEader22").setText("Das ist text").show();
    }
}
