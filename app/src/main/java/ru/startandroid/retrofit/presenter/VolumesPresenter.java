package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.BodyForCreateInvoice;

/**
 * Created by root on 1/17/17.
 */

public interface VolumesPresenter {

    void onDestroy();

    void loadGetListForVpn();

    void postCreateInvoice(BodyForCreateInvoice bodyForCreateInvoice);

    void unSubscribe();
}
