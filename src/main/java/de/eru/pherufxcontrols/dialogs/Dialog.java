package de.eru.pherufxcontrols.dialogs;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author Philipp Bruckner
 */
public abstract class Dialog implements Initializable {

    private final StringProperty title = new SimpleStringProperty();
    private Parent root;
    /**
     * 1: Ja,OK / 0: Nein / -1: Abbrechen
     */
    private int response = -1;

    @FXML
    protected Label textLabel;
    @FXML
    protected Label headerLabel;
    @FXML
    protected ImageView imageView;

    public int showAndWait() {
        Stage stage = initStage();
        stage.showAndWait();
        return response;
    }

    protected Stage initStage() {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.getIcons().add(imageView.getImage());
        stage.titleProperty().bind(title);
        return stage;
    }

    protected void closeDialog(int response) {
        this.response = response;
        getRoot().getScene().getWindow().hide();
    }

    public Dialog bindBidirectionalTextProperty(Property<String> property) {
        textLabel.textProperty().bindBidirectional(property);
        return this;
    }

    public Dialog bindBidirectionalHeaderProperty(Property<String> property) {
        headerLabel.textProperty().bindBidirectional(property);
        return this;
    }

    public Dialog bindTitleProperty(ObservableValue<? extends String> observable) {
        title.bind(observable);
        return this;
    }

    public String getText() {
        return textLabel.getText();
    }

    public Dialog setText(final String text) {
        this.textLabel.setText(text);
        return this;
    }

    public String getHeader() {
        return headerLabel.getText();
    }

    public Dialog setHeader(final String header) {
        this.headerLabel.setText(header);
        return this;
    }

    public String getTitle() {
        return title.get();
    }

    public Dialog setTitle(final String title) {
        this.title.set(title);
        return this;
    }

    public StringProperty titleProperty() {
        return title;
    }

    protected Parent getRoot() {
        return root;
    }

    protected void setRoot(Parent root) {
        this.root = root;
    }

}
