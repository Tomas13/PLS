package kz.kazpost.toolpar.events;

import kz.kazpost.toolpar.Model.BodyForCreateInvoiceWithout;

/**
 * Created by root on 2/3/17.
 */

public class PostBodyEvent {
    private BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout;

    public PostBodyEvent(BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout) {
        this.bodyForCreateInvoiceWithout = bodyForCreateInvoiceWithout;
    }

    public BodyForCreateInvoiceWithout getBodyForCreateInvoiceWithout() {
        return bodyForCreateInvoiceWithout;
    }
}
