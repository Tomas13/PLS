package kz.kazpost.toolpar.view;

import kz.kazpost.toolpar.Model.login.LoginResponse;
import kz.kazpost.toolpar.base.MvpView;

/**
 * Created by root on 3/7/17.
 */

public interface LoginView extends MvpView {

    void showLoginData(LoginResponse loginResponse);

    void showLoginEmptyData();

    void showLoginError(Throwable throwable);
}
