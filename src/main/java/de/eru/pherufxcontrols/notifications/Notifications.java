package de.eru.pherufxcontrols.notifications;

import java.io.IOException;
import java.net.URL;
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

    private static final ObservableList<Notification> NOTIFICATIONS = initNotificationsList();

    private static NotificationAlignment alignment = NotificationAlignment.BOTTOM_RIGHT;

    private Notifications() {
    }

    private static ObservableList<Notification> initNotificationsList() {
        ObservableList<Notification> notificationsList = FXCollections.observableArrayList();
        notificationsList.addListener((ListChangeListener.Change<? extends Notification> c) -> {
            arrangeNotifications();
        });
        return notificationsList;
    }

    private static void arrangeNotifications() {
        double currentPosition = 0.0;
        if (alignment == NotificationAlignment.BOTTOM_LEFT || alignment == NotificationAlignment.BOTTOM_RIGHT) {
            currentPosition = VISUAL_BOUNDS.getMaxY();
        }
        for (Notification notification : NOTIFICATIONS) {
            if (alignment == NotificationAlignment.TOP_LEFT || alignment == NotificationAlignment.TOP_RIGHT) {
                notification.setY(currentPosition);
                currentPosition += notification.getHeight();
            } else {
                currentPosition -= notification.getHeight();
                notification.setY(currentPosition);
            }
            if (alignment == NotificationAlignment.BOTTOM_LEFT || alignment == NotificationAlignment.TOP_LEFT) {
                notification.setX(0);
            } else {
                notification.setX(VISUAL_BOUNDS.getMaxX() - notification.getRoot().getScene().getWindow().getWidth());
            }
        }
    }

    public static InfoNotification createInfoNotification() {
        return (InfoNotification) getLoadedNotification("info");
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
            return notification;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void removeNotification(Notification notification) {
        NOTIFICATIONS.remove(notification);
    }

    public static void addNotification(Notification notification) {
        NOTIFICATIONS.add(notification);
    }

    public static NotificationAlignment getAlignment() {
        return alignment;
    }

    public static void setAlignment(NotificationAlignment alignment) {
        Notifications.alignment = alignment;
        arrangeNotifications();
    }

}
