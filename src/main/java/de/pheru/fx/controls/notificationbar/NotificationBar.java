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

/**
 * Created by Philipp on 16.03.2016.
 */
public class NotificationBar extends VBox {

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
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        setUpClip();
        setUpBindingsAndListeners();
    }

    private void setUpClip() {
        Rectangle r = new Rectangle();
        r.widthProperty().bind(widthProperty());
        r.heightProperty().bind(heightProperty());
        content.setClip(r);
    }

    private void setUpBindingsAndListeners() {
        currentNotificationBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            resizeToContent();
        });
        currentIndex.addListener((observable, oldValue, newValue) -> {
            setCurrentNotificationNode(elements.get(newValue.intValue()));
        });
        elements.addListener(new ElementsListChangeLister());

        previousButton.visibleProperty().bind(currentIndex.greaterThan(0));
        previousButton.managedProperty().bind(currentIndex.greaterThan(0));
        nextButton.visibleProperty().bind(currentIndex.lessThan(Bindings.size(elements).subtract(1)));
        nextButton.managedProperty().bind(currentIndex.lessThan(Bindings.size(elements).subtract(1))
                .or(previousButton.managedProperty()));
    }

    @FXML
    public void next() {
        if (currentIndex.get() < elements.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
        }
    }

    @FXML
    public void previous() {
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
        }
    }

    @FXML
    public void close() {
        if (currentIndex.get() < elements.size()) {
            elements.remove(currentIndex.get());
        }
    }

    public Element addElement(Type type, String message) {
        Label label = createMessageLabel();
        label.setText(message);
        return addElement(type, label);

    }

    //TODO Issue #42 Automatische Anpassung bei Inhalts-Resize
//    public Element addElement(Type type, StringProperty message) {
//        Label label = createMessageLabel();
//        label.textProperty().bind(message);
//        label.textProperty().addListener((observable, oldValue, newValue) -> {
//            resizeToContent();
//        });
//        return addElement(type, label);
//    }

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

    public void resizeToContent() {
        setMinMaxHeight(computeContentHeight(), animate);
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

    private double computeContentHeight() { //TODO Issue #43 Flackern bei resize
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
        if (imageView.isVisible()) {
            minHeight = Math.max(imageView.getFitHeight(), minHeight);
        }
        return Math.max(minHeight, prefHeight);
    }

    private void setCurrentNotificationNode(Element element) {
        currentNotificationBox.getChildren().setAll(element.getNode());
        if (element.getType() == Type.NONE) {
            getStyleClass().setAll(STYLE_CLASS);
            imageView.setManaged(false);
            imageView.setVisible(false);
        } else {
            getStyleClass().setAll(STYLE_CLASS, element.getType().getStyleClass());
            imageView.setImage(new Image(element.getType().getImagePath()));
            imageView.setManaged(showTypeIcon);
            imageView.setVisible(showTypeIcon);
        }
        resizeToContent();
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

    private class ElementsListChangeLister implements ListChangeListener<Element> {

        @Override
        public void onChanged(Change<? extends Element> c) {
            if (elements.isEmpty()) {
                currentNotificationBox.getChildren().clear();
                setMinMaxHeight(0.0, animate);
            } else {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        if (c.getFrom() == currentIndex.get()) {
                            if (currentIndex.get() >= elements.size()) {
                                currentIndex.set(elements.size() - 1);
                            } else {
                                setCurrentNotificationNode(elements.get(currentIndex.get()));
                            }
                        } else if (c.getFrom() < currentIndex.get()) {
                            currentIndex.set(currentIndex.get() - 1);
                        }
                    } else if (c.wasAdded()) {
                        if (elements.size() == c.getAddedSize()) {
                            setCurrentNotificationNode(elements.get(0));
                        }
                    }
                }
            }
        }
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
