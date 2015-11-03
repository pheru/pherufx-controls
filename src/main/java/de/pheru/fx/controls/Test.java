package de.pheru.fx.controls;

import de.pheru.fx.controls.notification.Notification;
import de.pheru.fx.controls.notification.Notifications;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 *
 * @author Philipp Bruckner
 */
public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createTestInterface(primaryStage)));
        primaryStage.show();
    }

    private VBox createTestInterface(Window owner) {
        Button tonButton = new Button("Ton");
        tonButton.setOnAction((ActionEvent event) -> {
            testTon(owner);
        });
        Button timerButton = new Button("Timer");
        timerButton.setOnAction((ActionEvent event) -> {
            testTimer(owner);
        });
        Button checkBoxButton = new Button("CheckBox");
        checkBoxButton.setOnAction((ActionEvent event) -> {
            testCheckBox(owner);
        });
        Button exitButtonButton = new Button("ExitButton");
        exitButtonButton.setOnAction((ActionEvent event) -> {
            testExitButton(owner);
        });
        Button alignmentButton = new Button("Alignment");
        alignmentButton.setOnAction((ActionEvent event) -> {
            testAlignment(owner);
        });
        Button screenButton = new Button("Screen");
        screenButton.setOnAction((ActionEvent event) -> {
            testScreen(owner);
        });
        return new VBox(tonButton, timerButton, checkBoxButton, exitButtonButton, alignmentButton, screenButton);
    }

    private void testTon(Window owner) {
        new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    Notifications.createNotification(Notification.Type.INFO).setText("Ton").show(owner, true); //Ton
                });
                Thread.sleep(1000);
                Platform.runLater(() -> {
                    Notifications.createNotification(Notification.Type.INFO).setText("Kein Ton").show(owner); //Kein Ton
                });
                Thread.sleep(1000);
                Notifications.setPlaySound(true);
                Platform.runLater(() -> {
                    Notifications.createNotification(Notification.Type.INFO).setText("Ton ").show(owner); //Ton
                });
                Thread.sleep(1000);
                Platform.runLater(() -> {
                    Notifications.createNotification(Notification.Type.INFO).setText("Kein Ton").show(owner, false); //Kein Ton
                });
                Notifications.setPlaySound(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void testTimer(Window owner) {
        Notifications.createNotification(Notification.Type.INFO).setText("Default (Indefinite)").show(owner);
        Notifications.createNotification(Notification.Type.INFO).setText("Indefinite").setDuration(Duration.INDEFINITE).show(owner);
        Notifications.createNotification(Notification.Type.INFO).setText("3 Sekunden").setDuration(Duration.seconds(3)).show(owner);
        Notifications.setDefaultDuration(Duration.seconds(3));
        Notifications.createNotification(Notification.Type.INFO).setText("Default (3 Sekunden)").show(owner);
        Notifications.createNotification(Notification.Type.INFO).setText("5 Sekunden").setDuration(Duration.seconds(5)).show(owner);
        Notifications.createNotification(Notification.Type.INFO).setText("Indefinite").setDuration(Duration.INDEFINITE).show(owner);
        Notifications.setDefaultDuration(Duration.INDEFINITE);
    }

    private void testScreen(Window owner) {
        if (Screen.getScreens().size() < 2) {
            System.err.println("Screen-Funktionionalität kann nicht mit nur einem Screen getestet werden!");
            return;
        }
        Notifications.createNotification(Notification.Type.INFO).show(owner);
        Notifications.createNotification(Notification.Type.INFO).show(owner);
        Notifications.setScreen(Screen.getScreens().get(1));
        Notifications.createNotification(Notification.Type.INFO).show(owner);
        Notifications.createNotification(Notification.Type.INFO).show(owner);
        Notifications.setScreen(Screen.getScreens().get(0));
        //TODO bindScreenToOwner
    }

    private void testCheckBox(Window owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void testExitButton(Window owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void testAlignment(Window owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
