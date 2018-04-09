package de.pheru.fx.controls.filledslider;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class FilledSlider extends Control {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FilledSliderSkin(this);
    }
}
