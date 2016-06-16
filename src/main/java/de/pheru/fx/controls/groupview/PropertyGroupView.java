package de.pheru.fx.controls.groupview;

import com.sun.javafx.property.PropertyReference;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

import java.util.List;

/**
 * Created by Philipp on 11.06.2016.
 */
class PropertyGroupView<I> extends Control {

    private final String propertyName;
    private PropertyReference<Object> propertyReference;
    private ObservableList<I> items = FXCollections.observableArrayList();
    private ObservableMap<Object, ObservableList<I>> propertyValueItemMap = FXCollections.observableHashMap();

    protected PropertyGroupView(String propertyName) {
        this.propertyName = propertyName;
        items.addListener(new ListChangeListener<I>() {
            @Override
            public void onChanged(Change<? extends I> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        handleAdded(c.getAddedSubList());
                    } else if (c.wasRemoved()) {
                        handleRemoved(c.getRemoved());
                    }
                }
            }
        });
    }

    private void handleAdded(List<? extends I> added) {
        for (I item : added) {
            Object value = getPropertyValue(item);
            if (!propertyValueItemMap.containsKey(value)) {
                propertyValueItemMap.put(value, FXCollections.observableArrayList());
            }
            propertyValueItemMap.get(value).add(item);
        }
    }

    private void handleRemoved(List<? extends I> removed) {
        for (I item : removed) {
            Object value = getPropertyValue(item);
            List<I> itemsForValue = propertyValueItemMap.get(value);
            itemsForValue.remove(item);
            if (itemsForValue.isEmpty()) {
                propertyValueItemMap.remove(value);
            }
        }
    }

    private Object getPropertyValue(I item) {
        if (propertyReference == null) {
            propertyReference = new PropertyReference<>(item.getClass(), propertyName);
        }
        return propertyReference.get(item);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public ObservableList<I> getItems() {
        return items;
    }

    protected ObservableMap<Object, ObservableList<I>> getPropertyValueItemMap() {
        return propertyValueItemMap;
    }
}
