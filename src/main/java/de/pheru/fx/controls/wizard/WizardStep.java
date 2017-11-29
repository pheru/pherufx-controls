package de.pheru.fx.controls.wizard;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;

public interface WizardStep<T> {

    Node activate(final T model);

    ObservableBooleanValue finishDisabled(final T model);

    ObservableBooleanValue nextDisabled(final T model);

    default ObservableBooleanValue backDisabled(final T model) {
        return new SimpleBooleanProperty(false);
    }
}
