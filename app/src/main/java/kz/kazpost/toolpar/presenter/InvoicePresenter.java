package kz.kazpost.toolpar.presenter;

import kz.kazpost.toolpar.base.MvpPresenter;
import kz.kazpost.toolpar.view.InvoiceView;

/**
 * Created by root on 1/17/17.
 */

public interface InvoicePresenter<V extends InvoiceView> extends MvpPresenter<V> {


    void loadGeneralInvoice(String accessToken);

    void retrofitAcceptGeneralInvoice(Long generalInvoiceId, String accessToken);

//    void postCreateInvoice(BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout);

    void unSubscribe();

    String handleStatus(String message);
}

