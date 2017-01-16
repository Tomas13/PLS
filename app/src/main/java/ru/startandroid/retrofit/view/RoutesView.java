package ru.startandroid.retrofit.view;

import ru.startandroid.retrofit.Model.routes.Routes;

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
