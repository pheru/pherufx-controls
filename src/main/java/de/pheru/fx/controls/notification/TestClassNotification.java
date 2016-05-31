package de.pheru.fx.controls.notification;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Bruckner
 */
public class TestClassNotification extends Application {

    Stage stage;

    private ComboBox<Notification.Type> typeBox;
    private final BooleanProperty b = new SimpleBooleanProperty(true);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setScene(new Scene(createTestInterface(primaryStage)));
        primaryStage.show();
    }

    private VBox createTestInterface(Window owner) {
        ComboBox<Pos> positionBox = new ComboBox<>(FXCollections.observableArrayList(Pos.values()));
        positionBox.getSelectionModel().select(0);
        Notification.getDefaults().positionProperty().bind(positionBox.getSelectionModel().selectedItemProperty());

        typeBox = new ComboBox<>();
        typeBox.getItems().setAll(Notification.Type.values());
        typeBox.getSelectionModel().selectFirst();

        Button showButton = new Button("Show");
        showButton.setOnAction((ActionEvent event) -> testShow());

        Button testButton = new Button("Test");
        testButton.setOnAction((ActionEvent event) -> testTest(owner));

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

        Button layoutButton = new Button("Layout");
        layoutButton.setOnAction((ActionEvent event) -> testLayout());

        Button hideAllButton = new Button("Hide All (X)");
//        hideAllButton.setOnAction((ActionEvent event) -> Notification.hideAll(null));

        VBox box = new VBox(positionBox, typeBox, showButton, testButton, allgButton, tonButton, timerButton, checkBoxButton, exitButtonButton,
                layoutButton, hideAllButton);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(3);
        box.setMinWidth(200);
        return box;
    }

    private void testShow() {
        Notification n = new Notification(typeBox.getSelectionModel().getSelectedItem(), "Test");
        n.setDuration(Duration.INDEFINITE);
        n.show();
    }

    private void testTest(Window owner) {
        for (Notification.Type type : Notification.Type.values()) {
            HBox h = new HBox(new Button("Ja"), new Button("Nein"));
            h.setSpacing(5);
            Notification notification = new Notification(type, new VBox(new Label("Blubb"), h));
            notification.setDuration(Duration.INDEFINITE);
//            notification.show();
        }
        StringProperty s = new SimpleStringProperty("Test");
        TableView<String> t = new TableView<>();
        t.setPrefHeight(200);
        Notification n = new Notification(typeBox.getValue(), t);
//        n.setWindow(owner);
        n.show();
        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(()->{

            t.setMinHeight(400);
            });
        }).start();
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
        new Notification(typeBox.getSelectionModel().getSelectedItem(), new VBox(new Label("Lade irgendwas..."), pBar)).show();
        Notification n = new Notification(typeBox.getSelectionModel().getSelectedItem(), new Label("CustomContent"));
        n.bindDontShowAgainProperty(new SimpleBooleanProperty(true));
        n.show();
//        new Notification(Notification.Type.ERROR, new Label("CustomContent - Error")).show();
//        new Notification(Notification.Type.INFO, new Label("CustomContent - Info")).show();
//        new Notification(Notification.Type.WARNING, new Label("CustomContent - Warning")).show();
        new Notification(Notification.Type.INFO, "Info").show();
        new Notification(Notification.Type.NONE, "NONE", "ASDÖLKJSDLKJD").show();
        new Notification(Notification.Type.NONE, "NONE2").show();
        new Notification(Notification.Type.INFO, "Info").show();
        new Notification(Notification.Type.WARNING, "Warning").show();
        new Notification(Notification.Type.WARNING, "Warning").show();
        new Notification(Notification.Type.ERROR, "Error - Und zwar ein ganz, ganz, ganz langer! Oh ja, da schauste!").show();
        new Notification(Notification.Type.ERROR, "Error - Und zwar ein ganz, ganz, ganz langer! Oh ja, da schauste!").show();
//        new Notification(Notification.Type.INFO, "Info", "Info").show();
//        new Notification(Notification.Type.WARNING, "Warning", "Warning").show();
//        new Notification(Notification.Type.ERROR, "Error", "Error").show();

        Thread t2 = new Thread(t);
        t2.setDaemon(true);
        t2.start();

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void testTon() {
//        Notification.getDefaults().setDuration(Duration.seconds(3));
        Thread t = new Thread(() -> {
            stage = null;
            try {
                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Ton 1").show()); // Ton
                Thread.sleep(1000);
                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Ton \n2").show()); // Ton
                Thread.sleep(1000);
//                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Kein Ton 2").show();  //Kein Ton
//                Thread.sleep(1000);
//                Notification.getDefaults().setPlaySound(true);
//                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Ton 3").show(); //Ton
//                Thread.sleep(1000);
//                Platform.runLater(() -> new Notification(Notification.Type.INFO, "Kein Ton 4").show(); //Kein Ton
//                Notification.getDefaults().setPlaySound(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void testLayout() {
        Thread t = new Thread(() -> {
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
        });
        t.setDaemon(true);
        t.start();
    }

    private void testTimer() {
        new Notification(Notification.Type.INFO, "Default (Indefinite)").show();
        Notification notification = new Notification(Notification.Type.INFO, "Indefinite");
        notification.setDuration(Duration.INDEFINITE);
        notification.show();
        Notification notification1 = new Notification(Notification.Type.INFO, "3 Sekunden");
        notification1.setDuration(Duration.seconds(3));
        notification1.show();

        Notification.getDefaults().setDuration(Duration.seconds(3));

        new Notification(Notification.Type.INFO, "Default (3 Sekunden)").show();
        Notification notification2 = new Notification(Notification.Type.INFO, "5 Sekunden");
        notification2.setDuration(Duration.seconds(5));
        notification2.show();
        Notification notification3 = new Notification(Notification.Type.INFO, "Indefinite");
        notification3.setDuration(Duration.INDEFINITE);
        notification3.show();
        Notification.getDefaults().setDuration(Duration.INDEFINITE);
    }

    private void testCheckBox() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void testExitButton() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
