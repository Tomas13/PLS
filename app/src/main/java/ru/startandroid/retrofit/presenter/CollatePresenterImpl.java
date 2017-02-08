package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.IdsCollate;
import ru.startandroid.retrofit.Model.acceptgen.Example;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.destinationlist.ResponseDestinationList;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.CollateView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/17/17.
 */

public class CollatePresenterImpl implements CollatePresenter {

    private Subscription subscription;
    private CollateView view;
    private NetworkService service;

    public CollatePresenterImpl(CollateView view, NetworkService service) {
        this.view = view;
        this.service = service;
    }

    @Override
    public void loadGetListForVpn() {
        view.showProgress();

        Observable<CollateResponse> callEdges =
                service.getApiService().getListForVpn();

        subscription = callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.showVolumesData(response);
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
    public void loadDestinationList() {
        view.showProgress();

        Observable<ResponseDestinationList> callDestinationList =
                service.getApiService().getDestionationLists();

        subscription = callDestinationList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.hideProgress();
                                view.showDestinationList(response);
                            } else {
                                view.hideProgress();
                                view.showRoutesEmptyData();
                            }
                        },
                        throwable -> {
                            view.showRoutesError(throwable);
                            view.hideProgress();
                        });

    }

    @Override
    public void postCollate(IdsCollate idsCollate) {
        view.showProgress();

        Observable<CollateResponse> postCollateDestinationLists =
                service.getApiService().postCollateDestinationLists(idsCollate);

        subscription = postCollateDestinationLists
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.hideProgress();
                                view.showCollateResponse(response);
                            } else {

                                loadGetListForVpn();

//                                view.hideProgress();
//                                view.showRoutesEmptyData();
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



    @Override
    public void onDestroy() {
        view = null;
    }

}
