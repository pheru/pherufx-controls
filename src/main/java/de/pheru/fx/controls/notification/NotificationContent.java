package de.pheru.fx.controls.notification;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * @author Philipp Bruckner
 */
public class NotificationContent {

    @FXML
    private GridPane root;
    @FXML
    private Label headerLabel;
    @FXML
    private Label textLabel;
    @FXML
    private ImageView imageView;

    public NotificationContent(Notification.Type type, String text, String header) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NotificationContent.class.getResource("notificationcontent.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();

            if (type != Notification.Type.NONE) {
                imageView.setImage(new Image(type.getImagePath()));
            } else {
                root.getChildren().remove(imageView);
            }
            textLabel.setText(text);
            headerLabel.setText(header);
            layout(header.isEmpty());
        } catch (IOException e) {
            throw new RuntimeException("TODO", e);
        }
    }

    public NotificationContent(Notification.Type type, StringProperty textProperty, StringProperty headerProperty) {
        this(type, textProperty.get(), headerProperty.get());
        textLabel.textProperty().bind(textProperty);
        headerLabel.textProperty().bind(headerProperty);
        headerProperty.addListener((observable, oldValue, newValue) -> {
            layout(newValue.isEmpty());
        });
    }

    public NotificationContent(Notification.Type type, StringProperty textProperty, String header) {
        this(type, textProperty.get(), header);
        textLabel.textProperty().bind(textProperty);
    }

    private void layout(boolean headerEmpty) {
        if (headerEmpty) {
            root.getChildren().remove(headerLabel);
            GridPane.setColumnIndex(textLabel, 1);
            GridPane.setRowIndex(imageView, 1);
        } else if (!root.getChildren().contains(headerLabel)) {
            GridPane.setRowIndex(imageView, 0);
            GridPane.setColumnIndex(textLabel, 0);
            root.getChildren().add(headerLabel);
        }
    }

    public Node getRoot() {
        return root;
    }
}
