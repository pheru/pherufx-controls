package de.eru.pherufxcontrols;

import de.eru.pherufxcontrols.dialogs.Dialogs;
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
        int response = Dialogs.createConfirmDialog()
                .setHeader("Neuer Header")
                .setText("neuer Text! WIUHUUU!°")
                .setType(ConfirmType.QUESTION)
                .setTitle("Title!")
                .showAndWait();
        if (response == 1) {
            response = Dialogs.createInfoDialog()
                    .setType(InfoType.WARNING)
                    .setHeader("Header!")
                    .setText("Das ist ein Text")
                    .showAndWait();
            if(response == 1){
                System.out.println("OKAY!");
            }else{
                System.out.println("NICHT OKAY");
            }
        }
    }
}
