package de.eru.pherufxcontrols.dialogs;

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
public abstract class Dialog implements Initializable{

    private final StringProperty title = new SimpleStringProperty();
    private Parent root;
    /**
     * 1:  Ja / OK
     * 0:  Nein
     * -1: Abbrechen
     */
    protected int response = -1;

    @FXML
    protected Label textLabel;
    @FXML
    protected Label headerLabel;
    @FXML
    protected ImageView image;
    
    public int showAndWait(){
        Stage stage = initStage();
        stage.showAndWait();
        return response;
    }
    
    protected Stage initStage(){
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(image.getImage());
        stage.setTitle(title.get());
        return stage;
    }
    
    protected void closeDialog(int response){
        this.response = response;
        getRoot().getScene().getWindow().hide();
    }
    
    public Dialog bindTextProperty(ObservableValue<? extends String> observable) {
        textLabel.textProperty().bind(observable);
        return this;
    }

    public Dialog bindHeaderProperty(ObservableValue<? extends String> observable) {
        headerLabel.textProperty().bind(observable);
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
