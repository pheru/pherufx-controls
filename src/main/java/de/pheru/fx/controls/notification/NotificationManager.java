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

    private static final ObjectProperty<Screen> screen = createScreenProperty();
    private static final ObjectProperty<Alignment> alignment = createAlignmentProperty();
    private static final ObjectProperty<Duration> defaultDuration = new SimpleObjectProperty<>(Duration.INDEFINITE);
    private static final BooleanProperty playSound = new SimpleBooleanProperty(false);
    private static final BooleanProperty showAgainOnOwnerHidden = new SimpleBooleanProperty(false); //TODO nicht implementiert

    private static final ObservableList<Notification> notifications = createNotificationsList();
    private static Window boundOwner = null;
    private static final ChangeListener<Number> ownerListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
        Rectangle2D rect = new Rectangle2D(boundOwner.getX(), boundOwner.getY(), boundOwner.getWidth(), boundOwner.getHeight());
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

    private static ObservableList<Notification> createNotificationsList() {
        ObservableList<Notification> notificationsList = FXCollections.observableArrayList();
        notificationsList.addListener((ListChangeListener.Change<? extends Notification> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    arrangeNotifications(false); //TODO bei add kompletten arrange?
                } else {
                    arrangeNotifications(true);
                }
            }
        });
        return notificationsList;
    }

    public static void hideAll() {
        ObservableList<Notification> notificationsCopy = FXCollections.observableArrayList(notifications);
        for (Notification n : notificationsCopy) {
            n.hide(false);
        }
    }

    //TODO magic-numbers entfernen
    protected static void arrangeNotifications(boolean animated) {
        double targetX = 5.0;
        double targetY = 5.0;
        final Rectangle2D visualBounds = screen.get().getVisualBounds();

        if (alignment.get() == Alignment.BOTTOM_LEFT || alignment.get() == Alignment.BOTTOM_RIGHT) {
            targetY = visualBounds.getMaxY() - 3;
        }
        if (alignment.get() == Alignment.BOTTOM_RIGHT || alignment.get() == Alignment.TOP_RIGHT) {
            targetX = visualBounds.getMaxX() - 350 - 5;
        }
        for (Notification notification : notifications) {
            if (alignment.get() == Alignment.TOP_LEFT || alignment.get() == Alignment.TOP_RIGHT) {
                if (targetY + notification.getHeight() > visualBounds.getMaxY()) {
                    targetY = 5.0;
                    if (alignment.get() == Alignment.TOP_RIGHT) {
                        targetX -= notification.getWidth() + 5;
                    } else { //TOP_LEFT
                        targetX += notification.getWidth() + 5;
                    }
                }
                notification.setY(targetY, animated);
                targetY += notification.getHeight() + 2;
            } else { //BOTTOM
                if (targetY - notification.getHeight() < 3.0) {
                    targetY = visualBounds.getMaxY() - 3;
                    if (alignment.get() == Alignment.BOTTOM_RIGHT) {
                        targetX -= notification.getWidth() + 5;
                    } else { //BOTTOM_LEFT
                        targetX += notification.getWidth() + 5;
                    }
                }
                targetY -= notification.getHeight() + 2;
                notification.setY(targetY, animated);
            }
            notification.setX(targetX, animated);
        }
    }

    protected static ObservableList<Notification> getNotifications() {
        return notifications;
    }

    public static void bindScreenToOwner(Window owner) {
        if (boundOwner != null) {
            boundOwner.xProperty().removeListener(ownerListener);
            boundOwner.yProperty().removeListener(ownerListener);
        }
        boundOwner = owner;
        boundOwner.xProperty().addListener(ownerListener);
        boundOwner.yProperty().addListener(ownerListener);
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

    public enum Alignment {

        BOTTOM_RIGHT, BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT
    }

    public boolean isShowAgainOnOwnerHidden() {
        return showAgainOnOwnerHidden.get();
    }

    public void setShowAgainOnOwnerHidden(final boolean showAgainOnOwnerHidden) {
        NotificationManager.showAgainOnOwnerHidden.set(showAgainOnOwnerHidden);
    }

    public BooleanProperty showAgainOnOwnerHiddenProperty() {
        return showAgainOnOwnerHidden;
    }
}
