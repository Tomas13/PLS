package ru.startandroid.retrofit.presenter;

/**
 * Created by root on 1/17/17.
 */

public interface VolumesPresenter {

    void onDestroy();

    void loadGetListForVpn();

    void unSubscribe();
}
