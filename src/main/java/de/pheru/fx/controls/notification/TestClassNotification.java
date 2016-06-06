package de.pheru.fx.controls.notification;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Philipp Bruckner
 */
public class TestClassNotification extends Application {

    private Stage primaryStage;

    private ComboBox<Notification.Type> typeComboBox;
    private final BooleanProperty booleanProperty = new SimpleBooleanProperty(false);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        VBox content = new VBox();
        content.setSpacing(2.0);

        content.getChildren().add(createTypeComboBox());
        //Defaults
        content.getChildren().add(new Label("Defaults:"));
        content.getChildren().add(createPositionComboBox());
        content.getChildren().add(createCheckBox("HideOnMouseClicked", Notification.getDefaults().hideOnMouseClickedProperty()));
        content.getChildren().add(createCheckBox("AnimateShow", Notification.getDefaults().animateShowProperty()));
        content.getChildren().add(createCheckBox("CloseButtonVisible", Notification.getDefaults().closeButtonVisibleProperty()));
        content.getChildren().add(createCheckBox("FadeOut", Notification.getDefaults().fadeOutProperty()));
        content.getChildren().add(createCheckBox("StyleByType", Notification.getDefaults().styleByTypeProperty()));
        content.getChildren().add(createCheckBox("PlaySound", Notification.getDefaults().playSoundProperty()));
        content.getChildren().add(createDurationTextField());

        content.getChildren().add(new Separator(Orientation.HORIZONTAL));

        content.getChildren().add(createSingleAddButton());
        content.getChildren().add(createWindowButton());
        content.getChildren().add(createScreenButton());
        content.getChildren().add(createDurationButton());
        content.getChildren().add(createCustomContentButton());
        content.getChildren().add(createDontShowAgainButton());

        primaryStage.setScene(new Scene(content));
        primaryStage.show();
    }

    private TextField createDurationTextField() {
        TextField textField = new TextField("");
        textField.setPromptText("Duration (leer für INDEFINITE)");
        Bindings.bindBidirectional(textField.textProperty(), Notification.getDefaults().durationProperty(), new StringConverter<Duration>() {
            @Override
            public String toString(Duration object) {
                return object.toString();
            }

            @Override
            public Duration fromString(String string) {
                if (string.isEmpty()) {
                    return Duration.INDEFINITE;
                }
                return Duration.valueOf(string);
            }
        });
        return textField;
    }

    private CheckBox createCheckBox(String text, BooleanProperty prop) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.selectedProperty().bindBidirectional(prop);
        return checkBox;
    }

    private Button createSingleAddButton() {
        Button button = new Button("Single Add (Defaults)");
        button.setOnAction(event -> {
            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), "Text - SingleAdd", "Header");
            n.show();
        });
        return button;
    }

    private Button createWindowButton() {
        Button button = new Button("Window");
        button.setOnAction(event -> {
            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), "Text", "Header");
            n.show();
        });
        return button;
    }

    private Button createScreenButton() {
        Button button = new Button("Screen");
        button.setOnAction(event -> {
            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), "Text", "Header");
            n.show();
        });
        return button;
    }

    private Button createDurationButton() {
        Button button = new Button("Duration");
        button.setOnAction(event -> {
            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), "Text", "Header");
            n.show();
        });
        return button;
    }

    private Button createCustomContentButton() {
        Button button = new Button("Custom Content");
        button.setOnAction(event -> {
            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), "Text", "Header");
            n.show();
        });
        return button;
    }

    private Button createDontShowAgainButton() {
        Button button = new Button("Dont Show Again");
        button.setOnAction(event -> {
            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), "Text", "Header");
            n.show();
        });
        return button;
    }

    private Button createButton() {
        Button button = new Button("");
        button.setOnAction(event -> {
            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), "Text", "Header");
            n.show();
        });
        return button;
    }

    private ComboBox<Pos> createPositionComboBox() {
        ComboBox<Pos> positionBox = new ComboBox<>(FXCollections.observableArrayList(Pos.values()));
        positionBox.getSelectionModel().select(0);
        Notification.getDefaults().positionProperty().bind(positionBox.getSelectionModel().selectedItemProperty());
        return positionBox;
    }

    private ComboBox<Notification.Type> createTypeComboBox() {
        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().setAll(Notification.Type.values());
        typeComboBox.getSelectionModel().selectFirst();
        return typeComboBox;
    }

}
