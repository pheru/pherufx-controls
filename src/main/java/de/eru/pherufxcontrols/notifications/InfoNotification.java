package de.eru.pherufxcontrols.notifications;

import de.eru.pherufxcontrols.utils.InfoType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 *
 * @author Philipp Bruckner
 */
public class InfoNotification extends Notification {

    private InfoType type;
    @FXML
    private Label headerLabel;
    @FXML
    private Label textLabel;
    @FXML
    private ImageView image;
    @FXML
    private CheckBox dontShowAgainBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setType(InfoType.INFO);
    }

    public InfoType getType() {
        return type;
    }

    public InfoNotification setType(InfoType type) {
        this.type = type;
        return this;
    }

    public String getHeader() {
        return headerLabel.getText();
    }

    public InfoNotification setHeader(String header) {
        headerLabel.setText(header);
        return this;
    }

    public String getText() {
        return textLabel.getText();
    }

    public InfoNotification setText(String text) {
        textLabel.setText(text);
        return this;
    }

    public InfoNotification bindHeaderProperty(ObservableValue<? extends String> observable) {
        headerLabel.textProperty().bind(observable);
        return this;
    }

    public InfoNotification bindTextProperty(ObservableValue<? extends String> observable) {
        textLabel.textProperty().bind(observable);
        return this;
    }

}
