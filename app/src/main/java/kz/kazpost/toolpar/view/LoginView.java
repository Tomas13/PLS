package kz.kazpost.toolpar.view;

import kz.kazpost.toolpar.Model.login.LoginResponse;

/**
 * Created by root on 3/7/17.
 */

public interface LoginView {

    void showLoginData(LoginResponse loginResponse);

    void showLoginEmptyData();

    void showLoginError(Throwable throwable);
}
