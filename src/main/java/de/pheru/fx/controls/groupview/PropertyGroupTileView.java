package de.pheru.fx.controls.groupview;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.util.Callback;

/**
 * Created by Philipp on 14.06.2016.
 */
public class PropertyGroupTileView<I> extends PropertyGroupView<I> {

    private Callback<PropertyGroup<I>, Node> tileFactory;

    //FlowPane-Properties
    private final DoubleProperty hgap = new SimpleDoubleProperty(5);
    private final DoubleProperty vgap = new SimpleDoubleProperty(5);
    private final ObjectProperty<VPos> rowValignment = new SimpleObjectProperty<>(VPos.TOP);
    private final ObjectProperty<HPos> columnHalignment = new SimpleObjectProperty<>(HPos.LEFT);
    private final ObjectProperty<Pos> alignment = new SimpleObjectProperty<>(Pos.TOP_LEFT);
    private final ObjectProperty<Orientation> orientation = new SimpleObjectProperty<>(Orientation.HORIZONTAL);

    public PropertyGroupTileView(String propertyName) {
        super(propertyName);
        setSkin(new PropertyGroupTileViewSkin<>(this));
    }

    public double getHgap() {
        return hgap.get();
    }

    public DoubleProperty hgapProperty() {
        return hgap;
    }

    public void setHgap(double hgap) {
        this.hgap.set(hgap);
    }

    public double getVgap() {
        return vgap.get();
    }

    public DoubleProperty vgapProperty() {
        return vgap;
    }

    public void setVgap(double vgap) {
        this.vgap.set(vgap);
    }

    public VPos getRowValignment() {
        return rowValignment.get();
    }

    public ObjectProperty<VPos> rowValignmentProperty() {
        return rowValignment;
    }

    public void setRowValignment(VPos rowValignment) {
        this.rowValignment.set(rowValignment);
    }

    public HPos getColumnHalignment() {
        return columnHalignment.get();
    }

    public ObjectProperty<HPos> columnHalignmentProperty() {
        return columnHalignment;
    }

    public void setColumnHalignment(HPos columnHalignment) {
        this.columnHalignment.set(columnHalignment);
    }

    public Pos getAlignment() {
        return alignment.get();
    }

    public ObjectProperty<Pos> alignmentProperty() {
        return alignment;
    }

    public void setAlignment(Pos alignment) {
        this.alignment.set(alignment);
    }

    public Orientation getOrientation() {
        return orientation.get();
    }

    public ObjectProperty<Orientation> orientationProperty() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }

    public Callback<PropertyGroup<I>, Node> getTileFactory() {
        return tileFactory;
    }

    public void setTileFactory(Callback<PropertyGroup<I>, Node> tileFactory) {
        this.tileFactory = tileFactory;
    }
}
