package de.pheru.fx.controls.demo.wizard;

import de.pheru.fx.controls.wizard.WizardStep;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class TestStep1 implements WizardStep<TestModel> {

    @Override
    public Node activate(final TestModel model) {
        TextField field = new TextField();
        field.textProperty().bindBidirectional(model.einsProperty());
        return new HBox(new Label("1"), field);
    }

    @Override
    public ObservableBooleanValue finishDisabled(final TestModel model) {
        return new SimpleBooleanProperty(true);
    }

    @Override
    public ObservableBooleanValue nextDisabled(final TestModel model) {
        return model.einsProperty().isEmpty();
    }
}
