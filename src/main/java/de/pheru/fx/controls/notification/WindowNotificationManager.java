package de.pheru.fx.controls.notification;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Window;

/**
 * Created by Philipp on 22.03.2016.
 */
class WindowNotificationManager extends NotificationManager {

    private final Window window;
    private Rectangle2D visualBounds;

    WindowNotificationManager(final Window window) {
        this.window = window;
        updateVisualBounds();

        final ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> Platform.runLater(() -> {
            updateVisualBounds();
            for (final Pos pos : Pos.values()) {
                arrangeNotifications(pos, false);
            }
        });
        window.xProperty().addListener(changeListener);
        window.yProperty().addListener(changeListener);
        window.widthProperty().addListener(changeListener);
        window.heightProperty().addListener(changeListener);
    }

    private void updateVisualBounds() {
        final Parent root = window.getScene().getRoot();
        final Bounds bounds = root.localToScreen(root.getBoundsInLocal());
        visualBounds = new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    protected Rectangle2D getVisualBounds() {
        return visualBounds;
    }

    @Override
    protected Window getOwner() {
        return window;
    }

}
