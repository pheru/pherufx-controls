package de.eru.pherufxcontrols.notifications;

import de.eru.pherufxcontrols.utils.InfoType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Philipp Bruckner
 */
public class InfoNotification extends Notification {

    @FXML
    private Label headerLabel;
    @FXML
    private Label textLabel;
    @FXML
    private ImageView image;
    @FXML
    private CheckBox dontShowAgainBox;
    private InfoType type;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setType(InfoType.INFO);
    }

    public InfoType getType() {
        return type;
    }

    public InfoNotification setType(InfoType type) {
        this.type = type;
        image.setImage(new Image(type.getImagePath()));
        return this;
    }

    public String getHeader() {
        return headerLabel.getText();
    }

    public InfoNotification setHeader(String header) {
        headerLabel.setText(header);
        return this;
    }

    public String getText() {
        return textLabel.getText();
    }

    public InfoNotification setText(String text) {
        textLabel.setText(text);
        return this;
    }
    
    public boolean isDontShowAgain() {
        return dontShowAgainBox.isSelected();
    }

    public InfoNotification setDontShowAgain(boolean dontShowAgain) {
        dontShowAgainBox.setSelected(dontShowAgain);
        return this;
    }

    public InfoNotification bindHeaderProperty(Property<String> property) {
        headerLabel.textProperty().bindBidirectional(property);
        return this;
    }

    public InfoNotification bindTextProperty(Property<String> property) {
        textLabel.textProperty().bindBidirectional(property);
        return this;
    }

    @Override
    public InfoNotification setTimer(Integer timer) {
        return (InfoNotification) super.setTimer(timer);
    }

    public InfoNotification bindDontShowAgainProperty(Property<Boolean> property) {
        dontShowAgainBox.selectedProperty().bindBidirectional(property);
        return this;
    }
    
    public Image getImage(){
        return image.getImage();
    }

    @Override
    public InfoNotification setTitle(String title) {
        return (InfoNotification) super.setTitle(title);
    }
 
    
    
}
