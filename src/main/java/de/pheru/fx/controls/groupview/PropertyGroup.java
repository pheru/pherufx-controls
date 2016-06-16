package de.pheru.fx.controls.groupview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PropertyGroup<I> {

    private final Object propertyValue;
    private final ObservableList<I> items;

    PropertyGroup(Object propertyValue, ObservableList<I> items) {
        this.propertyValue = propertyValue;
        this.items = items;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public ObservableList<I> getItemsUnmodifiable() {
        return FXCollections.unmodifiableObservableList(items);
    }
}
