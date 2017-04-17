package de.pheru.fx.controls.notification;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

abstract class NotificationManager {

    public static final double NOTIFICATION_SPACING = 2.0;
    public static final double SCREEN_SPACING = 3.0;

    private static final Map<Window, WindowNotificationManager> windowNotificationManagers = new HashMap<>();
    private static final Map<Screen, ScreenNotificationManager> screenNotificationManagers = new HashMap<>();

    private final ObservableMap<Pos, ObservableList<Notification>> notificationsMap = FXCollections.observableHashMap();

    protected abstract Rectangle2D getVisualBounds();

    protected abstract Window getOwner();

    protected static NotificationManager getInstanceForWindow(final Window window) {
        if (windowNotificationManagers.containsKey(window)) {
            return windowNotificationManagers.get(window);
        } else {
            final WindowNotificationManager windowNotificationManager = new WindowNotificationManager(window);
            windowNotificationManagers.put(window, windowNotificationManager);
            return windowNotificationManager;
        }
    }

    protected static NotificationManager getInstanceForScreen(final Screen screen) {
        if (screenNotificationManagers.containsKey(screen)) {
            return screenNotificationManagers.get(screen);
        } else {
            final ScreenNotificationManager screenNotificationManager = new ScreenNotificationManager(screen);
            screenNotificationManagers.put(screen, screenNotificationManager);
            return screenNotificationManager;
        }
    }

    protected void show(final boolean animate, final Notification notification) {
        final Popup popup = notification.getPopup();
        final Pos position = notification.getPosition();
        final StackPane root = notification.getRoot();

        popup.show(getOwner());
        popup.setX(initialTargetX(position, root.getWidth()));
        popup.setY(getNewY(position, root.getHeight()));

        popup.heightProperty().addListener((observable, oldValue, newValue) -> {
            arrangeNotifications(position, true);
        });

        getNotificationsForPosition(position).add(notification);

        if (animate) {
            playAnimation(position, notification);
        }

        if (notification.isPlaySound()) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void playAnimation(final Pos position, final Notification notification) {
        final Popup popup = notification.getPopup();
        final StackPane root = notification.getRoot();
        final FadeTransition fadeTransition = new FadeTransition(Duration.millis(400), notification.getNotificationBox());
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(100);
        notification.getNotificationBox().setOpacity(0);
        if (position == Pos.CENTER) {
            final ScaleTransition scaleTransition = new ScaleTransition(notification.getAnimationDuration(), root);
            scaleTransition.setFromX(0.0);
            scaleTransition.setToX(1.0);
            scaleTransition.setFromY(0.0);
            scaleTransition.setToY(1.0);
            scaleTransition.setOnFinished(event -> fadeTransition.play());
            scaleTransition.play();
        } else {
            final Rectangle clip = new Rectangle(popup.getWidth(), popup.getHeight());
            final DoubleProperty layoutProperty;
            switch (notification.getPosition()) {
                case TOP_LEFT:
                case CENTER_LEFT:
                case BOTTOM_LEFT:
                case BASELINE_LEFT:
                    clip.setLayoutX(0 - clip.getWidth());
                    clip.setLayoutY(0);
                    layoutProperty = clip.layoutXProperty();
                    break;
                case TOP_CENTER:
                    clip.setLayoutX(0);
                    clip.setLayoutY(0 - clip.getHeight());
                    layoutProperty = clip.layoutYProperty();
                    break;
                case TOP_RIGHT:
                case CENTER_RIGHT:
                case BOTTOM_RIGHT:
                case BASELINE_RIGHT:
                    clip.setLayoutX(clip.getWidth());
                    clip.setLayoutY(0);
                    layoutProperty = clip.layoutXProperty();
                    break;
                case BOTTOM_CENTER:
                case BASELINE_CENTER:
                default:
                    clip.setLayoutX(0);
                    clip.setLayoutY(clip.getHeight());
                    layoutProperty = clip.layoutYProperty();
                    break;
            }
            root.setClip(clip);
            final Timeline timeline = new Timeline(new KeyFrame(notification.getAnimationDuration(), new KeyValue(layoutProperty, 0)));
            timeline.setOnFinished(event -> fadeTransition.play());
            timeline.play();
        }
    }

    protected void hideAll() {
        for (final ObservableList<Notification> notifications : notificationsMap.values()) {
            final ObservableList<Notification> notificationsCopy = FXCollections.observableArrayList(notifications);
            for (final Notification n : notificationsCopy) {
                n.hide();
            }
        }
    }

    protected void arrangeNotifications(final Pos position, final boolean animated) {
        double totalHeight = 0;
        for (final Notification notification : getNotificationsForPosition(position)) {
            final double targetX = initialTargetX(position, notification.getRoot().getWidth());
            final double targetY = initialTargetY(position, notification.getRoot().getHeight());

            notification.setX(targetX, animated);
            switch (position.getVpos()) {
                case TOP:
                    notification.setY(targetY + totalHeight, animated);
                    break;
                case CENTER:
                case BOTTOM:
                case BASELINE:
                default:
                    notification.setY(targetY - totalHeight, animated);
                    break;
            }
            totalHeight += notification.getRoot().getHeight() + NOTIFICATION_SPACING;
        }
    }

    protected void removeNotification(final Notification notification) {
        getNotificationsForPosition(notification.getPosition()).remove(notification);
    }

    private double initialTargetX(final Pos position, final double notificationWidth) {
        final Rectangle2D visualBounds = getVisualBounds();
        switch (position.getHpos()) {
            case LEFT:
                return visualBounds.getMinX() + SCREEN_SPACING;
            case CENTER:
                return visualBounds.getMinX() + (visualBounds.getWidth() - SCREEN_SPACING - notificationWidth) / 2;
            case RIGHT:
            default:
                return visualBounds.getMaxX() - SCREEN_SPACING - notificationWidth;
        }
    }

    private double initialTargetY(final Pos position, final double notificationHeight) {
        final Rectangle2D visualBounds = getVisualBounds();
        switch (position.getVpos()) {
            case TOP:
                return visualBounds.getMinY() + SCREEN_SPACING;
            case CENTER:
                return visualBounds.getMinY() + (visualBounds.getHeight() - SCREEN_SPACING) / 2 - notificationHeight;
            case BOTTOM:
            case BASELINE:
            default:
                return visualBounds.getMaxY() - SCREEN_SPACING - notificationHeight;
        }
    }

    private double getNewY(final Pos position, final double notificationHeight) {
        double targetY = initialTargetY(position, notificationHeight);
        for (final Notification notification : getNotificationsForPosition(position)) {
            switch (position.getVpos()) {
                case TOP:
                    targetY += notification.getRoot().getHeight() + NOTIFICATION_SPACING;
                    break;
                case CENTER:
                case BOTTOM:
                case BASELINE:
                default:
                    targetY -= notification.getRoot().getHeight() + NOTIFICATION_SPACING;
                    break;
            }
        }
        return targetY;
    }

    private ObservableList<Notification> getNotificationsForPosition(final Pos position) {
        if (!notificationsMap.containsKey(position)) {
            notificationsMap.put(position, createNotificationsList(position));
        }
        return notificationsMap.get(position);
    }

    private ObservableList<Notification> createNotificationsList(final Pos position) {
        final ObservableList<Notification> notificationsList = FXCollections.observableArrayList();
        notificationsList.addListener((ListChangeListener.Change<? extends Notification> c) -> {
            while (c.next()) {
                if (c.wasRemoved()) { //Neue werden "von oben" eingefuegt -> kein arrange noetig
                    arrangeNotifications(position, true);
                }
            }
        });
        return notificationsList;
    }
}
