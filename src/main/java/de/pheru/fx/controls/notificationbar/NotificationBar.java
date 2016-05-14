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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ImageView imageView;
    @FXML
    private VBox currentNotificationBox;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button closeButton;

    private final ObservableList<Element> elements = FXCollections.observableArrayList();
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);

    private boolean animate = true;
    private boolean showTypeIcon = true;

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

        currentNotificationBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            setMinMaxHeight(computeContentHeight(), animate);
        });

        elements.addListener((ListChangeListener<Element>) c -> {
            if (elements.isEmpty()) {
                currentNotificationBox.getChildren().clear();
                setMinMaxHeight(0.0, animate);
            } else {
                while (c.next()) {
                    if (c.wasRemoved() && c.getFrom() == currentIndex.get()) {
                        if (currentIndex.get() >= elements.size()) {
                            currentIndex.set(elements.size() - 1);
                        } else {
                            setCurrentNotificationNode(elements.get(currentIndex.get()));
                        }
                    } else if (c.wasAdded()) {
                        if (elements.size() == c.getAddedSize()) {
                            setCurrentNotificationNode(elements.get(0));
                        }
                    }
                }
            }
        });
        currentIndex.addListener((observable, oldValue, newValue) -> {
            setCurrentNotificationNode(elements.get(newValue.intValue()));
        });
        previousButton.visibleProperty().bind(currentIndex.greaterThan(0));
        previousButton.managedProperty().bind(currentIndex.greaterThan(0));
        nextButton.visibleProperty().bind(currentIndex.lessThan(Bindings.size(elements).subtract(1)));
        nextButton.managedProperty().bind(currentIndex.lessThan(Bindings.size(elements).subtract(1))
                .or(previousButton.managedProperty()));
    }

    private void setCurrentNotificationNode(Element node) {
        currentNotificationBox.getChildren().setAll(node.getNode());
        setMinMaxHeight(computeContentHeight(), animate);
        if (node.getType() == Type.NONE) {
            getStyleClass().setAll(STYLE_CLASS);
            imageView.setManaged(false);
            imageView.setVisible(false);
        } else {
            getStyleClass().setAll(STYLE_CLASS, node.getType().getStyleClass());
            imageView.setImage(new Image(node.getType().getImagePath()));
            imageView.setManaged(showTypeIcon);
            imageView.setVisible(showTypeIcon);
        }
    }

    private void setMinMaxHeight(double value, boolean animate) {
        if (animate) {
            new Timeline(new KeyFrame(Duration.millis(200),
                    new KeyValue(content.minHeightProperty(), value),
                    new KeyValue(content.maxHeightProperty(), value),
                    new KeyValue(minHeightProperty(), value),
                    new KeyValue(maxHeightProperty(), value))).play();
        } else {
            setMinHeight(value);
            setMaxHeight(value);
            content.setMinHeight(value);
            content.setMaxHeight(value);
        }
    }

    @FXML
    private void next() {
        currentIndex.set(currentIndex.get() + 1);
    }

    @FXML
    private void previous() {
        currentIndex.set(currentIndex.get() - 1);
    }

    @FXML
    private void close() {
        elements.remove(currentIndex.get());
    }

    public Element addElement(Type type, String message) {
        Label label = createMessageLabel();
        label.setText(message);
        return addElement(type, label);

    }

    public Element addElement(Type type, StringProperty message) {
        Label label = createMessageLabel();
        label.textProperty().bind(message);
        label.textProperty().addListener((observable, oldValue, newValue) -> {
            setMinMaxHeight(computeContentHeight(), animate);
        });
        return addElement(type, label);
    }

    public Element addElement(Type type, Node node) {
        Element element = new Element(type, node);
        addElement(element);
        return element;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    private Label createMessageLabel() {
        Label label = new Label();
        label.setWrapText(true);
        label.setAlignment(Pos.TOP_LEFT);
        return label;
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

        double prefHeight = contentNode.prefHeight(currentNotificationBox.getWidth());

        currentNotificationBox.getChildren().setAll(contentNode);

        double minHeight = closeButton.getHeight();
        if(imageView.isVisible()){
            minHeight = Math.max(imageView.getFitHeight(), minHeight);
        }
        return Math.max(minHeight, prefHeight);
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public boolean isShowTypeIcon() {
        return showTypeIcon;
    }

    public void setShowTypeIcon(boolean showTypeIcon) {
        this.showTypeIcon = showTypeIcon;
    }

    public class Element {
        private final Node node;
        private final Type type;

        public Element(Type type, Node node) {
            this.node = node;
            this.type = type;
        }

        private Node getNode() {
            return node;
        }

        private Type getType() {
            return type;
        }

        public void close() {
            elements.remove(this);
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
