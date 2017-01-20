package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.IdsCollate;

/**
 * Created by root on 1/17/17.
 */

public interface AcceptGenInvoicePresenter {

    void onDestroy();

    void loadGetListForVpn();

    void loadDestinationList();

    void postCollate(IdsCollate idsCollate);

    void unSubscribe();

    void retrofitAcceptGeneralInvoice(Long generalInvoiceId);
}
