package de.pheru.fx.controls;

import de.pheru.fx.controls.notification.Notification;
import de.pheru.fx.controls.notification.NotificationManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
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
        Button allgButton = new Button("Allgemein");
        allgButton.setOnAction((ActionEvent event) -> testAllg());

        Button tonButton = new Button("Ton");
        tonButton.setOnAction((ActionEvent event) -> testTon());

        Button timerButton = new Button("Timer");
        timerButton.setOnAction((ActionEvent event) -> testTimer());

        Button checkBoxButton = new Button("CheckBox");
        checkBoxButton.setOnAction((ActionEvent event) -> testCheckBox());

        Button exitButtonButton = new Button("ExitButton");
        exitButtonButton.setOnAction((ActionEvent event) -> testExitButton());

        Button alignmentButton = new Button("Alignment");
        alignmentButton.setOnAction((ActionEvent event) -> testAlignment());

        Button screenButton = new Button("Screen");
        screenButton.setOnAction((ActionEvent event) -> testScreen(owner));

        Button layoutButton = new Button("Layout");
        layoutButton.setOnAction((ActionEvent event) -> testLayout());

        Button positionButton = new Button("Position");
        positionButton.setOnAction((ActionEvent event) -> testPosition());

        Button hideAllButton = new Button("Hide All");
        hideAllButton.setOnAction((ActionEvent event) -> NotificationManager.hideAll());

        VBox box = new VBox(allgButton, tonButton, timerButton, checkBoxButton, exitButtonButton, alignmentButton,
                screenButton, layoutButton, positionButton, hideAllButton);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(3);
        box.setMinWidth(200);
        return box;
    }

    private void testAllg() {
        Task<Void> t = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(-1, 100);
                Thread.sleep(2000);
                updateProgress(1, 100);
                Thread.sleep(1500);
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(50);
                    updateProgress(i + 1, 100);
                }
                return null;
            }
        };
        ProgressBar pBar = new ProgressBar(-1);
        pBar.setPrefWidth(500);
        pBar.progressProperty().bind(t.progressProperty());
        new Notification(new VBox(new Label("Lade irgendwas..."), pBar)).show();
        Notification n = new Notification(new Label("CustomContent"));
        n.bindDontShowAgainProperty(new SimpleBooleanProperty(true));
        n.show();
//        new Notification(Notification.Type.ERROR, new Label("CustomContent - Error")).show();
//        new Notification(Notification.Type.INFO, new Label("CustomContent - Info")).show();
//        new Notification(Notification.Type.WARNING, new Label("CustomContent - Warning")).show();
        new Notification(Notification.Type.INFO, "Info").show();
        new Notification(Notification.Type.INFO, "Info").show();
        new Notification(Notification.Type.WARNING, "Warning").show();
        new Notification(Notification.Type.WARNING, "Warning").show();
        new Notification(Notification.Type.ERROR, "Error - Und zwar ein ganz, ganz, ganz langer! Oh ja, da schauste!").show();
        new Notification(Notification.Type.ERROR, "Error - Und zwar ein ganz, ganz, ganz langer! Oh ja, da schauste!").show();
