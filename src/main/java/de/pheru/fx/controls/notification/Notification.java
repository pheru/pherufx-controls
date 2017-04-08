package de.pheru.fx.controls.notification;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author Philipp Bruckner
 */
public class Notification extends NotificationProperties {

    private static NotificationDefaults defaults = new NotificationDefaults();

    @FXML
    private StackPane root;
    @FXML
    private VBox notificationBox;
    @FXML
    private Label headerLabel;
    @FXML
    private HBox contentBox;
    @FXML
    private CheckBox dontShowAgainBox;
    @FXML
    private Button closeButton;

    private final EventHandler<MouseEvent> hideEventHandler = event -> {
        if (isClosable()) {
            hide();
        }
    };

    private Popup popup;
    private final Timeline durationTimeline = new Timeline();
    private Timeline xTimeline;
    private Timeline yTimeline;
    private Type type;

    private Notification(final Type type) {
        super(defaults);
        this.type = type;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(NotificationManager.class.getResource("notification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
            getStylesheets().addAll(defaults.getStylesheets());
        } catch (IOException e) {
            throw new IllegalStateException("Could not load fxml!", e);
        }

        headerLabel.visibleProperty().bind(headerLabel.textProperty().isEmpty().not());
        headerLabel.managedProperty().bind(headerLabel.textProperty().isEmpty().not());

        durationProperty().addListener((observable, oldValue, newValue) -> durationTimeline.playFromStart());
        root.setOnMouseEntered(event -> {
            durationTimeline.stop();
            closeButton.visibleProperty().bind(closableProperty());
        });
        root.setOnMouseExited(event -> {
            durationTimeline.play();
            closeButton.visibleProperty().unbind();
            closeButton.setVisible(false);
        });
        if (isHideOnMouseClicked()) {
            root.setOnMouseClicked(hideEventHandler);
        }
        hideOnMouseClickedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (root.getOnMouseClicked() == null) {
                    root.setOnMouseClicked(hideEventHandler);
                }
            } else {
                if (root.getOnMouseClicked() == hideEventHandler) {
                    root.setOnMouseClicked(null);
                }
            }
        });
    }

    private ImageView createHeaderImageView(final Type type) {
        final ImageView iv = new ImageView(type.getImagePath());
        iv.setFitHeight(24);
        iv.setFitWidth(24);
        return iv;
    }

    public Notification(final Type type, final Node content) {
        this(type);
        contentBox.getChildren().add(content);
    }

    public Notification(final Type type, final String text) {
        this(type);
        final Label label = getContentLabel();
        label.setText(text);
        contentBox.getChildren().add(label);
    }

    public Notification(final Type type, final StringProperty textProperty) {
        this(type);
        final Label label = getContentLabel();
        label.textProperty().bind(textProperty);
        contentBox.getChildren().add(label);
    }

    private Label getContentLabel() {
        final Label label = new Label();
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        return label;
    }

    public void show() {
        if (type != Type.NONE) {
            if (getStyleByType().isApplyStyle()) {
                root.getStyleClass().add(type.getStyleClass());
            }
            if (getStyleByType().isShowIcon()) {
                headerLabel.setGraphic(createHeaderImageView(type));
            }
        }
        root.getStylesheets().addAll(getStyleSheets());
        getNotificationManagerInstance().show(isAnimateShow(), this);
        if (getDuration() != Duration.INDEFINITE) {
            durationTimeline.getKeyFrames().clear();
            durationTimeline.getKeyFrames().add(new KeyFrame(getDuration(), (ActionEvent event) -> hide()));
            durationTimeline.playFromStart();
        }
    }

    @FXML
    private void closeNotification() {
        hide();
    }

    public void hide() {
        durationTimeline.stop();
        if (isFadeOut()) {
            final FadeTransition fadeTransition = new FadeTransition(getFadeOutDuration(), root);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished((ActionEvent event) -> hidePopup());
            fadeTransition.play();
        } else {
            hidePopup();
        }
    }

    private void hidePopup() {
        if (root.getScene() != null) {
            popup.hide();
        }
    }

    public static void hideAll(final Screen screen) {
        NotificationManager.getInstanceForScreen(screen).hideAll();
    }

    public static void hideAll(final Window owner) {
        NotificationManager.getInstanceForWindow(owner).hideAll();
    }

    protected Popup getPopup() {
        if (popup == null) {
            popup = new Popup();
            popup.setAutoFix(false);
            popup.getContent().add(root);
            popup.setOnHidden((WindowEvent event) -> getNotificationManagerInstance().removeNotification(this));
        }
        return popup;
    }

    private NotificationManager getNotificationManagerInstance() {
        if (getWindow() != null) {
            return NotificationManager.getInstanceForWindow(getWindow());
        } else {
            return NotificationManager.getInstanceForScreen(getScreen());
        }
    }

    protected void setY(final double position, boolean animated) {
        if (!animated) {
            popup.setY(position);
        } else {
            final DoubleProperty popupY = new SimpleDoubleProperty(popup.getY());
            popupY.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                popup.setY(newValue.doubleValue());
            });
            if (yTimeline != null) {
                yTimeline.stop();
            }
            yTimeline = new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(popupY, position)));
            yTimeline.play();
        }
    }

    protected void setX(final double position, final boolean animated) {
        if (!animated) {
            popup.setX(position);
        } else {
            final DoubleProperty popupX = new SimpleDoubleProperty(popup.getX());
            popupX.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                popup.setX(newValue.doubleValue());
            });
            if (xTimeline != null) {
                xTimeline.stop();
            }
            xTimeline = new Timeline(new KeyFrame(Duration.millis(200.0), new KeyValue(popupX, position)));
            xTimeline.play();
        }
    }

    public void setOnMouseClicked(final EventHandler<? super MouseEvent> eventHandler) {
        root.setOnMouseClicked(event -> {
            eventHandler.handle(event);
            if (isHideOnMouseClicked()) {
                hideEventHandler.handle(event);
            }
        });
    }

    public void bindDontShowAgainProperty(final Property<Boolean> property) { //TODO Durch getter&setter ersetzen?
        dontShowAgainBox.selectedProperty().bindBidirectional(property);
        dontShowAgainBox.setVisible(true);
        dontShowAgainBox.setManaged(true);
    }

    public String getHeaderText() {
        return headerLabel.textProperty().get();
    }

    public StringProperty headerTextProperty() {
        return headerLabel.textProperty();
    }

    public void setHeaderText(final String headerText) {
        headerLabel.setText(headerText);
    }

    protected StackPane getRoot() {
        return root;
    }

    public ObservableList<String> getStylesheets() {
        return root.getStylesheets();
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return durationTimeline.currentTimeProperty();
    }

    public Duration getCurrentTime() {
        return durationTimeline.getCurrentTime();
    }

    @Override
    public void setPosition(Pos position) {
        // Die Position wird vom NotificationManager gebraucht und
        // darf daher nicht geaendert werden, solange die Notification sichtbar ist
        if (popup != null && popup.isShowing()) {
            throw new IllegalStateException("The position can not be changed while notification is showing!");
        }
        super.setPosition(position);
    }

    @Override
    public void setWindow(Window window) {
        // Das Window wird vom NotificationManager gebraucht und
        // darf daher nicht geaendert werden, solange die Notification sichtbar ist
        if (popup != null && popup.isShowing()) {
            throw new IllegalStateException("The window can not be changed while notification is showing!");
        }
        super.setWindow(window);
    }

    @Override
    public void setScreen(Screen screen) {
        // Der Screen wird vom NotificationManager gebraucht und
        // darf daher nicht geaendert werden, solange die Notification sichtbar ist
        if (popup != null && popup.isShowing()) {
            throw new IllegalStateException("The screen can not be changed while notification is showing!");
        }
        super.setScreen(screen);
    }

    public static NotificationProperties getDefaults() {
        return defaults;
    }

    public static void setDefaults(final NotificationDefaults defaults) {
        Notification.defaults = defaults;
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
