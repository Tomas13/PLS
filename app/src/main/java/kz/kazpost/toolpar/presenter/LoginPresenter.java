package kz.kazpost.toolpar.presenter;

import kz.kazpost.toolpar.base.MvpPresenter;
import kz.kazpost.toolpar.view.LoginView;

/**
 * Created by root on 3/7/17.
 */

public interface LoginPresenter<V extends LoginView> extends MvpPresenter<V> {

//    void onDestroy();

    String getAccessToken();
    String getRefreshToken();

    void postLogin(String username, String password);

    void unSubscribe();

    void saveRefreshToken(String refreshToken);

    void saveAccessToken(String accessToken);

    void saveUsername(String mLogin);

    void savePassword(String mPassword);

    boolean hasRefreshToken();
}
