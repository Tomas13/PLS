package kz.kazpost.toolpar.view;

import kz.kazpost.toolpar.Model.routes.Routes;

/**
 * Created by root on 1/16/17.
 */

public interface RoutesView {

    void showProgress();

    void hideProgress();

    void showRoutesData(Routes routes);

    void showRoutesEmptyData();

    void showRoutesError(Throwable throwable);
}
