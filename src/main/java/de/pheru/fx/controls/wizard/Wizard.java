package de.pheru.fx.controls.wizard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Philipp on 23.09.2016.
 */
public class Wizard<T> {

    @FXML
    private BorderPane root;
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonNext;
    @FXML
    private Button buttonFinish;

    private final ObjectProperty<T> model = new SimpleObjectProperty<>();
    private final ObservableList<WizardStep<T>> steps = FXCollections.observableArrayList();
    private final ObjectProperty<WizardStep<T>> currentStep = new SimpleObjectProperty<>();

    private Stage stage;
    private Modality modality = Modality.NONE;
    private Window owner = null;

    private EventHandler<? super WizardEvent<T>> onFinish;

    public Wizard() {
        final FXMLLoader fxmlLoader = new FXMLLoader(Wizard.class.getResource("wizard.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("de.pheru.fx.controls.wizard.bundle.wizard"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("TODO", e);
        }

        steps.addListener(new ListChangeListener<WizardStep<T>>() {
            @Override
            public void onChanged(Change<? extends WizardStep<T>> c) {
                while (c.next()) {
                    if (c.wasAdded() && currentStep.get() == null) {
                        currentStep.set(c.getAddedSubList().get(0));
                    } else if (c.getRemoved().contains(currentStep.get())) {
                        if (steps.isEmpty()) {
                            currentStep.set(null);
                        } else if (steps.size() >= c.getFrom()) { //TODO
                            currentStep.set(steps.get(c.getFrom() - 1));
                        }
                    }
                }
                resetButtons(steps.indexOf(currentStep.get()));
            }
        });
        currentStep.addListener((observable, oldValue, newValue) -> {
            resetButtonBindings();
            resetButtons(steps.indexOf(newValue));
            root.setCenter(newValue.init(Wizard.this));
        });
    }

    public Wizard(final WizardStep<T>... steps) {
        this();
        this.steps.addAll(steps);
        currentStep.set(steps[0]);
    }

    private void resetButtonBindings() {
        buttonBack.disableProperty().unbind();
        buttonNext.disableProperty().unbind();
        buttonFinish.disableProperty().unbind();

    }

    private void resetButtons(final int newStepIndex) {
        if (!buttonBack.disableProperty().isBound()) {
            buttonBack.setDisable(newStepIndex == 0);
        }
        if (!buttonNext.disableProperty().isBound()) {
            buttonNext.setDisable(newStepIndex == steps.size() - 1);
        }
        if (!buttonFinish.disableProperty().isBound()) {
            buttonFinish.setDisable(newStepIndex != steps.size() - 1);
        }
    }


    @FXML
    public void next() {
        final int currentIndex = steps.indexOf(currentStep.get());
        final WizardStep<T> nextStep = steps.get(currentIndex + 1);
        currentStep.set(nextStep);
    }

    @FXML
    public void back() {
        final int currentIndex = steps.indexOf(currentStep.get());
        final WizardStep<T> previousStep = steps.get(currentIndex - 1);
        currentStep.set(previousStep);
    }

    @FXML
    public void finish() {
        if (onFinish != null) {
            onFinish.handle(new WizardEvent<>(model.get()));
        }
        hide();
    }

    public void show() {
        if (stage == null || !stage.isShowing()) {
            stage = new Stage();
            stage.initModality(modality);
            if (owner != null) {
                stage.initOwner(owner);
            }
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void hide() {
        if (stage != null) {
            stage.hide();
        }
    }

    public EventHandler<? super WizardEvent<T>> getOnFinish() {
        return onFinish;
    }

    public void setOnFinish(EventHandler<? super WizardEvent<T>> onFinish) {
        this.onFinish = onFinish;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public Window getOwner() {
        return owner;
    }

    public void setOwner(Window owner) {
        this.owner = owner;
    }

    public T getModel() {
        return model.get();
    }

    public ObjectProperty<T> modelProperty() {
        return model;
    }

    public void setModel(final T model) {
        this.model.set(model);
    }

    public ObservableList<WizardStep<T>> getSteps() {
        return steps;
    }

    public WizardStep<T> getCurrentStep() {
        return currentStep.get();
    }

    public ObjectProperty<WizardStep<T>> currentStepProperty() {
        return currentStep;
    }

    public void setCurrentStep(WizardStep<T> currentStep) {
        this.currentStep.set(currentStep);
    }
}
