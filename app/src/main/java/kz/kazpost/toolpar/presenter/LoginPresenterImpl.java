package kz.kazpost.toolpar.presenter;

import javax.inject.Inject;

import kz.kazpost.toolpar.base.BasePresenter;
import kz.kazpost.toolpar.data.DataManager;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import kz.kazpost.toolpar.Interface.GitHubService;
import kz.kazpost.toolpar.Model.login.LoginResponse;
import kz.kazpost.toolpar.view.LoginView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.BASE_URL;
import static kz.kazpost.toolpar.utils.Singleton.getUserClient;

/**
 * Created by root on 3/7/17.
 */

public class LoginPresenterImpl<V extends LoginView> extends BasePresenter<V> implements LoginPresenter<V> {

    @Inject
    public LoginPresenterImpl(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public String getAccessToken() {
        return getDataManager().getAccessToken();
    }

    @Override
    public String getRefreshToken() {
        return getDataManager().getRefreshToken();
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
                        response -> getMvpView().showLoginData(response),
                        throwable -> {
                            if (throwable.getMessage().equals("HTTP 401 Unauthorized")) {
                                getMvpView().showLoginError(throwable);
                            }
                        });

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void saveUsername(String mLogin) {
        getDataManager().saveUsername(mLogin);
    }

    @Override
    public void savePassword(String mPassword) {
        getDataManager().savePassword(mPassword);
    }

    @Override
    public boolean hasRefreshToken() {
        return getDataManager().hasRefreshToken();
    }

    @Override
    public void saveRefreshToken(String refreshToken) {
        getDataManager().saveRefreshToken(refreshToken);
    }

    @Override
    public void saveAccessToken(String accessToken) {
        getDataManager().saveAccessToken(accessToken);
    }


}

