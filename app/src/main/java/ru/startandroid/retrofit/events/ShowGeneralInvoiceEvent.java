package ru.startandroid.retrofit.events;

import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;

/**
 * Created by root on 2/9/17.
 */

public class ShowGeneralInvoiceEvent {
    private InvoiceMain invoiceMain;

    public ShowGeneralInvoiceEvent(InvoiceMain invoiceMain) {
        this.invoiceMain = invoiceMain;
    }

    public InvoiceMain getInvoiceMain() {
        return invoiceMain;
    }
}
