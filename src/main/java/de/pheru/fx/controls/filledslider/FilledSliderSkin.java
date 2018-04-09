package de.pheru.fx.controls.filledslider;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

import java.net.URL;

public class FilledSliderSkin extends SkinBase<FilledSlider> {

    private final StackPane stackPane = new StackPane();
    private final ProgressBar progressBar = new ProgressBar();
    private final Slider slider = new Slider();

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected FilledSliderSkin(final FilledSlider control) {
        super(control);
        //TODO Stylesheet
        initSlider();
        initProgressbar();
        initStackpane();
        getChildren().setAll(stackPane);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) {
                System.out.println("Slider: " + newValue.doubleValue());
                System.out.println("Progress: " + (newValue.doubleValue() / slider.getMax()));
            }
        });
    }

    private void initProgressbar() {
        progressBar.setProgress(0.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.progressProperty().bind(slider.valueProperty().divide(slider.maxProperty()));
    }

    private void initSlider() {
        slider.setValue(0.0);
        slider.setMaxWidth(Double.MAX_VALUE);
    }

    private void initStackpane() {
        final String stylesheet = getClass().getResource("filledslider.css").toString();
        stackPane.getStylesheets().setAll(stylesheet);
        stackPane.getChildren().setAll(progressBar, slider);
    }
}
