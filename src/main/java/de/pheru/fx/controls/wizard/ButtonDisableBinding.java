package de.pheru.fx.controls.wizard;

import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;

class ButtonDisableBinding extends BooleanBinding {

    private final ValueComputer valueComputer;

    ButtonDisableBinding(final ValueComputer valueComputer, final Observable... dependencies) {
        this.valueComputer = valueComputer;
        bind(dependencies);
    }

    @Override
    protected boolean computeValue() {
        return valueComputer.compute();
    }

    interface ValueComputer {
        boolean compute();
    }
}