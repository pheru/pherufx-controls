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

    // ******************************************************************************************************
    // * Constructors
    // ******************************************************************************************************

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

    private Notification(final Type type) {
        super(defaults);
        this.type = type;
        loadFXML();
        getStylesheets().addAll(defaults.getStylesheets());
        initHeaderLabel();
        initMouseHoverListeners();
        initClickedListeners();
        initDurationListeners();
    }

    private void loadFXML() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(NotificationManager.class.getResource("notification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load fxml!", e);
        }
    }

    private void initHeaderLabel() {
        headerLabel.visibleProperty().bind(headerLabel.textProperty().isEmpty().not());
        headerLabel.managedProperty().bind(headerLabel.textProperty().isEmpty().not());
    }

    private void initMouseHoverListeners() {
        root.setOnMouseEntered(event -> {
            durationTimeline.stop();
            closeButton.visibleProperty().bind(closableProperty());
        });
        root.setOnMouseExited(event -> {
            durationTimeline.play();
            closeButton.visibleProperty().unbind();
            closeButton.setVisible(false);
        });
    }

    private void initClickedListeners() {
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

    private void initDurationListeners() {
        durationProperty().addListener((observable, oldValue, newValue) -> {
            durationTimeline.stop();
            durationTimeline.getKeyFrames().clear();
            durationTimeline.getKeyFrames().add(new KeyFrame(newValue, (ActionEvent event) -> hide()));
            durationTimeline.playFromStart();
        });
    }

    private Label getContentLabel() {
        final Label label = new Label();
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        return label;
    }

    // ******************************************************************************************************
    // * public methods
    // ******************************************************************************************************

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

    public void bindDontShowAgainProperty(final Property<Boolean> property) {
        dontShowAgainBox.selectedProperty().bindBidirectional(property);
        dontShowAgainBox.setVisible(true);
        dontShowAgainBox.setManaged(true);
    }

    // ******************************************************************************************************
    // * protected methods
    // ******************************************************************************************************

    protected StackPane getRoot() {
        return root;
    }

    protected VBox getNotificationBox() { //TODO
        return notificationBox;
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

    protected void setY(final double position, final boolean animated) {
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

    // ******************************************************************************************************
    // * private methods
    // ******************************************************************************************************

    private ImageView createHeaderImageView(final Type type) {
        final ImageView iv = new ImageView(type.getImagePath());
//        final ImageView iv = new ImageView("de/pheru/fx/controls/notification/testicon.png");
        iv.setFitHeight(24);
        iv.setFitWidth(24);
        return iv;
    }

    @FXML
    private void closeNotification() {
        hide();
    }

    private void hidePopup() {
        if (root.getScene() != null) {
            popup.hide();
        }
    }

    private NotificationManager getNotificationManagerInstance() {
        if (getWindow() != null) {
            return NotificationManager.getInstanceForWindow(getWindow());
        } else {
            return NotificationManager.getInstanceForScreen(getScreen());
        }
    }

    // ******************************************************************************************************
    // * Getters & Setters
    // ******************************************************************************************************

    public void setOnMouseClicked(final EventHandler<? super MouseEvent> eventHandler) {
        root.setOnMouseClicked(event -> {
            eventHandler.handle(event);
            if (isHideOnMouseClicked()) {
                hideEventHandler.handle(event);
            }
        });
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

    // ******************************************************************************************************
    // * Static Methods
    // ******************************************************************************************************

    public static void hideAll(final Screen screen) {
        NotificationManager.getInstanceForScreen(screen).hideAll();
    }

    public static void hideAll(final Window owner) {
        NotificationManager.getInstanceForWindow(owner).hideAll();
    }

    public static NotificationProperties getDefaults() {
        return defaults;
    }

    public static void setDefaults(final NotificationDefaults defaults) {
        Notification.defaults = defaults;
    }

    // ******************************************************************************************************
    // * Enums
    // ******************************************************************************************************

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
