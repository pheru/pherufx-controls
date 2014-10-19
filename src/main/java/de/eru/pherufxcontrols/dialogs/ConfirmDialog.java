package de.eru.pherufxcontrols.dialogs;

import de.eru.pherufxcontrols.utils.ConfirmType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

/**
 *
 * @author Philipp Bruckner
 */
public final class ConfirmDialog extends Dialog {
    
    private ConfirmType type;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setType(ConfirmType.QUESTION);
        setTitle("Bestätigen");
    }
    
    @FXML
    private void responseYes(){
        closeDialog(1);
    }
    @FXML
    private void responseNo(){
        closeDialog(0);
    }
    @FXML
    private void responseCancel(){
        closeDialog(-1);
    }
    
    private void closeDialog(int response){
        this.response = response;
        getRoot().getScene().getWindow().hide();
    }

    public ConfirmType getType() {
        return type;
    }

    public ConfirmDialog setType(ConfirmType type) {
        this.type = type;
        image.setImage(new Image(type.getImagePath()));
        return this;
    }
    
    @Override
    public ConfirmDialog setText(String text) {
        return (ConfirmDialog) super.setText(text);
    }

    @Override
    public ConfirmDialog setHeader(String header) {
        return (ConfirmDialog) super.setHeader(header);
    }

    @Override
    public ConfirmDialog setTitle(String title) {
        return (ConfirmDialog) super.setTitle(title);
    }
    
    @Override
    public ConfirmDialog bindHeaderProperty(ObservableValue<? extends String> observable) {
        return (ConfirmDialog) super.bindHeaderProperty(observable);
    }

    @Override
    public ConfirmDialog bindTextProperty(ObservableValue<? extends String> observable) {
        return (ConfirmDialog) super.bindTextProperty(observable);
    }

    @Override
    public ConfirmDialog bindTitleProperty(ObservableValue<? extends String> observable) {
        return (ConfirmDialog) super.bindTitleProperty(observable);
    }
}
