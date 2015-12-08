package de.pheru.fx.controls.notification;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * @author Philipp Bruckner
 */
public final class NotificationManager {

    public static final double NOTIFICATION_VERTICAL_SPACING = 2.0;
    public static final double NOTIFICATION_HORIZONTAL_SPACING = 5.0;
    public static final double SCREEN_SPACING = 3.0;

    private static final ObjectProperty<Duration> defaultDuration = new SimpleObjectProperty<>(Duration.INDEFINITE);
    private static final BooleanProperty styleByType = new SimpleBooleanProperty(true);
    private static final BooleanProperty playSound = new SimpleBooleanProperty(false);
    private static final ObjectProperty<Screen> screen = createScreenProperty();
    private static final ObjectProperty<Alignment> alignment = createAlignmentProperty();
    private static final ObjectProperty<Window> boundOwner = createBoundOwnerProperty(); //TODO Umbenennen WindowForScreen?

    private static final ObservableList<Notification> notifications = createNotificationsList();

    private static final ChangeListener<Number> ownerListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
        Window window = boundOwner.get();
        Rectangle2D rect = new Rectangle2D(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        screen.set(Screen.getScreensForRectangle(rect).get(0));
    };

    private NotificationManager() {
    }

    private static ObjectProperty<Screen> createScreenProperty() {
        ObjectProperty<Screen> screenProperty = new SimpleObjectProperty<>(Screen.getPrimary());
        screenProperty.addListener((ObservableValue<? extends Screen> observable, Screen oldValue, Screen newValue) -> {
            arrangeNotifications(false);
        });
        return screenProperty;
    }

    private static ObjectProperty<Alignment> createAlignmentProperty() {
        ObjectProperty<Alignment> alignmentProperty = new SimpleObjectProperty<>(Alignment.BOTTOM_RIGHT);
        alignmentProperty.addListener((ObservableValue<? extends Alignment> observable, Alignment oldValue, Alignment newValue) -> {
            arrangeNotifications(false);
        });
        return alignmentProperty;
    }

