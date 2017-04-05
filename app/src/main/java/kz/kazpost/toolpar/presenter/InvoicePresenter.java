package kz.kazpost.toolpar.presenter;

/**
 * Created by root on 1/17/17.
 */

public interface InvoicePresenter {

    void onDestroy();

    void loadGeneralInvoice(String accessToken);

    void retrofitAcceptGeneralInvoice(Long generalInvoiceId, String accessToken);

//    void postCreateInvoice(BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout);

    void unSubscribe();

    String handleStatus(String message);
}

