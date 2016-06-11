package de.pheru.fx.controls.notification;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.StringConverter;

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
        content.getChildren().add(createScreenComboBox());
        content.getChildren().add(createWindowComboBox());
        content.getChildren().add(createCheckBox("HideOnMouseClicked", Notification.getDefaults().hideOnMouseClickedProperty()));
        content.getChildren().add(createCheckBox("AnimateShow", Notification.getDefaults().animateShowProperty()));
        content.getChildren().add(createCheckBox("CloseButtonVisible", Notification.getDefaults().closeButtonVisibleProperty()));
        content.getChildren().add(createCheckBox("FadeOut", Notification.getDefaults().fadeOutProperty()));
        content.getChildren().add(createCheckBox("StyleByType", Notification.getDefaults().styleByTypeProperty()));
        content.getChildren().add(createCheckBox("PlaySound", Notification.getDefaults().playSoundProperty()));
        content.getChildren().add(new Label("  Duration:"));
        content.getChildren().add(createDurationTextField("Duration", Notification.getDefaults().durationProperty()));
        content.getChildren().add(new Label("  Anim.Duration:"));
        content.getChildren().add(createDurationTextField("AnimationDuration", Notification.getDefaults().animationDurationProperty()));
        content.getChildren().add(new Label("  Fade Duration:"));
        content.getChildren().add(createDurationTextField("FadeOutDuration", Notification.getDefaults().fadeOutDurationProperty()));
        content.getChildren().add(createStylesheetCheckBox());

        content.getChildren().add(new Separator(Orientation.HORIZONTAL));

        content.getChildren().add(createSingleAddButton());
        content.getChildren().add(createCustomContentButton());
        content.getChildren().add(createDontShowAgainButton());

        primaryStage.setScene(new Scene(content));
        primaryStage.show();
    }

    private TextField createDurationTextField(String text, ObjectProperty<Duration> property) {
        TextField textField = new TextField("");
        textField.setPromptText(text + " (leer für INDEFINITE)");
        Bindings.bindBidirectional(textField.textProperty(), property, new StringConverter<Duration>() {
            @Override
            public String toString(Duration object) {
                return String.valueOf(object.toMillis());
            }

            @Override
            public Duration fromString(String string) {
                if (string.isEmpty()) {
                    return Duration.INDEFINITE;
                }
                return Duration.millis(Double.valueOf(string));
            }
        });
        return textField;
    }

    private CheckBox createCheckBox(String text, BooleanProperty prop) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.selectedProperty().bindBidirectional(prop);
        return checkBox;
    }

    private CheckBox createStylesheetCheckBox() {
        CheckBox checkBox = new CheckBox("Stylesheet");
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Notification.getDefaults().getStyleSheets().add(getClass().getResource("test.css").toExternalForm());
            } else {
                Notification.getDefaults().getStyleSheets().clear();
            }
        });
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

    private Button createCustomContentButton() {
        Button button = new Button("Custom Content");
        button.setOnAction(event -> {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(100);
                        updateProgress(i, 100);
                    }
                    return null;
                }
            };
            VBox content = new VBox();

            ProgressBar progressBar = new ProgressBar(-1);
            progressBar.progressProperty().bind(task.progressProperty());
            content.getChildren().add(progressBar);

            Label label = new Label("Test");
            content.getChildren().add(label);

            Button button1 = new Button("Test-Button");
            content.getChildren().add(button1);
            button1.setOnAction(event1 -> new Thread(task).start());

            TableView<String> tableView = new TableView<>();
            tableView.setMaxHeight(200);
            tableView.setMaxWidth(200);
            content.getChildren().add(tableView);

            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), content);
            n.show();
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    Platform.runLater(() -> n.setPosition(Pos.CENTER));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        return button;
    }

    private Button createDontShowAgainButton() {
        Button button = new Button("Dont Show Again");
        button.setOnAction(event -> {
            Notification n = new Notification(typeComboBox.getSelectionModel().getSelectedItem(), "Text", "Header");
            n.bindDontShowAgainProperty(booleanProperty);
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

    private ComboBox<Screen> createScreenComboBox() {
        ComboBox<Screen> comboBox = new ComboBox<>(Screen.getScreens());
        comboBox.setMaxWidth(400);
        comboBox.getSelectionModel().select(0);
        Notification.getDefaults().screenProperty().bind(comboBox.getSelectionModel().selectedItemProperty());
        return comboBox;
    }

    // Keine ComboBox wegen https://bugs.openjdk.java.net/browse/JDK-8134923 (Exception wenn null ausgewählt wird)
    private ChoiceBox<Window> createWindowComboBox() {
        ChoiceBox<Window> comboBox = new ChoiceBox<>();
        comboBox.getItems().addAll(null, primaryStage);
        comboBox.getSelectionModel().select(0);
        Notification.getDefaults().windowProperty().bind(comboBox.getSelectionModel().selectedItemProperty());
        return comboBox;
    }

    private ComboBox<Notification.Type> createTypeComboBox() {
        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().setAll(Notification.Type.values());
        typeComboBox.getSelectionModel().selectFirst();
        return typeComboBox;
    }

}
