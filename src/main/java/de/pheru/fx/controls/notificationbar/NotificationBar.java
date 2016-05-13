package de.pheru.fx.controls.notificationbar;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    public static final String STYLE_CLASS = "notificationbar";

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

    private final ObservableList<NotificationNode> notificationNodes = FXCollections.observableArrayList();
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

        currentNotificationBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            setMinMaxHeight(computeContentHeight(), true);
        });

        notificationNodes.addListener((ListChangeListener<NotificationNode>) c -> {
            if (notificationNodes.isEmpty()) {
                currentNotificationBox.getChildren().clear();
                setMinMaxHeight(0.0, true);
            } else {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        if (currentNotificationIndex.get() >= notificationNodes.size()) {
                            currentNotificationIndex.set(notificationNodes.size() - 1);
                        } else {
                            setCurrentNotificationNode(notificationNodes.get(currentNotificationIndex.get()));
                        }
                    } else if (c.wasAdded()) {
                        if (notificationNodes.size() == c.getAddedSize()) {
                            setCurrentNotificationNode(notificationNodes.get(0));
                        }
                    }
                }
            }
        });
        currentNotificationIndex.addListener((observable, oldValue, newValue) -> {
            setCurrentNotificationNode(notificationNodes.get(newValue.intValue()));
        });
        previousButton.visibleProperty().bind(currentNotificationIndex.greaterThan(0));
        previousButton.managedProperty().bind(currentNotificationIndex.greaterThan(0));
        nextButton.visibleProperty().bind(currentNotificationIndex.lessThan(Bindings.size(notificationNodes).subtract(1)));
        nextButton.managedProperty().bind(currentNotificationIndex.lessThan(Bindings.size(notificationNodes).subtract(1))
                .or(previousButton.managedProperty()));
    }

    private void setCurrentNotificationNode(NotificationNode node) {
        currentNotificationBox.getChildren().setAll(node.getNode());
        setMinMaxHeight(computeContentHeight(), true);
        if (node.getType() == Type.NONE) {
            getStyleClass().setAll(STYLE_CLASS);
        } else {
            getStyleClass().setAll(STYLE_CLASS, node.getType().getStyleClass());
        }
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

    public void addNotification(Type type, String message) {
        Label label = createMessageLabel();
        label.setText(message);
        notificationNodes.add(new NotificationNode(label, type));
    }

    public void addNotification(Type type, StringProperty message) {
        Label label = createMessageLabel();
        label.textProperty().bind(message);
        notificationNodes.add(new NotificationNode(label, type));
    }

    private Label createMessageLabel() {
        Label label = new Label();
        label.textProperty().addListener((observable, oldValue, newValue) -> {
            setMinMaxHeight(computeContentHeight(), true);
        });
        label.setWrapText(true);
        label.setAlignment(Pos.TOP_LEFT);
        return label;
    }

    public void addNotification(Type type, Node node) {
        notificationNodes.add(new NotificationNode(node, type));
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

    private class NotificationNode {
        private final Node node;
        private final Type type;

        public NotificationNode(Node node, Type type) {
            this.node = node;
            this.type = type;
        }

        public Node getNode() {
            return node;
        }

        public Type getType() {
            return type;
        }
    }

    public enum Type {
        INFO("img/Info.png", "info"),
        WARNING("img/Warning.png", "warning"),
        ERROR("img/Error.png", "error"),
        NONE("", "");

        private final String imagePath;
        private final String styleClass;

        Type(final String imagePath, String styleClass) {
            this.imagePath = imagePath;
            this.styleClass = styleClass;
        }

        protected String getImagePath() {
            return imagePath;
        }

        protected String getStyleClass() {
            return styleClass;
        }
    }
}
