package de.pheru.fx.controls.notificationbar;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Bruckner
 */
public class TestClassNotificationBar extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<NotificationBar.Element> elements = new ArrayList<>();

        VBox box = new VBox();

        MenuBar menuBar = new MenuBar(new Menu("Menu"));
        box.getChildren().add(menuBar);

        NotificationBar notificationBar = new NotificationBar();
        box.getChildren().add(notificationBar);

        TableView<Object> tableView = new TableView<>();
        tableView.setMaxHeight(250);
        box.getChildren().add(tableView);

        ComboBox<NotificationBar.Type> comboBox = new ComboBox<>();
        comboBox.getItems().setAll(NotificationBar.Type.values());
        comboBox.getSelectionModel().selectFirst();
        box.getChildren().add(comboBox);

        Button previousButton = new Button("Previous");
        previousButton.setOnAction(event -> notificationBar.previous());
        box.getChildren().add(previousButton);

        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> notificationBar.next());
        box.getChildren().add(nextButton);

//        TitledPane tp = new TitledPane();
//        Label label = new Label();
//        label.setWrapText(true);
//        label.setAlignment(Pos.TOP_LEFT);
//        label.setText("a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a ");
//        tp.setContent(new VBox(new Label("Ich bin in einer VBox!"), new ProgressBar(-1), new Button("Ich bin auch in einer VBox!")));
//        VBox.setVgrow(tp, Priority.ALWAYS);
//        box.getChildren().add(tp);
//
//        Platform.runLater(() -> {
//            Pane title = (Pane) tp.lookup(".title");
//            if (title != null) {
//                title.setVisible(false);
//                title.setManaged(false);
//                title.setMaxHeight(0.0);
//                title.setMinHeight(0.0);
//                title.setPrefHeight(0.0);
//            }
//        });
//
//        Button tpb = new Button("TP");
//        tpb.setOnAction(event -> {
//            tp.setExpanded(!tp.isExpanded());
//        });
//        box.getChildren().add(tpb);
//        VBox.setVgrow(tp, Priority.ALWAYS);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> notificationBar.close());
        box.getChildren().add(closeButton);

        Button closeFirstListElementButton = new Button("Close first list element");
        closeFirstListElementButton.setOnAction(event -> {
            elements.get(0).close();
            elements.remove(0);
        });
        box.getChildren().add(closeFirstListElementButton);

        TextField textField = new TextField();
        box.getChildren().add(textField);

        Button addTextButton = new Button("Add Text");
        addTextButton.setOnAction(event -> elements.add(notificationBar.addElement(comboBox.getSelectionModel().getSelectedItem(), textField.getText())));
        box.getChildren().add(addTextButton);

        Button addNodeButton = new Button("Add Node");
        addNodeButton.setOnAction(event -> elements.add(notificationBar.addElement(comboBox.getSelectionModel().getSelectedItem(),
                new VBox(new Label("Ich bin in einer VBox!"), new ProgressBar(-1), new Button("Ich bin auch in einer VBox!")))));
        box.getChildren().add(addNodeButton);


        primaryStage.setScene(new Scene(box));
        primaryStage.show();
    }

}
