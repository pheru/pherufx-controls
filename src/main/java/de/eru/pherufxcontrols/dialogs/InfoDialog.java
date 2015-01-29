package de.eru.pherufxcontrols.dialogs;

import de.eru.pherufxcontrols.utils.InfoType;
import de.eru.pherufxcontrols.utils.Response;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Philipp Bruckner
 */
public final class InfoDialog extends Dialog{

    private InfoType type;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setType(InfoType.INFO);
        setTitle("Information");
    }
    
    public void show() {
        Stage stage = initStage();
        stage.show();
    }
    
    @FXML
    private void responseOK(){
        closeDialog(Response.OK);
    }

    public InfoType getType() {
        return type;
    }

    public InfoDialog setType(InfoType type) {
        this.type = type;
        imageView.setImage(new Image(type.getImagePath()));
        return this;
    }

    @Override
    public InfoDialog setText(String text) {
        return (InfoDialog) super.setText(text);
    }

    @Override
    public InfoDialog setHeader(String header) {
        return (InfoDialog) super.setHeader(header);
    }

    @Override
    public InfoDialog setTitle(String title) {
        return (InfoDialog) super.setTitle(title);
    }

    @Override
    public InfoDialog bindBidirectionalHeaderProperty(Property<String> property) {
        return (InfoDialog) super.bindBidirectionalHeaderProperty(property);
    }

    @Override
    public InfoDialog bindBidirectionalTextProperty(Property<String> property) {
        return (InfoDialog) super.bindBidirectionalTextProperty(property);
    }

    @Override
    public InfoDialog bindTitleProperty(ObservableValue<? extends String> observable) {
        return (InfoDialog) super.bindTitleProperty(observable);
    }
}
