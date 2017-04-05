package kz.kazpost.toolpar.utils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import kz.kazpost.toolpar.Interface.GitHubService;
import kz.kazpost.toolpar.Model.login.LoginResponse;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.BASE_URL;
import static kz.kazpost.toolpar.utils.Singleton.getUserClient;

/**
 * Created by root on 3/9/17.
 */

public class AccessToken {

    String mAccessToken;
    String username;
    String password;

    public AccessToken(String username1, String password1) {
        username = username1;
        password = password1;
    }


    public String getmAccessToken() {
        return mAccessToken;
    }

    public void postLogin(){
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
                        "offline-access",
                        username,
                        password);

        callCreate
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> mAccessToken = response.getAccessToken(),
                        throwable -> {

                            if (throwable.getMessage().equals("HTTP 401 Unauthorized")){
                                mAccessToken = "Неверный пароль или логин";
                            }

                        });

    }


}