//        new Notification(Notification.Type.INFO, "Info", "Info").show();
//        new Notification(Notification.Type.WARNING, "Warning", "Warning").show();
//        new Notification(Notification.Type.ERROR, "Error", "Error").show();

        new Thread(t).start();
    }

    private void testTon() {
        new Thread(() -> {
            try {
                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Ton").show(true)); // Ton
                Thread.sleep(1000);
                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Kein Ton").show());  //Kein Ton
                Thread.sleep(1000);
                NotificationManager.setPlaySound(true);
                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Ton ").show()); //Ton
                Thread.sleep(1000);
                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Kein Ton").show(false)); //Kein Ton
                NotificationManager.setPlaySound(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void testLayout() {
        new Thread(() -> {
            try {
                Notification mitHeader = new Notification(Notification.Type.WARNING, "Mit Header", "Header");
                Notification ohneHeader = new Notification(Notification.Type.INFO, "Ohne Header");
                StringProperty s = new SimpleStringProperty("init");
                StringProperty s2 = new SimpleStringProperty("init2");
                StringProperty s3 = new SimpleStringProperty("init3");
                StringProperty s4 = new SimpleStringProperty("init4");
                Notification varSize = new Notification(Notification.Type.ERROR, s, s2);
                Notification varSize2 = new Notification(Notification.Type.INFO, s3, s4);

                Platform.runLater(() -> {
                    mitHeader.show();
                    ohneHeader.show();
                    varSize.show();
                    varSize2.show();
                });
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    s.set("1 ghzhhh h hn n njjujhh zb zbt tghhhhjju hjj h  g vv b v bnhjjkj");
                    s2.set("2 ghzhhh h hn n njjujhh zb zbt tghhhhjju hjj h  g vv b v bnhjjkj");
                    s3.set("3 ghzhhh h hn n njjujhh zb zbt tghhhhjju hjj h  g vv b v bnhjjkj");
                    s4.set("4 ghzhhh h hn n njjujhh zb zbt tghhhhjju hjj h  g vv b v bnhjjkj");
                });
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    s.set("1 gkj");
                    s2.set("2 ghjkj");
                    s3.set("3 jkj");
                    s4.set("4 jjkj");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void testPosition() {
        new Notification(Notification.Type.INFO, "Eins - 0").show(0);
        new Notification(Notification.Type.INFO, "Zwei - 1").show(1);
        Notification drei = new Notification(Notification.Type.INFO, "Drei - 0");
        drei.show(0);
        new Notification(Notification.Type.INFO, "Vier - 2").show(2);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    drei.hide(false);
                    drei.show(0);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void testTimer() {
        new Notification(Notification.Type.INFO, "Default (Indefinite)").show();
        Notification notification = new Notification(Notification.Type.INFO, "Indefinite");
        notification.setDuration(Duration.INDEFINITE);
        notification.show();
        Notification notification1 = new Notification(Notification.Type.INFO, "3 Sekunden");
        notification1.setDuration(Duration.seconds(3));
        notification1.show();

        NotificationManager.setDefaultDuration(Duration.seconds(3));

        new Notification(Notification.Type.INFO, "Default (3 Sekunden)").show();
        Notification notification2 = new Notification(Notification.Type.INFO, "5 Sekunden");
        notification2.setDuration(Duration.seconds(5));
        notification2.show();
        Notification notification3 = new Notification(Notification.Type.INFO, "Indefinite");
        notification3.setDuration(Duration.INDEFINITE);
        notification3.show();
        NotificationManager.setDefaultDuration(Duration.INDEFINITE);
    }

    private void testScreen(Window owner) {
        if (Screen.getScreens().size() < 2) {
            System.err.println("Screen-Funktionionalität kann nicht mit nur einem Screen getestet werden!");
            return;
        }
        new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    new Notification(Notification.Type.INFO, "asd").show();
                    new Notification(Notification.Type.INFO, "asdasd").show();
                    new Notification(Notification.Type.INFO, "sadf").show();
                    new Notification(Notification.Type.INFO, "fdesdf").show();
                });
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    NotificationManager.setScreen(Screen.getScreens().get(1));
                });
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    NotificationManager.setScreen(Screen.getScreens().get(0));
                });
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    NotificationManager.setBoundOwner(owner);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void testCheckBox() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void testExitButton() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void testAlignment() {
        new Notification(Notification.Type.INFO, "Eins").show(true);
        new Notification(Notification.Type.INFO, "Zwei").show(true);
        new Notification(Notification.Type.INFO, "Drei").show(true);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                NotificationManager.setAlignment(NotificationManager.Alignment.TOP_RIGHT);
                Thread.sleep(3000);
                NotificationManager.setAlignment(NotificationManager.Alignment.TOP_LEFT);
                Thread.sleep(3000);
                NotificationManager.setAlignment(NotificationManager.Alignment.BOTTOM_LEFT);
                Thread.sleep(3000);
                NotificationManager.setAlignment(NotificationManager.Alignment.BOTTOM_RIGHT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
