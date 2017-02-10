package ru.startandroid.retrofit.view;

import ru.startandroid.retrofit.Model.CreateResponse;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;

/**
 * Created by root on 1/17/17.
 */

public interface VolumesView {

    void showProgress();

    void hideProgress();

//    void showVolumesData(CollateResponse volumes);
    
    void showRoutesEmptyData();

    void showRoutesError(Throwable throwable);
}
