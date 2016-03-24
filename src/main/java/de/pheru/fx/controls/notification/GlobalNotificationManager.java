package de.pheru.fx.controls.notification;

import com.sun.javafx.stage.StageHelper;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Created by Philipp on 22.03.2016.
 */
class GlobalNotificationManager extends NotificationManager {

    private static Stage notificationStage;

    @Override
    protected Rectangle2D getVisualBounds() {
        return Screen.getPrimary().getVisualBounds(); //TODO evtl. minY etwas größer (wegen Fensterleiste & -Buttons)
    }

    @Override
    protected Window getOwner() {
        return getNotificationStage();
    }

    protected static Stage getNotificationStage() {
        if (notificationStage == null) {
            notificationStage = new Stage(StageStyle.UTILITY);
            notificationStage.setOpacity(0.0);
            notificationStage.show();
            StageHelper.getStages().addListener((ListChangeListener<Stage>) c -> {
                if (c.getList().size() == 1 && c.getList().get(0) == notificationStage) {
                    Platform.runLater(() -> {
                        notificationStage.hide();
                    });
                }
            });
        }
        return notificationStage;
    }

}
