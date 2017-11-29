package de.pheru.fx.controls.demo.wizard;

import de.pheru.fx.controls.wizard.WizardStep;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class TestStep2 implements WizardStep<TestModel> {

    @Override
    public Node activate(final TestModel model) {
        TextField field = new TextField();
        field.textProperty().bindBidirectional(model.zweiProperty());
        return new HBox(new Label("2"), field);
    }

    @Override
    public ObservableBooleanValue finishDisabled(final TestModel model) {
        return new SimpleBooleanProperty(false);
    }

    @Override
    public ObservableBooleanValue nextDisabled(final TestModel model) {
        return new SimpleBooleanProperty(false);
    }
}