    private static ObjectProperty<Window> createBoundOwnerProperty() {
        ObjectProperty<Window> boundOwnerProperty = new SimpleObjectProperty<>(null);
        boundOwnerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.xProperty().removeListener(ownerListener);
                oldValue.yProperty().removeListener(ownerListener);
            }
            if (newValue != null) {
                newValue.xProperty().addListener(ownerListener);
                newValue.yProperty().addListener(ownerListener);
            }
        });
        return boundOwnerProperty;
    }

    private static ObservableList<Notification> createNotificationsList() {
        ObservableList<Notification> notificationsList = FXCollections.observableArrayList();
        notificationsList.addListener((ListChangeListener.Change<? extends Notification> c) -> arrangeNotifications(true));
        return notificationsList;
    }

    public static void hideAll() {
        ObservableList<Notification> notificationsCopy = FXCollections.observableArrayList(notifications);
        for (Notification n : notificationsCopy) {
            n.hide(false);
        }
    }

    protected static void arrangeNotifications(boolean animated) {
        double targetX = SCREEN_SPACING;
        double targetY = SCREEN_SPACING;
        final Rectangle2D visualBounds = screen.get().getVisualBounds();

        if (alignment.get() == Alignment.BOTTOM_LEFT || alignment.get() == Alignment.BOTTOM_RIGHT) {
            targetY = visualBounds.getMaxY() - SCREEN_SPACING + NOTIFICATION_VERTICAL_SPACING;
        }
        if (alignment.get() == Alignment.BOTTOM_RIGHT || alignment.get() == Alignment.TOP_RIGHT) {
            targetX = visualBounds.getMaxX() - Notification.WIDTH - SCREEN_SPACING;
        }
        for (Notification notification : notifications) {
            if (alignment.get() == Alignment.TOP_LEFT || alignment.get() == Alignment.TOP_RIGHT) {
                if (targetY + notification.getHeight() + SCREEN_SPACING > visualBounds.getMaxY()) {
                    targetY = SCREEN_SPACING;
                    if (alignment.get() == Alignment.TOP_RIGHT) {
                        targetX -= notification.getWidth() + NOTIFICATION_HORIZONTAL_SPACING;
                    } else { //TOP_LEFT
                        targetX += notification.getWidth() + NOTIFICATION_HORIZONTAL_SPACING;
                    }
                }
                notification.setY(targetY, animated);
                targetY += notification.getHeight() + NOTIFICATION_VERTICAL_SPACING;
            } else { //BOTTOM
                if (targetY - notification.getHeight() < SCREEN_SPACING) {
                    targetY = visualBounds.getMaxY() - SCREEN_SPACING;
                    if (alignment.get() == Alignment.BOTTOM_RIGHT) {
                        targetX -= notification.getWidth() + NOTIFICATION_HORIZONTAL_SPACING;
                    } else { //BOTTOM_LEFT
                        targetX += notification.getWidth() + NOTIFICATION_HORIZONTAL_SPACING;
                    }
                }
                targetY -= notification.getHeight() + NOTIFICATION_VERTICAL_SPACING;
                notification.setY(targetY, animated);
            }
            notification.setX(targetX, animated);
        }
    }

    protected static double getTargetY(int index) {
        double targetY = SCREEN_SPACING;
        final Rectangle2D visualBounds = screen.get().getVisualBounds();

        if (alignment.get() == Alignment.BOTTOM_LEFT || alignment.get() == Alignment.BOTTOM_RIGHT) {
            targetY = visualBounds.getMaxY() - SCREEN_SPACING;
        }
        for (int i = 0; i < notifications.size() && i < index; i++) {
            Notification notification = notifications.get(i);
            if (alignment.get() == Alignment.TOP_LEFT || alignment.get() == Alignment.TOP_RIGHT) {
                if (targetY + notification.getHeight() + SCREEN_SPACING > visualBounds.getMaxY()) {
                    targetY = SCREEN_SPACING;
                }
                targetY += notification.getHeight() + NOTIFICATION_VERTICAL_SPACING;
            } else { //BOTTOM
                if (targetY - notification.getHeight() < SCREEN_SPACING) {
                    targetY = visualBounds.getMaxY() - SCREEN_SPACING;
                }
                targetY -= notification.getHeight() + NOTIFICATION_VERTICAL_SPACING;
            }
        }
        return targetY;
    }

    protected static ObservableList<Notification> getNotifications() {
        return notifications;
    }

    public static Screen getScreen() {
        return screen.get();
    }

    public static void setScreen(final Screen screen) {
        NotificationManager.screen.set(screen);
    }

    public static ObjectProperty<Screen> screenProperty() {
        return screen;
    }

    public static Window getBoundOwner() {
        return boundOwner.get();
    }

    public static ObjectProperty<Window> boundOwnerProperty() {
        return boundOwner;
    }

    public static void setBoundOwner(Window boundOwner) {
        NotificationManager.boundOwner.set(boundOwner);
    }

    public static Alignment getAlignment() {
        return alignment.get();
    }

    public static void setAlignment(Alignment alignment) {
        NotificationManager.alignment.set(alignment);
    }

    public static ObjectProperty<Alignment> alignmentProperty() {
        return alignment;
    }

    public static Duration getDefaultDuration() {
        return defaultDuration.get();
    }

    public static void setDefaultDuration(final Duration defaultTimer) {
        NotificationManager.defaultDuration.set(defaultTimer);
    }

    public static ObjectProperty<Duration> defaultDurationProperty() {
        return defaultDuration;
    }

    public static boolean isPlaySound() {
        return playSound.get();
    }

    public static void setPlaySound(final boolean playSound) {
        NotificationManager.playSound.set(playSound);
    }

    public static BooleanProperty playSoundProperty() {
        return playSound;
    }

    public static boolean isStyleByType() {
        return styleByType.get();
    }

    public static BooleanProperty styleByTypeProperty() {
        return styleByType;
    }

    public static void setStyleByType(boolean styleByType) {
        NotificationManager.styleByType.set(styleByType);
    }

    public enum Alignment {

        BOTTOM_RIGHT, BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT
    }
}
