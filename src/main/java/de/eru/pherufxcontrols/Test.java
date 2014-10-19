/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
                .showAndWait();
        if (response == 1) {
            Dialogs.createInfoDialog()
                    .setType(InfoType.WARNING)
                    .setHeader("Header!")
                    .setText("Das ist ein Text")
                    .show();
        }
    }
}
