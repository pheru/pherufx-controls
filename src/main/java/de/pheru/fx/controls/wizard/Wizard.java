package de.pheru.fx.controls.wizard;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Locale;
import java.util.ResourceBundle;

public class Wizard<T> {

    private BorderPane root;
    private Button backButton;
    private Button nextButton;
    private Button finishButton;

    private final ObservableList<WizardStep<T>> steps = FXCollections.observableArrayList();
    private final ObjectProperty<WizardStep<T>> currentStep = new SimpleObjectProperty<>();
    private final T model;

    private Stage stage;
    private Window owner;
    private EventHandler<? super WizardEvent<T>> onFinish;
    private Modality modality = Modality.NONE;

    public Wizard(final T model, final WizardStep<T>... steps) {
        this.model = model;
        initUI();
        initListeners();

        this.steps.addAll(steps);
    }

    private void initUI() {
        backButton = new Button();
        backButton.setOnAction(event -> back());
        nextButton = new Button();
        nextButton.setOnAction(event -> next());
        finishButton = new Button();
        finishButton.setOnAction(event -> finish());
        setButtonTextByResourceBundle(ResourceBundle.getBundle("de.pheru.fx.controls.wizard.bundle.wizard", Locale.getDefault()));

        final HBox buttonBox = new HBox(backButton, nextButton, finishButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setSpacing(10.0);

        root = new BorderPane();
        root.setPadding(new Insets(10.0));
        VBox bottomBox = new VBox(new Separator(Orientation.HORIZONTAL), buttonBox);
        bottomBox.setSpacing(3.0);
        root.setBottom(bottomBox);
    }

    private void initListeners() {
        steps.addListener((ListChangeListener<WizardStep<T>>) c -> {
            while (c.next()) {
                if (c.wasAdded() && currentStep.get() == null) {
                    currentStep.set(c.getAddedSubList().get(0));
                } else if (c.wasRemoved()) {
                    if (steps.isEmpty()) {
                        currentStep.set(null);
                        hide();
                    } else if (c.getRemoved().contains(currentStep.get())) {
                        if (c.getFrom() > 0) {
                            currentStep.set(steps.get(c.getFrom() - 1));
                        } else {
                            currentStep.set(null);
                            hide();
                        }
                    }
                }
            }
        });
        currentStep.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                rebindButtons(newValue);
                root.setCenter(newValue.activate(model));
            } else {
                unbindButtons();
                root.setCenter(null);
            }
        });
    }

    private void rebindButtons(final WizardStep<T> newCurrentStep) {
        unbindButtons();

        final ObservableBooleanValue backDisabledObservable = newCurrentStep.backDisabled(model);
        backButton.disableProperty().bind(new ButtonDisableBinding(
                () -> steps.indexOf(newCurrentStep) == 0 || backDisabledObservable.get(),
                backDisabledObservable));

        final ObservableBooleanValue nextDisabledObservable = newCurrentStep.nextDisabled(model);
        nextButton.disableProperty().bind(new ButtonDisableBinding(
                () -> steps.indexOf(newCurrentStep) == steps.size() - 1 || nextDisabledObservable.get(),
                nextDisabledObservable, Bindings.size(steps)));

        final ObservableBooleanValue finishDisabledObservable = newCurrentStep.finishDisabled(model);
        finishButton.disableProperty().bind(new ButtonDisableBinding(
                finishDisabledObservable::get, finishDisabledObservable));
    }

    private void unbindButtons() {
        backButton.disableProperty().unbind();
        nextButton.disableProperty().unbind();
        finishButton.disableProperty().unbind();
    }

    public void next() {
        final int currentIndex = steps.indexOf(currentStep.get());
        final WizardStep<T> nextStep = steps.get(currentIndex + 1);
        currentStep.set(nextStep);
    }

    public void back() {
        final int currentIndex = steps.indexOf(currentStep.get());
        final WizardStep<T> previousStep = steps.get(currentIndex - 1);
        currentStep.set(previousStep);
    }

    public void finish() {
        if (onFinish != null) {
            onFinish.handle(new WizardEvent<>(model));
        }
        hide();
    }

    public void show() {
        if (stage == null) {
            stage = new Stage();
            stage.initModality(modality);
            if (owner != null) {
                stage.initOwner(owner);
            }
            stage.setScene(new Scene(root));
        }
        if (!stage.isShowing()) {
            stage.show();
        }
    }

    public void hide() {
        if (stage != null) {
            stage.hide();
        }
    }

    public void setButtonTextByResourceBundle(final ResourceBundle resourceBundle) {
        setBackButtonText(resourceBundle.getString("back"));
        setNextButtonText(resourceBundle.getString("next"));
        setFinishButtonText(resourceBundle.getString("finish"));
    }

    public void setBackButtonText(final String text) {
        backButton.setText(text);
    }

    public void setNextButtonText(final String text) {
        nextButton.setText(text);
    }

    public void setFinishButtonText(final String text) {
        finishButton.setText(text);
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
        return model;
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
