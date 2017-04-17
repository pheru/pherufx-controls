package de.pheru.fx.controls.wizard;

import javafx.scene.Node;

/**
 * Created by Philipp on 23.09.2016.
 */
public interface WizardStep<T> {

    Node init(final Wizard<T> wizard);
}
