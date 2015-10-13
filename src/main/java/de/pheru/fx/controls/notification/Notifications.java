package de.pheru.fx.controls.notification;

import java.io.IOException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
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

    /*
    TODO
    screen: ChangeListener für rearrange
    owner: listener add/remove von XY Listener
    x & y Changelistener als Feld
    */
    private static final ObjectProperty<Screen> screen = new SimpleObjectProperty<>(Screen.getPrimary());
    private static Window owner;

    private static final ObservableList<CustomNotification> notifications = createNotificationsList();

    private static final ObjectProperty<Alignment> alignment = createAlignmentProperty();
    private static final IntegerProperty defaultTimer = new SimpleIntegerProperty(10);

    private Notifications() {
    }

    private static ObjectProperty<Alignment> createAlignmentProperty() {
        ObjectProperty<Alignment> p = new SimpleObjectProperty<>(Alignment.BOTTOM_RIGHT);
        p.addListener((ObservableValue<? extends Alignment> observable, Alignment oldValue, Alignment newValue) -> {
            arrangeNotifications(false);
        });
        return p;
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

    private static void arrangeNotifications(boolean animated) {
        double targetX = 5.0;
        double targetY = 5.0;
        final Rectangle2D visualBounds = screen.get().getVisualBounds();
        
        if (alignment.get() == Alignment.BOTTOM_LEFT || alignment.get() == Alignment.BOTTOM_RIGHT) {
            targetY = visualBounds.getMaxY() - 3;
        }
        if (alignment.get() == Alignment.BOTTOM_RIGHT || alignment.get() == Alignment.TOP_RIGHT) {
            targetX = visualBounds.getMaxX() - 350 - 5; //TODO 350 nicht als fixe Zahl
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
        ChangeListener<Number> listener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Rectangle2D rect = new Rectangle2D(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight());
            System.out.println(rect);
            ObservableList<Screen> screens = Screen.getScreensForRectangle(rect);
            System.out.println(screens);
        };
        owner.xProperty().addListener(listener);
        owner.yProperty().addListener(listener);
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

    public enum Alignment {

        BOTTOM_RIGHT, BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT
    }
}
