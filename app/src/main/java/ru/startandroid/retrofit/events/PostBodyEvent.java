package ru.startandroid.retrofit.events;

import ru.startandroid.retrofit.Model.BodyForCreateInvoiceWithout;

/**
 * Created by root on 2/3/17.
 */

public class PostBodyEvent {
    BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout;

    public PostBodyEvent(BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout) {
        this.bodyForCreateInvoiceWithout = bodyForCreateInvoiceWithout;
    }

    public BodyForCreateInvoiceWithout getBodyForCreateInvoiceWithout() {
        return bodyForCreateInvoiceWithout;
    }
}
