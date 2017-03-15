package ru.startandroid.retrofit.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.login.BodyLogin;
import ru.startandroid.retrofit.Model.login.LoginResponse;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.LoginView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.Const.PASSWORD;
import static ru.startandroid.retrofit.Const.TOKEN_SHARED_PREF;
import static ru.startandroid.retrofit.Const.USERNAME;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

/**
 * Created by root on 3/7/17.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView loginView;

    public LoginPresenterImpl(LoginView view) {
        loginView = view;
    }


    @Override
    public void postLogin(String username, String password) {

        Retrofit retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient())
                .build();

        GitHubService gitHubServ = retrofitRoutes.create(GitHubService.class);


        Observable<LoginResponse> callCreate =
                gitHubServ.postLogin(
                        "no-cache",
                        "password",
                        "toolpar-mobile",
                        "offline_access",
                        username,
                        password);

        callCreate
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {

                            loginView.showLoginData(response);

                        },
                        throwable -> {

                            if (throwable.getMessage().equals("HTTP 401 Unauthorized")){

                                loginView.showLoginError(throwable);
                            }

                        });

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void onDestroy() {

    }

}

