package de.pheru.fx.controls.marquee;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

class TestMarquee extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Label label = new Label("Label");
        label.setMaxWidth(30);

        final Marquee marquee = new Marquee("White Pearl, Black Ocean");
        marquee.setStyle("-fx-background-color: white");

        Button b = new Button("+");
        b.setOnAction(event -> {
//            marquee.setMaxWidth(marquee.getMaxWidth() + 1);
            marquee.setText(marquee.getText() + " Blubb");
            label.setText(label.getText() + " Text");
        });

        Button br = new Button("reset");
        br.setOnAction(event -> marquee.setText("Blubb"));

        Button bd = new Button("Delay");
        bd.setOnAction(event -> {
            marquee.setAnimationEndDelay(Duration.millis(9999));
        });

        label.setStyle("-fx-background-color: yellow");
        HBox box = new HBox(b,
//                new Text("Text"),
//                label,
                marquee,
                br,
                bd);
//        marquee.setAnimationSpeed(3);
        marquee.setInfiniteScroll(true);
        box.setAlignment(Pos.CENTER_LEFT);
        marquee.setMaxWidth(100);
//        marquee.setPrefWidth(200);
//        marquee.setMinWidth(0);
        box.setMaxWidth(400);
        box.setMinWidth(400);

//        marquee.setAnimationEndDelay(Duration.ZERO);
//        marquee.setAnimationStartDelay(Duration.ZERO);
        box.setStyle("-fx-background-color: red;");
        final Scene scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
