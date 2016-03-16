package de.pheru.fx.controls.notificationbar;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Philipp on 16.03.2016.
 */
public class NotificationBar extends HBox implements Initializable {

    @FXML
    private HBox currentNotificationBox;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button closeButton;

    private final ObservableList<Node> notificationNodes = FXCollections.observableArrayList();
    private final IntegerProperty currentNotificationIndex = new SimpleIntegerProperty(0);

    public NotificationBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(NotificationBar.class.getResource("notificationbar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notificationNodes.add(new Label("1"));
        notificationNodes.add(new Label("2"));
        notificationNodes.add(new Label("3"));
        notificationNodes.add(new Label("4"));
        notificationNodes.add(new Label("5"));

        //#############################################################################

        notificationNodes.addListener((ListChangeListener<Node>) c -> {
            System.out.println("listlistener");
            if (notificationNodes.isEmpty()) {
                //TODO hide bar
            }
            while (c.next()) {
                if (c.wasRemoved()) {
                    // Kein -1, da der Eintrag bereits aus der Liste entfernt wurde
                    if (c.getFrom() == notificationNodes.size()) {
                        currentNotificationIndex.set(currentNotificationIndex.get() - 1);
                    } else {
                        currentNotificationBox.getChildren().clear();
                        currentNotificationBox.getChildren().add(notificationNodes.get(c.getFrom()));
                    }
                }
            }
        });
        currentNotificationIndex.addListener((observable, oldValue, newValue) -> {
            System.out.println("indexlistener");
            currentNotificationBox.getChildren().clear();
            currentNotificationBox.getChildren().add(notificationNodes.get(newValue.intValue()));
        });
        previousButton.disableProperty().bind(currentNotificationIndex.isEqualTo(0));
        nextButton.disableProperty().bind(currentNotificationIndex.isEqualTo(Bindings.size(notificationNodes).subtract(1)));
    }

    @FXML
    private void next() {
        System.out.println("N");
        currentNotificationIndex.set(currentNotificationIndex.get() + 1);
    }

    @FXML
    private void previous() {
        System.out.println("P");
        currentNotificationIndex.set(currentNotificationIndex.get() - 1);
    }

    @FXML
    private void close() {
        System.out.println("C");
        notificationNodes.remove(currentNotificationIndex.get());
    }

    public ObservableList<Node> getNotificationNodes() {
        return notificationNodes;
    }
}
