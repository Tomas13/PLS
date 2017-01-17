package ru.startandroid.retrofit.presenter;

/**
 * Created by root on 1/17/17.
 */

public interface InvoicePresenter {

    void onDestroy();

    void loadGeneralInvoice();

    void unSubscribe();
}

