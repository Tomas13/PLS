package ru.startandroid.retrofit.view;

import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;

/**
 * Created by root on 1/17/17.
 */

public interface InvoiceView {

    void showProgress();

    void hideProgress();

    void showGeneralInvoice(InvoiceMain invoiceMain);

    void showRoutesEmptyData();

    void showRoutesError(Throwable throwable);
}
