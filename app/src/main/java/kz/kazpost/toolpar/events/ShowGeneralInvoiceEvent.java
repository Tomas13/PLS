package kz.kazpost.toolpar.events;

import kz.kazpost.toolpar.Model.geninvoice.InvoiceMain;

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
