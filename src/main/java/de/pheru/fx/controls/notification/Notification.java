package de.pheru.fx.controls.notification;

import javafx.beans.property.Property;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Philipp Bruckner
 */
public class Notification extends CustomNotification {

    @FXML
    private Label headerLabel;
    @FXML
    private Label textLabel;
    @FXML
    private ImageView image;
    private Type type;

    protected Notification() {
    }

    public Notification setWrapText(boolean wrapText) {
        textLabel.setWrapText(wrapText);
        return this;
    }

    public Type getType() {
        return type;
    }

    protected Notification setType(Type type) {
        this.type = type;
        image.setImage(new Image(type.getImagePath()));
        headerLabel.textProperty().set(type.getText());
        setTitle(type.getText());
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

    public Notification bindHeaderProperty(Property<String> property) {
        headerLabel.textProperty().bindBidirectional(property);
        return this;
    }

    public Notification bindTextProperty(Property<String> property) {
        textLabel.textProperty().bindBidirectional(property);
        return this;
    }

    public Image getImage() {
        return image.getImage();
    }

    @Override
    public Notification setExitButtonVisible(boolean exitButtonVisible) {
        return (Notification) super.setExitButtonVisible(exitButtonVisible);
    }

    @Override
    public Notification bindDontShowAgainProperty(Property<Boolean> property) {
        return (Notification) super.bindDontShowAgainProperty(property);
    }

    @Override
    public Notification hideOnMouseClicked() {
        return (Notification) super.hideOnMouseClicked(); 
    }

    @Override
    public Notification setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        return (Notification) super.setOnMouseClicked(value);
    }

    @Override
    public Notification setTitle(String title) {
        return (Notification) super.setTitle(title);
    }

    @Override
    public Notification setTimer(int timer) {
        return (Notification) super.setTimer(timer);
    }

    public enum Type {

        INFO("Information", "img/Info.png"),
        WARNING("Warnung", "img/Warning.png"),
        ERROR("Fehler", "img/Error.png");

        private final String text;
        private final String imagePath;

        private Type(final String text, final String imagePath) {
            this.text = text;
            this.imagePath = imagePath;
        }

        public String getText() {
            return text;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

}
