package de.pheru.fx.controls.notificationbar;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Philipp on 16.03.2016.
 */
public class NotificationBar extends VBox implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private VBox currentNotificationBox;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button closeButton;

    private final ObservableList<Node> notificationNodes = FXCollections.observableArrayList();
    private final IntegerProperty currentNotificationIndex = new SimpleIntegerProperty(0);

    public NotificationBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(NotificationBar.class.getResource("notificationbar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Rectangle r = new Rectangle();
        r.widthProperty().bind(widthProperty());
        r.heightProperty().bind(heightProperty());
        content.setClip(r);

        setMinMaxHeight(0.0, false);

        widthProperty().addListener((observable, oldValue, newValue) -> {
            setMinMaxHeight(computeContentHeight(), true);
        });

        notificationNodes.addListener((ListChangeListener<Node>) c -> {
            if (notificationNodes.isEmpty()) {
                setMinMaxHeight(0.0, true);
            } else {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        if (currentNotificationIndex.get() >= notificationNodes.size()) {
                            currentNotificationIndex.set(notificationNodes.size() - 1);
                        } else {
                            currentNotificationBox.getChildren().setAll(notificationNodes.get(currentNotificationIndex.get()));
                        }
                        if (notificationNodes.isEmpty()) {
                            setMinMaxHeight(0.0, true);
                        } else {
                            setMinMaxHeight(computeContentHeight(), true);
                        }
                    } else if (c.wasAdded()) {
                        if (notificationNodes.size() == c.getAddedSize()) {
                            currentNotificationBox.getChildren().setAll(notificationNodes.get(0));
                            setMinMaxHeight(computeContentHeight(), true);
                        }
                    }
                }
            }
        });
        currentNotificationIndex.addListener((observable, oldValue, newValue) -> {
            currentNotificationBox.getChildren().setAll(notificationNodes.get(newValue.intValue()));
            setMinMaxHeight(computeContentHeight(), true);
        });
        previousButton.disableProperty().bind(currentNotificationIndex.isEqualTo(0));
        nextButton.disableProperty().bind(currentNotificationIndex.isEqualTo(Bindings.size(notificationNodes).subtract(1)));
    }

    private void setMinMaxHeight(double value, boolean animate) {
        if (animate) {
            new Timeline(new KeyFrame(Duration.millis(200),
                    new KeyValue(minHeightProperty(), value),
                    new KeyValue(maxHeightProperty(), value))).play();
        } else {
            setMinHeight(value);
            setMaxHeight(value);
        }
    }

    @FXML
    private void next() {
        currentNotificationIndex.set(currentNotificationIndex.get() + 1);
    }

    @FXML
    private void previous() {
        currentNotificationIndex.set(currentNotificationIndex.get() - 1);
    }

    @FXML
    private void close() {
        notificationNodes.remove(currentNotificationIndex.get());
    }

    public ObservableList<Node> getNotificationNodes() {
        return notificationNodes;
    }

    private double computeContentHeight() {
        if (currentNotificationBox.getChildren().isEmpty()) {
            return 0.0;
        }
        Node contentNode = currentNotificationBox.getChildren().get(0);
        Group root = new Group();
        Scene scene = new Scene(root);
        root.getChildren().add(contentNode);
        root.applyCss();
        root.layout();

        double height = contentNode.prefHeight(currentNotificationBox.getWidth());

        currentNotificationBox.getChildren().setAll(contentNode);

        double buttonHeight = Math.max(nextButton.getHeight(), closeButton.getHeight());
        return Math.max(buttonHeight, height);
    }
}
