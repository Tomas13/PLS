package ru.startandroid.retrofit.view;

import ru.startandroid.retrofit.Model.IdsCollate;
import ru.startandroid.retrofit.Model.acceptgen.Oinvoice;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.destinationlist.ResponseDestinationList;

/**
 * Created by root on 1/17/17.
 */

public interface AcceptGenInvoiceView {

    void showProgress();

    void hideProgress();

    void showVolumesData(CollateResponse volumes);

    void showGeneralInvoiceId(Oinvoice oinvoice);

    void showCollateResponse(CollateResponse collateResponse);

    void showDestinationList(ResponseDestinationList destinationList);

    void showRoutesEmptyData();

    void showRoutesError(Throwable throwable);
}
