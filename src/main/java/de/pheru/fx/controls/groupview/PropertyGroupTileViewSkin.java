package de.pheru.fx.controls.groupview;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PropertyGroupTileViewSkin<I> extends PropertyGroupViewSkin<I> {

    private FlowPane flowPane;
    private PropertyGroupTileView<I> propertyGroupTileView;
    private ObservableMap<Object, Node> propertyValueNodeMap = FXCollections.observableHashMap();

    public PropertyGroupTileViewSkin(PropertyGroupTileView<I> propertyGroupTileView) {
        super(propertyGroupTileView);
        this.propertyGroupTileView = propertyGroupTileView;

        initFlowPane();
        propertyValueNodeMap.addListener(new PropertyValueNodeMapChangeListener());
    }

    private void initFlowPane() {
        flowPane = new FlowPane(); //TODO Scrollbar?
        //FlowPane-Properties
        flowPane.hgapProperty().bind(propertyGroupTileView.hgapProperty());
        flowPane.vgapProperty().bind(propertyGroupTileView.vgapProperty());
        flowPane.rowValignmentProperty().bind(propertyGroupTileView.rowValignmentProperty());
        flowPane.columnHalignmentProperty().bind(propertyGroupTileView.columnHalignmentProperty());
        flowPane.alignmentProperty().bind(propertyGroupTileView.alignmentProperty());
        flowPane.orientationProperty().bind(propertyGroupTileView.orientationProperty());

        //Control-Properties
        flowPane.minHeightProperty().bind(propertyGroupTileView.minHeightProperty());
        flowPane.prefHeightProperty().bind(propertyGroupTileView.prefHeightProperty());
        flowPane.maxHeightProperty().bind(propertyGroupTileView.maxHeightProperty());
        flowPane.minWidthProperty().bind(propertyGroupTileView.minWidthProperty());
        flowPane.prefWidthProperty().bind(propertyGroupTileView.prefWidthProperty());
        flowPane.maxWidthProperty().bind(propertyGroupTileView.maxWidthProperty());
    }

    @Override
    protected void propertyValueRemoved(MapChangeListener.Change<?, ? extends ObservableList<I>> c) {
        propertyValueNodeMap.remove(c.getKey());
    }

    @Override
    protected void propertyValueAdded(MapChangeListener.Change<?, ? extends ObservableList<I>> c) {
        PropertyGroup<I> propertyGroup = new PropertyGroup<>(c.getKey(), c.getMap().get(c.getKey()));
        if (propertyGroupTileView.getTileFactory() != null) {
            propertyValueNodeMap.put(c.getKey(), propertyGroupTileView.getTileFactory().call(propertyGroup));
        } else {
            VBox defaultNode = new VBox();
            defaultNode.getChildren().addAll(new Label(c.getKey().toString()),
                    new Label(String.valueOf(c.getValueAdded().size()))); //TODO valueAdded empty
            propertyValueNodeMap.put(c.getKey(), defaultNode);
        }
    }

    @Override
    public PropertyGroupView<I> getSkinnable() {
        return propertyGroupTileView;
    }

    @Override
    public Node getNode() {
        return flowPane;
    }

    @Override
    public void dispose() {
        flowPane = null;
        propertyGroupTileView = null;
        propertyValueNodeMap = null;
    }

    private class PropertyValueNodeMapChangeListener implements MapChangeListener<Object, Node> {
        @Override
        public void onChanged(Change<?, ? extends Node> c) {
            if (c.wasAdded()) {
                flowPane.getChildren().add(c.getValueAdded());
            }
            if (c.wasRemoved()) {
                flowPane.getChildren().remove(c.getValueRemoved());
            }
        }
    }
}
