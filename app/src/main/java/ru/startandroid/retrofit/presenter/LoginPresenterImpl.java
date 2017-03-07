package ru.startandroid.retrofit.presenter;

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
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

/**
 * Created by root on 3/7/17.
 */

public class LoginPresenterImpl implements LoginPresenter {

    BodyLogin bodyLogin;
    LoginView loginView;


    public LoginPresenterImpl(LoginView view) {
        loginView = view;
    }


    @Override
    public void postLogin() {

        Retrofit retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient())
                .build();

        GitHubService gitHubServ = retrofitRoutes.create(GitHubService.class);


        Observable<LoginResponse> callCreate =
                gitHubServ.postLogin(bodyLogin);

        callCreate
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {

                            loginView.showLoginData(response);

                        },
                        throwable -> {

                            loginView.showLoginError(throwable);

                        });

    }

    @Override
    public void setBody(BodyLogin body) {
        bodyLogin = body;
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void onDestroy() {

    }

}

