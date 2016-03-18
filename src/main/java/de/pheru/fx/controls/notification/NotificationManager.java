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
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;

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
    private static final ObjectProperty<Pos> position = createPositionProperty();
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

    private static ObjectProperty<Pos> createPositionProperty() {
        ObjectProperty<Pos> posProperty = new SimpleObjectProperty<>(Pos.BOTTOM_RIGHT);
        posProperty.addListener((ObservableValue<? extends Pos> observable, Pos oldValue, Pos newValue) -> {
            arrangeNotifications(false);
        });
        return posProperty;
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

    public static List<Notification> getNotificationsUnmodifiable() {
        return Collections.unmodifiableList(notifications);
    }

    protected static void arrangeNotifications(boolean animated) {
        double targetX = SCREEN_SPACING;
        double targetY = SCREEN_SPACING;
        final Rectangle2D visualBounds = screen.get().getVisualBounds();

        //Target-Y
        switch (position.get().getVpos()) {
            case TOP:
                break;
            case CENTER:
                targetY = (visualBounds.getMaxY() - SCREEN_SPACING + NOTIFICATION_VERTICAL_SPACING) / 2;
                break;
            case BOTTOM:
            default:
                targetY = visualBounds.getMaxY() - SCREEN_SPACING + NOTIFICATION_VERTICAL_SPACING;
                break;
        }

        //Target-X
        switch (position.get().getHpos()) {
            case LEFT:
                break;
            case CENTER:
                targetX = (visualBounds.getMaxX() - Notification.WIDTH - SCREEN_SPACING) / 2;
                break;
            case RIGHT:
            default:
                targetX = visualBounds.getMaxX() - Notification.WIDTH - SCREEN_SPACING;
                break;
        }

        for (Notification notification : notifications) {
            switch (position.get().getVpos()) {
                case TOP:
                case CENTER:
                    if (targetY + notification.getHeight() + SCREEN_SPACING > visualBounds.getMaxY()) {
                        targetY = SCREEN_SPACING;
                        if (position.get().getHpos() == HPos.RIGHT) {
                            targetX -= notification.getWidth() + NOTIFICATION_HORIZONTAL_SPACING;
                        } else { //LEFT, CENTER
                            targetX += notification.getWidth() + NOTIFICATION_HORIZONTAL_SPACING;
                        }
                    }
                    notification.setY(targetY, animated);
                    targetY += notification.getHeight() + NOTIFICATION_VERTICAL_SPACING;
                    break;
                case BOTTOM:
                default:
                    if (targetY - notification.getHeight() < SCREEN_SPACING) {
                        targetY = visualBounds.getMaxY() - SCREEN_SPACING;
                        if (position.get().getHpos() == HPos.RIGHT) {
                            targetX -= notification.getWidth() + NOTIFICATION_HORIZONTAL_SPACING;
                        } else { //LEFT, CENTER
                            targetX += notification.getWidth() + NOTIFICATION_HORIZONTAL_SPACING;
                        }
                    }
                    targetY -= notification.getHeight() + NOTIFICATION_VERTICAL_SPACING;
                    notification.setY(targetY, animated);
                    break;
            }
            notification.setX(targetX, animated);
        }
    }

    protected static double getTargetY(int index) {
        double targetY = SCREEN_SPACING;
        final Rectangle2D visualBounds = screen.get().getVisualBounds();

        if (position.get() == Pos.BOTTOM_LEFT || position.get() == Pos.BOTTOM_RIGHT) {
            targetY = visualBounds.getMaxY() - SCREEN_SPACING;
        }
        for (int i = 0; i < notifications.size() && i < index; i++) {
            Notification notification = notifications.get(i);
            if (position.get() == Pos.TOP_LEFT || position.get() == Pos.TOP_RIGHT) {
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

    public static Pos getPosition() {
        return position.get();
    }

    public static void setPosition(Pos position) {
        NotificationManager.position.set(position);
    }

    public static ObjectProperty<Pos> positionProperty() {
        return position;
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

}
