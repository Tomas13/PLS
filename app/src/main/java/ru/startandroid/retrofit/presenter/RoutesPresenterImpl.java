package ru.startandroid.retrofit.presenter;

import android.support.v4.util.LruCache;

import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.RoutesView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/16/17.
 */

public class RoutesPresenterImpl implements RoutesPresenter {

    private Subscription subscription;
    private RoutesView view;
    private NetworkService service;

    public RoutesPresenterImpl(RoutesView view, NetworkService service) {
        this.view = view;
        this.service = service;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadRoutes() {

        view.showProgress();

        Observable<Routes> callEdges =
                service.getApiService().getRoutesInfo();

        subscription = callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
//                                view.showRoutesData(response);
                                view.hideProgress();
                            } else {
                                view.showRoutesEmptyData();
                                view.hideProgress();
                            }
                        },
                        throwable -> {
                            view.showRoutesError(throwable);
                            view.hideProgress();
                        });

    }

    @Override
    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
