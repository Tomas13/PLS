package kz.kazpost.toolpar.view;

import kz.kazpost.toolpar.base.MvpView;

/**
 * Created by root on 1/17/17.
 */

public interface VolumesView extends MvpView {

//    void showVolumesData(CollateResponse volumes);
    
    void showRoutesEmptyData();

    void showRoutesError(Throwable throwable);
}
