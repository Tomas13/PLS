package ru.startandroid.retrofit.view;

import ru.startandroid.retrofit.Model.login.LoginResponse;

/**
 * Created by root on 3/7/17.
 */

public interface LoginView {

    void showProgress();

    void hideProgress();

    void showLoginData(LoginResponse loginResponse);

    void showLoginEmptyData();

    void showLoginError(Throwable throwable);
}
