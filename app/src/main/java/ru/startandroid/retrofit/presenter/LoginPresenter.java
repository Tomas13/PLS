package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.login.BodyLogin;

/**
 * Created by root on 3/7/17.
 */

public interface LoginPresenter {

    void onDestroy();

    void postLogin(String username, String password);

    void unSubscribe();
}
