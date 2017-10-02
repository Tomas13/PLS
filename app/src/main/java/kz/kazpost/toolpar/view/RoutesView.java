package kz.kazpost.toolpar.view;

import kz.kazpost.toolpar.Model.routes.Routes;
import kz.kazpost.toolpar.base.MvpView;

/**
 * Created by root on 1/16/17.
 */

public interface RoutesView extends MvpView {

    void showProgress();

    void hideProgress();

    void showRoutesData(Routes routes);

    void showRoutesEmptyData();

    void showRoutesError(Throwable throwable);
}
