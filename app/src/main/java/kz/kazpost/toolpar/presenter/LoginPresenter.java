package kz.kazpost.toolpar.presenter;

/**
 * Created by root on 3/7/17.
 */

public interface LoginPresenter {

    void onDestroy();

    void postLogin(String username, String password);

    void unSubscribe();
}
