package kz.kazpost.toolpar.events;

import kz.kazpost.toolpar.Model.acceptgen.Example;

/**
 * Created by root on 2/9/17.
 */

public class AcceptGenInvoiceEvent {

    private Example example;

    public AcceptGenInvoiceEvent(Example example) {
        this.example = example;
    }

    public Example getExample() {
        return example;
    }
}
