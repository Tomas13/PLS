package ru.startandroid.retrofit.presenter;

/**
 * Created by root on 1/16/17.
 */

public interface RoutesPresenter {

    void onDestroy();

    void loadRoutes();

    void unSubscribe();

}
