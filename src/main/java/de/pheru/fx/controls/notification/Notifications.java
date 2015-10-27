package de.pheru.fx.controls.notification;

import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 *
 * @author Philipp Bruckner
 */
public final class Notifications {

    public static final int TIMER_INDEFINITE = -1;

    /*
     public properties
     */
    private static final ObjectProperty<Screen> screen = createScreenProperty();
    private static final ObjectProperty<Alignment> alignment = createAlignmentProperty();
    private static final IntegerProperty defaultTimer = new SimpleIntegerProperty(TIMER_INDEFINITE);
    private static final BooleanProperty playSound = new SimpleBooleanProperty(false);
    private static final BooleanProperty showAgainOnOwnerHidden = new SimpleBooleanProperty(false); //TODO nicht implementiert

    /*
     private fields
     */
    private static final ObservableList<CustomNotification> notifications = createNotificationsList();
    private static Window boundOwner = null;
    private static final ChangeListener<Number> ownerListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
        //TODO fertig implementieren
        Rectangle2D rect = new Rectangle2D(boundOwner.getX(), boundOwner.getY(), boundOwner.getWidth(), boundOwner.getHeight());
        System.out.println(rect);
        ObservableList<Screen> screens = Screen.getScreensForRectangle(rect);
        System.out.println(screens);
    };

    private Notifications() {
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

    private static ObservableList<CustomNotification> createNotificationsList() {
        ObservableList<CustomNotification> notificationsList = FXCollections.observableArrayList();
        notificationsList.addListener((ListChangeListener.Change<? extends CustomNotification> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    arrangeNotifications(false);
                } else {
                    arrangeNotifications(true);
                }
            }
        });
        return notificationsList;
    }

    private static void arrangeNotifications(boolean animated) { //TODO magic-numbers entfernen
        double targetX = 5.0;
        double targetY = 5.0;
        final Rectangle2D visualBounds = screen.get().getVisualBounds();

        if (alignment.get() == Alignment.BOTTOM_LEFT || alignment.get() == Alignment.BOTTOM_RIGHT) {
            targetY = visualBounds.getMaxY() - 3;
        }
        if (alignment.get() == Alignment.BOTTOM_RIGHT || alignment.get() == Alignment.TOP_RIGHT) {
            targetX = visualBounds.getMaxX() - 350 - 5;
        }
        for (CustomNotification notification : notifications) {
            if (alignment.get() == Alignment.TOP_LEFT || alignment.get() == Alignment.TOP_RIGHT) {
                if (targetY + notification.getHeight() > visualBounds.getMaxY()) {
                    targetY = 5.0;
                    if (alignment.get() == Alignment.BOTTOM_RIGHT || alignment.get() == Alignment.TOP_RIGHT) {
                        targetX -= notification.getWidth() + 5;
                    } else {
                        targetX += notification.getWidth() + 5;
                    }
                }
                notification.setY(targetY, animated);
                targetY += notification.getHeight() + 2;
            } else {
                if (targetY - notification.getHeight() < 3.0) {
                    targetY = visualBounds.getMaxY() - 3;
                    if (alignment.get() == Alignment.BOTTOM_RIGHT || alignment.get() == Alignment.TOP_RIGHT) {
                        targetX -= notification.getWidth() + 5;
                    } else {
                        targetX += notification.getWidth() + 5;
                    }
                }
                targetY -= notification.getHeight() + 2;
                notification.setY(targetY, animated);
            }
            notification.setX(targetX, animated);
        }
    }

    public static Notification createNotification(Notification.Type type) {
        Notification notification = loadNotification();
        notification.setType(type);
        return notification;
    }

    private static Notification loadNotification() {
        Notification notification = new Notification();
        try {
            FXMLLoader notificationFxmlLoader = new FXMLLoader(Notifications.class.getResource("notification.fxml"));
            notificationFxmlLoader.setController(notification);
            notificationFxmlLoader.load();

            FXMLLoader contentFxmlLoader = new FXMLLoader(Notifications.class.getResource("content.fxml"));
            contentFxmlLoader.setController(notification);
            contentFxmlLoader.load();

            notification.setContent(contentFxmlLoader.getRoot());
            notification.setTimer(defaultTimer.get());
            return notification;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static CustomNotification createCustomNotification(Node content) {
        try {
            CustomNotification notification = new CustomNotification();
            FXMLLoader fxmlLoader = new FXMLLoader(Notifications.class.getResource("notification.fxml"));
            fxmlLoader.setController(notification);
            fxmlLoader.load();
            notification.setContent(content);
            notification.setTimer(defaultTimer.get());
            return notification;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected static void removeNotification(CustomNotification notification) {
        notifications.remove(notification);
    }

    protected static void addNotification(CustomNotification notification) {
        notifications.add(notification);
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
        Notifications.screen.set(screen);
    }

    public static ObjectProperty<Screen> screenProperty() {
        return screen;
    }

    public static Alignment getAlignment() {
        return alignment.get();
    }

    public static void setAlignment(Alignment alignment) {
        Notifications.alignment.set(alignment);
    }

    public static ObjectProperty<Alignment> alignmentProperty() {
        return alignment;
    }

    public static Integer getDefaultTimer() {
        return defaultTimer.get();
    }

    public static void setDefaultTimer(final Integer defaultTimer) {
        Notifications.defaultTimer.set(defaultTimer);
    }

    public static IntegerProperty defaultTimerProperty() {
        return defaultTimer;
    }

    public static boolean isPlaySound() {
        return playSound.get();
    }

    public static void setPlaySound(final boolean playSound) {
        Notifications.playSound.set(playSound);
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
        Notifications.showAgainOnOwnerHidden.set(showAgainOnOwnerHidden);
    }

    public BooleanProperty showAgainOnOwnerHiddenProperty() {
        return showAgainOnOwnerHidden;
    }
}
