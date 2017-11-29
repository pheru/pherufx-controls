package de.pheru.fx.controls.demo.wizard;

import de.pheru.fx.controls.wizard.Wizard;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestWizard extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Wizard<TestModel> wizard = new Wizard<>(new TestModel(), new TestStep1(), new TestStep2(), new TestStep3());
        wizard.show();
    }
}
