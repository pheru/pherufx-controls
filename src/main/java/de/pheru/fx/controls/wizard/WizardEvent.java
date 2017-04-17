package de.pheru.fx.controls.wizard;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by Philipp on 18.10.2016.
 */
public class WizardEvent<T> extends Event {

    public static final EventType<WizardEvent> EVENT_TYPE = new EventType<>("WIZARD");

    private final T model;

    public WizardEvent(final T model) {
        super(EVENT_TYPE);
        this.model = model;
    }

    public T getModel() {
        return model;
    }
}
