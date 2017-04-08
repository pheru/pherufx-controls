package de.pheru.fx.controls.notification;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NotificationDefaults extends NotificationProperties {

    private ObservableList<String> stylesheets = FXCollections.observableArrayList();

    public ObservableList<String> getStylesheets() {
        return stylesheets;
    }

    public void setStylesheets(ObservableList<String> stylesheets) {
        this.stylesheets = stylesheets;
    }
}
