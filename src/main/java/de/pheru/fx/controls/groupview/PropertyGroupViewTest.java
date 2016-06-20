package de.pheru.fx.controls.groupview;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Philipp on 11.06.2016.
 */
public class PropertyGroupViewTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        PropertyGroupTileView<Titel> g = new PropertyGroupTileView<>("interpret");
        g.setTileFactory(param -> {
            VBox v = new VBox();
            v.getChildren().add(new Separator(Orientation.HORIZONTAL));
            v.getChildren().add(new Label(String.valueOf(param.getItemsUnmodifiable().size())));
            v.getChildren().add(new Label(param.getPropertyValue().toString()));
            v.getChildren().add(new Separator(Orientation.HORIZONTAL));
            TableView<String> t = new TableView<>();
            t.setMaxHeight(200);
            t.setMaxWidth(200);
            v.getChildren().add(t);
            return v;
        });
//        g.setRowValignment(VPos.BOTTOM);
//        g.setColumnHalignment(HPos.RIGHT);
//        g.setMinHeight(600);
//        g.setHgap(Double.MAX_VALUE);
//        g.setvBarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//        g.sethBarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        g.getItems().add(new Titel("Kamelot"));
        g.getItems().add(new Titel("Gamma Ray"));
        g.getItems().add(new Titel("Kamelot"));
        g.getItems().add(new Titel("Kamelot"));
        g.getItems().add(new Titel("Kamelot"));
        g.getItems().add(new Titel("Gamma Ray"));
        g.getItems().add(new Titel("Gamma Ray2"));
        g.getItems().add(new Titel("Gamma Ray2"));
        g.getItems().add(new Titel("Gamma Ray2"));

        primaryStage.setScene(new Scene(g));
        primaryStage.show();
    }

    public static class Titel {
        private String interpret;

        public Titel(String interpret) {
            this.interpret = interpret;
        }

        public String getInterpret() {
            return interpret;
        }

        public void setInterpret(String interpret) {
            this.interpret = interpret;
        }

        @Override
        public String toString() {
            return interpret;
        }
    }
}
