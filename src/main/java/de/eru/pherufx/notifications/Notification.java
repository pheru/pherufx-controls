package de.eru.pherufx.notifications;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Philipp Bruckner
 */
public class Notification extends AbstractNotification {

    public enum Type {

        INFO("img/Info.png"),
        WARNING("img/Warning.png"),
        ERROR("img/Error.png");

        private final String imagePath;

        private Type(final String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    @FXML
    private Label headerLabel;
    @FXML
    private Label textLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private ImageView image;
    @FXML
    private CheckBox dontShowAgainBox;
    private Type type;

    protected Notification(){
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timerLabel.textProperty().bind(timer.asString());
        dontShowAgainBox.setVisible(false);
    }

    @FXML
    private void closeNotification() {
        hide();
    }
    
    public Notification setWrapText(boolean wrapText){
        textLabel.setWrapText(wrapText);
        return this;
    }
    
    public Type getType() {
        return type;
    }

    protected Notification setType(Type type) {
        this.type = type;
        image.setImage(new Image(type.getImagePath()));
        return this;
    }

    public String getHeader() {
        return headerLabel.getText();
    }

    public Notification setHeader(String header) {
        headerLabel.setText(header);
        return this;
    }

    public String getText() {
        return textLabel.getText();
    }

    public Notification setText(String text) {
        textLabel.setText(text);
        return this;
    }

    public boolean isDontShowAgain() {
        return dontShowAgainBox.isSelected();
    }

    public Notification bindHeaderProperty(Property<String> property) {
        headerLabel.textProperty().bindBidirectional(property);
        return this;
    }

    public Notification bindTextProperty(Property<String> property) {
        textLabel.textProperty().bindBidirectional(property);
        return this;
    }

    @Override
    public Notification setTimer(int timer) {
        return (Notification) super.setTimer(timer);
    }

    public Notification bindDontShowAgainProperty(Property<Boolean> property) {
        dontShowAgainBox.selectedProperty().bindBidirectional(property);
        dontShowAgainBox.setVisible(true);
        return this;
    }

    public Image getImage() {
        return image.getImage();
    }

    @Override
    public Notification setTitle(String title) {
        return (Notification) super.setTitle(title);
    }

}
