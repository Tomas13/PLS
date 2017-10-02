package kz.kazpost.toolpar.view;

import kz.kazpost.toolpar.Model.acceptgen.Example;
import kz.kazpost.toolpar.Model.geninvoice.InvoiceMain;
import kz.kazpost.toolpar.base.MvpView;

/**
 * Created by root on 1/17/17.
 */

public interface InvoiceView extends MvpView {

    void showProgress();

    void hideProgress();

    void showGeneralInvoice(InvoiceMain invoiceMain);

    void showGeneralInvoiceId(Example destinations);

    void showRoutesEmptyData();

    void showRoutesError(Throwable throwable);

    void showEmptyToast(String message);

//    void getPostResponse(CreateResponse createResponse);

}
