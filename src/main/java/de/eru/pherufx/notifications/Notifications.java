package de.eru.pherufx.notifications;

import java.io.IOException;
import java.net.URL;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 *
 * @author Philipp Bruckner
 */
public final class Notifications {

    public enum Alignment {

        BOTTOM_RIGHT, BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT
    }

    protected static final Rectangle2D VISUAL_BOUNDS = Screen.getPrimary().getVisualBounds();

    private static final ObservableList<AbstractNotification> notifications = initNotificationsList();

    private static final ObjectProperty<Alignment> alignment = createAlignmentProperty();
    private static final IntegerProperty defaultTimer = new SimpleIntegerProperty(10);

    private Notifications() {
    }
    
    private static ObjectProperty<Alignment> createAlignmentProperty(){
        ObjectProperty<Alignment> p = new SimpleObjectProperty<>(Alignment.BOTTOM_RIGHT);
        p.addListener((ObservableValue<? extends Alignment> observable, Alignment oldValue, Alignment newValue) -> {
            arrangeNotifications(false);
        });
        return p;
    }

    private static ObservableList<AbstractNotification> initNotificationsList() {
        ObservableList<AbstractNotification> notificationsList = FXCollections.observableArrayList();
        notificationsList.addListener((ListChangeListener.Change<? extends AbstractNotification> c) -> {
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
        if (alignment.get() == Alignment.BOTTOM_LEFT || alignment.get() == Alignment.BOTTOM_RIGHT) {
            targetY = VISUAL_BOUNDS.getMaxY() - 3;
        }
        if (alignment.get() == Alignment.BOTTOM_RIGHT || alignment.get() == Alignment.TOP_RIGHT) {
            targetX = VISUAL_BOUNDS.getMaxX() - 350 - 5; //TODO 350 nicht als fixe Zahl
        }

        for (AbstractNotification notification : notifications) {

            if (alignment.get() == Alignment.TOP_LEFT || alignment.get() == Alignment.TOP_RIGHT) {
                if (targetY + notification.getHeight() > VISUAL_BOUNDS.getMaxY()) {
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
                    targetY = VISUAL_BOUNDS.getMaxY() - 3;
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
        Notification notification = (Notification) getLoadedNotification("notification");
        notification.setType(type);
        return notification;
    }

    public static void showNotification(Notification.Type type, String header, String text, int timer, Property<Boolean> property) {
        Notification notification = createNotification(type);
        notification.setHeader(header);
        notification.setText(text);
        if (timer > 0) {
            notification.setTimer(timer);
        }
        if (property != null) {
            notification.bindDontShowAgainProperty(property);
        }
        notification.show();
    }

    public static CustomNotification createCustomNotification() {
        return (CustomNotification) getLoadedNotification("custom");
    }

    private static AbstractNotification getLoadedNotification(String fxmlName) {
        URL resource = Notifications.class.getResource(fxmlName + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            fxmlLoader.load();
            AbstractNotification notification = fxmlLoader.getController();
            notification.setRoot(fxmlLoader.getRoot());
            notification.setTimer(defaultTimer.get());
            return notification;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected static void removeNotification(AbstractNotification notification) {
        notifications.remove(notification);
    }

    protected static void addNotification(AbstractNotification notification) {
        notifications.add(notification);
    }

    public static Alignment getAlignment() {
        return alignment.get();
    }

    public static void setAlignment(Alignment alignment) {
        Notifications.alignment.set(alignment);
    }
    
    public static ObjectProperty<Alignment> alignmentProperty(){
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

}
