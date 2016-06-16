package de.pheru.fx.controls.groupview;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;

abstract class PropertyGroupViewSkin<I> implements Skin<PropertyGroupView<I>> {

    protected abstract void propertyValueRemoved(MapChangeListener.Change<?, ? extends ObservableList<I>> c);
    protected abstract void propertyValueAdded(MapChangeListener.Change<?, ? extends ObservableList<I>> c);

    public PropertyGroupViewSkin(PropertyGroupView<I> propertyGroupView) {
        propertyGroupView.getPropertyValueItemMap().addListener(new PropertyValueItemMapChangeListener());
    }

    private class PropertyValueItemMapChangeListener implements MapChangeListener<Object, ObservableList<I>> {

        @Override
        public void onChanged(Change<?, ? extends ObservableList<I>> c) {
            if (c.wasRemoved()) {
                propertyValueRemoved(c);
            }
            if (c.wasAdded()) {
                propertyValueAdded(c);
            }
        }
    }
}
