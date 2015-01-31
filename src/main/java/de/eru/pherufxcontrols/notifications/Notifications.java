package de.eru.pherufxcontrols.notifications;

import de.eru.pherufxcontrols.utils.NotificationType;
import java.io.IOException;
import java.net.URL;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

    public static final Rectangle2D VISUAL_BOUNDS = Screen.getPrimary().getVisualBounds();

    private static final ObservableList<Notification> notifications = initNotificationsList();

    private static NotificationAlignment alignment = NotificationAlignment.BOTTOM_RIGHT;
    private static final IntegerProperty defaultTimer = new SimpleIntegerProperty(7);
    
    private Notifications() {
    }

    private static ObservableList<Notification> initNotificationsList() {
        ObservableList<Notification> notificationsList = FXCollections.observableArrayList();
        notificationsList.addListener((ListChangeListener.Change<? extends Notification> c) -> {
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
        if (alignment == NotificationAlignment.BOTTOM_LEFT || alignment == NotificationAlignment.BOTTOM_RIGHT) {
            targetY = VISUAL_BOUNDS.getMaxY() - 3;
        }

        for (Notification notification : notifications) {
            if (alignment == NotificationAlignment.BOTTOM_RIGHT || alignment == NotificationAlignment.TOP_RIGHT) {
                targetX = VISUAL_BOUNDS.getMaxX() - notification.getRoot().getScene().getWindow().getWidth() - 5;
            }

            notification.setX(targetX);

            if (alignment == NotificationAlignment.TOP_LEFT || alignment == NotificationAlignment.TOP_RIGHT) {
                notification.setY(targetY, animated);
                targetY += notification.getHeight() + 2;
            } else {
                targetY -= notification.getHeight() + 2;
                notification.setY(targetY, animated);
            }
        }
    }

    public static InfoNotification createInfoNotification(NotificationType type) {
        InfoNotification infoNotification = (InfoNotification) getLoadedNotification("info");
        infoNotification.setType(type);
        return infoNotification;
    }

    public static CustomNotification createCustomNotification() {
        return (CustomNotification) getLoadedNotification("custom");
    }

    private static Notification getLoadedNotification(String fxmlName) {
        URL resource = Notifications.class.getResource(fxmlName + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            fxmlLoader.load();
            Notification notification = fxmlLoader.getController();
            notification.setRoot(fxmlLoader.getRoot());
            notification.setTimer(defaultTimer.get());
            return notification;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void removeNotification(Notification notification) {
        notifications.remove(notification);
    }

    public static void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public static NotificationAlignment getAlignment() {
        return alignment;
    }

    public static void setAlignment(NotificationAlignment alignment) {
        Notifications.alignment = alignment;
        arrangeNotifications(false);
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
