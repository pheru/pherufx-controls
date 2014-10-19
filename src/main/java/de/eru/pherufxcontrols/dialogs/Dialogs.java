package de.eru.pherufxcontrols.dialogs;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author Philipp Bruckner
 */
public final class Dialogs {

    private Dialogs() {
    }

    public static InfoDialog createInfoDialog() {
        return (InfoDialog) getLoadedDialog("info");
    }

    public static ConfirmDialog createConfirmDialog() {
        return (ConfirmDialog) getLoadedDialog("confirm");
    }

    private static Dialog getLoadedDialog(String fxmlName) {
        URL resource = Dialogs.class.getResource(fxmlName + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            fxmlLoader.load();
            Dialog dialog = fxmlLoader.getController();
            dialog.setRoot(fxmlLoader.getRoot());
            return dialog;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
