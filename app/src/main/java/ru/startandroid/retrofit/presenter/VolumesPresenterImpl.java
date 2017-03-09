package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.VolumesView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/17/17.
 */

public class VolumesPresenterImpl implements VolumesPresenter {

    private Subscription subscription;
    private VolumesView view;
    private NetworkService service;

    public VolumesPresenterImpl(VolumesView view, NetworkService service) {
        this.view = view;
        this.service = service;
    }

    @Override
    public void loadGetListForVpn() {
//        view.showProgress();

        Observable<CollateResponse> callEdges =
                service.getApiService().getListForVpn();

        subscription = callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
//                                view.showVolumesData(response);
//                                view.hideProgress();
                            } else {
                                view.showRoutesEmptyData();
//                                view.hideProgress();
                            }
                        },
                        throwable -> {
                            view.showRoutesError(throwable);
//                            view.hideProgress();
                        });

    }

    @Override
    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        view = null;
    }

}
