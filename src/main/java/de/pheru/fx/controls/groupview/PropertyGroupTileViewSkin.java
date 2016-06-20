package de.pheru.fx.controls.groupview;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class PropertyGroupTileViewSkin<I> extends PropertyGroupViewSkin<I> {

    private ScrollPane scrollPane;
    private FlowPane flowPane;
    private PropertyGroupTileView<I> propertyGroupTileView;
    private ObservableMap<Object, Node> propertyValueNodeMap = FXCollections.observableHashMap();

    public PropertyGroupTileViewSkin(PropertyGroupTileView<I> propertyGroupTileView) {
        super(propertyGroupTileView);
        this.propertyGroupTileView = propertyGroupTileView;

        initFlowPane();
        initScrollPane();
        propertyValueNodeMap.addListener(new PropertyValueNodeMapChangeListener());
    }

    private void initFlowPane() {
        flowPane = new FlowPane();
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

    private void initScrollPane() {
        scrollPane = new ScrollPane(flowPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.vbarPolicyProperty().bind(propertyGroupTileView.vBarPolicyProperty());
        scrollPane.hbarPolicyProperty().bind(propertyGroupTileView.hBarPolicyProperty());
        scrollPane.vvalueProperty().bind(propertyGroupTileView.vValueProperty());
        scrollPane.hvalueProperty().bind(propertyGroupTileView.hValueProperty());
        scrollPane.pannableProperty().bind(propertyGroupTileView.pannableProperty());
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
            HBox defaultNode = new HBox();
            Label label = new Label();
            label.textProperty().bind(Bindings.size(c.getValueAdded()).asString());
            defaultNode.getChildren().addAll(new Label(c.getKey() + ": "), label);
            propertyValueNodeMap.put(c.getKey(), defaultNode);
        }
    }

    @Override
    public PropertyGroupView<I> getSkinnable() {
        return propertyGroupTileView;
    }

    @Override
    public Node getNode() {
        return scrollPane;
    }

    @Override
    public void dispose() {
        scrollPane = null;
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
