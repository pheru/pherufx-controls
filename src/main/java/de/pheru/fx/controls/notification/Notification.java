package de.pheru.fx.controls.notification;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author Philipp Bruckner
 */
public class Notification extends CustomNotification {

    @FXML
    private GridPane contentRoot;
    @FXML
    private Label headerLabel;
    @FXML
    private Label textLabel;
    @FXML
    private ImageView image;
    private Type type;

    public Notification(Type type) {
        loadFXML();
        this.type = type;
        image.setImage(new Image(type.getImagePath()));
        headerLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            layout(newValue.isEmpty());
        });
        root.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Notifications.arrangeNotifications(true);
            }
        });
    }

    private void loadFXML() {
        try {
            FXMLLoader notificationFxmlLoader = new FXMLLoader(Notifications.class.getResource("notification.fxml"));
            notificationFxmlLoader.setController(this);
            notificationFxmlLoader.load();

            FXMLLoader contentFxmlLoader = new FXMLLoader(Notifications.class.getResource("content.fxml"));
            contentFxmlLoader.setController(this);
            contentFxmlLoader.load();

            setContent(contentFxmlLoader.getRoot());
        } catch (IOException e) {
            throw new RuntimeException("TODO", e);
        }
    }

    private void layout(boolean headerEmpty) {
        if (headerEmpty) {
            contentRoot.getChildren().remove(headerLabel);
            GridPane.setColumnIndex(textLabel, 1);
            GridPane.setRowIndex(image, 1);
        } else if (!contentRoot.getChildren().contains(headerLabel)) {
            GridPane.setRowIndex(image, 0);
            GridPane.setColumnIndex(textLabel, 0);
            contentRoot.getChildren().add(headerLabel);
        }
    }

    public Notification setWrapText(boolean wrapText) {
        textLabel.setWrapText(wrapText);
        return this;
    }

    public Type getType() {
        return type;
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
    public Notification hideOnMouseClicked(boolean fadeOut) {
        return (Notification) super.hideOnMouseClicked(fadeOut);
    }

    @Override
    public Notification setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        return (Notification) super.setOnMouseClicked(value);
    }

    @Override
    public Notification setDuration(Duration timer) {
        return (Notification) super.setDuration(timer);
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
