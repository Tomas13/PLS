package ru.startandroid.retrofit.presenter;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.RoutesView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

/**
 * Created by root on 1/16/17.
 */

public class RoutesPresenterImpl implements RoutesPresenter {

    private Subscription subscription;
    private RoutesView view;

    public RoutesPresenterImpl(RoutesView view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadRoutes(String accessToken) {

        view.showProgress();


        Retrofit retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient("Bearer " + accessToken))
                .build();

        GitHubService gitHubServ = retrofitRoutes.create(GitHubService.class);


        Observable<Routes> callEdges =
                gitHubServ.getRoutesInfo();

        subscription = callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.showRoutesData(response);
                                view.hideProgress();
                            } else {
                                view.showRoutesEmptyData();
                                view.hideProgress();
                            }
                        },
                        throwable -> {

                            Log.d("routesPresent", throwable.toString());
                            view.showRoutesError(throwable);
                            view.hideProgress();
                        }

                        );

    }

    @Override
    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
