package de.pheru.fx.controls.notificationbar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Philipp Bruckner
 */
public class TestClassNotificationBar extends Application {

    private NotificationBar notificationBar;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        notificationBar = new NotificationBar();

        TextField tf = new TextField();

        VBox v = new VBox();
        v.getChildren().add(notificationBar);
        v.getChildren().add(new Label("Test"));
        TableView<Object> tableView = new TableView<>();
        v.getChildren().add(tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        v.getChildren().add(tf);
        Button b = new Button("Add Message");

        final NotificationBar.Element[] e = new NotificationBar.Element[1];
        b.setOnAction(event -> {
            if (tf.getText().isEmpty()) {
                VBox vb = new VBox(new Label("Test"), new ProgressBar(0.5), new Button("Button"));
                e[0] = notificationBar.addElement(NotificationBar.Type.ERROR, vb);
            } else {
                notificationBar.addElement(NotificationBar.Type.INFO, tf.getText());
            }
        });
        v.getChildren().add(b);
        Button b2 = new Button("Add property");
        b2.setOnAction(event -> {
//            e[0].close();
            notificationBar.addElement(NotificationBar.Type.WARNING, tf.getText());
        });
        v.getChildren().add(b2);

        HBox h = new HBox();
        Label hl = new Label("asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd ");
        hl.setWrapText(true);
        h.getChildren().add(hl);
        primaryStage.setScene(new Scene(v));
        primaryStage.show();
    }

}
