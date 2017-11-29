package de.pheru.fx.controls.demo.wizard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestModel {

    private final StringProperty eins = new SimpleStringProperty();
    private final StringProperty zwei = new SimpleStringProperty();
    private final StringProperty drei = new SimpleStringProperty();
    private final StringProperty vier = new SimpleStringProperty();

    public String getEins() {
        return eins.get();
    }

    public StringProperty einsProperty() {
        return eins;
    }

    public void setEins(final String eins) {
        this.eins.set(eins);
    }

    public String getZwei() {
        return zwei.get();
    }

    public StringProperty zweiProperty() {
        return zwei;
    }

    public void setZwei(final String zwei) {
        this.zwei.set(zwei);
    }

    public String getDrei() {
        return drei.get();
    }

    public StringProperty dreiProperty() {
        return drei;
    }

    public void setDrei(final String drei) {
        this.drei.set(drei);
    }

    public String getVier() {
        return vier.get();
    }

    public StringProperty vierProperty() {
        return vier;
    }

    public void setVier(final String vier) {
        this.vier.set(vier);
    }
}
