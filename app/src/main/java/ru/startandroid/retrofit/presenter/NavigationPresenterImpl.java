package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.NavigationActView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/19/17.
 */

public class NavigationPresenterImpl implements NavitationPresenter {
    private Subscription subscription;
    private NavigationActView view;
    private NetworkService service;


    public NavigationPresenterImpl(NavigationActView view, NetworkService networkService) {
        this.view = view;
        this.service = networkService;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadMembershipInfo() {
        view.showProgress();

        Observable<Member> callEdges =
                service.getApiService().getMembershipInfo();

        subscription = callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("ok")) {
                                view.getMembershipData(response);
                                view.hideProgress();
                            } else {
                                view.showMemberEmptyData();
                                view.hideProgress();
                            }
                        },
                        throwable -> {
                            view.showMemberError(throwable);
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
