package de.pheru.fx.controls.wizard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Philipp on 07.10.2016.
 */
public class TestWizard extends Application{

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Wizard<String>().show();
    }
}
