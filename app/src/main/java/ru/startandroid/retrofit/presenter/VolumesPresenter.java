package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.BodyForCreateInvoice;
import ru.startandroid.retrofit.Model.BodyForCreateInvoiceWithout;

/**
 * Created by root on 1/17/17.
 */

public interface VolumesPresenter {

    void onDestroy();

    void loadGetListForVpn();

    void postCreateInvoice(BodyForCreateInvoice bodyForCreateInvoice);
    void postCreateInvoice(BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout);

    void unSubscribe();
}
