package kz.kazpost.toolpar.view;

/**
 * Created by root on 1/17/17.
 */

public interface CollateView {

    void showProgress();

    void hideProgress();

//    void showVolumesData(CollateResponse volumes);

//    void showCollateResponse(CollateResponse collateResponse);

//    void showDestinationList(ResponseDestinationList destinationList);

    void showRoutesEmptyData();

    void showRoutesError(Throwable throwable);
}
