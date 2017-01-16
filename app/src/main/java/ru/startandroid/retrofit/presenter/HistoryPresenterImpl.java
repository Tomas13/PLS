package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.models.HistoryService;
import ru.startandroid.retrofit.view.HistoryView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/16/17.
 */

public class HistoryPresenterImpl implements HistoryPresenter {

    private HistoryView view;
    private HistoryService service;

    public HistoryPresenterImpl(HistoryView view, HistoryService service) {
        this.view = view;
        this.service = service;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onResume() {
        this.view = view;
    }

    @Override
    public void loadHistory() {

        view.showProgress();

        Observable<LastActions> callEdges =
                service.getApiService().getLastActions();


        callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.showHistoryData(response);
                            } else {
                                view.showHistoryEmptyData();
                            }
                        },
                        throwable -> {
                            view.showHistoryError(throwable);
                            view.hideProgress();
                        });
    }

}
