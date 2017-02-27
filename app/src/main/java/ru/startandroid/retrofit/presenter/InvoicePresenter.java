package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.BodyForCreateInvoiceWithout;

/**
 * Created by root on 1/17/17.
 */

public interface InvoicePresenter {

    void onDestroy();

    void loadGeneralInvoice();

    void retrofitAcceptGeneralInvoice(Long generalInvoiceId);

//    void postCreateInvoice(BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout);

    void unSubscribe();

    String handleStatus(String message);
}

