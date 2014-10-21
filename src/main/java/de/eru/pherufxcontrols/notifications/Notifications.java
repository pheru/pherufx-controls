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

    private static final ObservableList<Notification> notifications = initNotificationsList();

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
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getPosition() != i) {
                notifications.get(i).setPosition(i);
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
    
    public static void remove(Notification notification){
        notifications.remove(notification);
    }
    public static void add(Notification notification){
        notifications.add(notification);
    }
}