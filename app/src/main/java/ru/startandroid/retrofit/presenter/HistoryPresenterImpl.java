package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.HistoryView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/16/17.
 */

public class HistoryPresenterImpl implements HistoryPresenter {

    Subscription subscription;
    private HistoryView view;
    private NetworkService service;

    public HistoryPresenterImpl(HistoryView view, NetworkService service) {
        this.view = view;
        this.service = service;
    }

    @Override
    public void onDestroy() {
        view = null;
    }


    @Override
    public void loadHistory() {

        view.showProgress();

        Observable<LastActions> callEdges =
                service.getApiService().getLastActions();


        subscription = callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.showHistoryData(response);
                                view.hideProgress();

                            } else {
                                view.showHistoryEmptyData();
                                view.hideProgress();

                            }
                        },
                        throwable -> {
//                            callEdges.retryWhen(observable ->)

                            view.showHistoryError(throwable);
                            view.hideProgress();
                        });

    }

    @Override
    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

    }

}
