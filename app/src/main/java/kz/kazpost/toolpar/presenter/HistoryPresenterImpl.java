package kz.kazpost.toolpar.presenter;

import javax.inject.Inject;

import kz.kazpost.toolpar.base.BasePresenter;
import kz.kazpost.toolpar.data.DataManager;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import kz.kazpost.toolpar.Interface.GitHubService;
import kz.kazpost.toolpar.Model.LastActions;
import kz.kazpost.toolpar.view.HistoryView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.AccessTokenConst;
import static kz.kazpost.toolpar.Const.BASE_URL;
import static kz.kazpost.toolpar.utils.Singleton.getUserClient;

/**
 * Created by root on 1/16/17.
 */

public class HistoryPresenterImpl<V extends HistoryView> extends BasePresenter<V> implements HistoryPresenter<V> {

    private Subscription subscription;

    @Inject
    HistoryPresenterImpl(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadHistory() {

        getMvpView().showProgress();


        Retrofit retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(AccessTokenConst))
                .build();

        GitHubService gitHubServ = retrofitRoutes.create(GitHubService.class);


        Observable<LastActions> callEdges =
                gitHubServ.getLastActions();


        subscription = callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                getMvpView().showHistoryData(response);
                                getMvpView().hideProgress();
                            } else {
                                getMvpView().showHistoryEmptyData();
                                getMvpView().hideProgress();
                            }
                        },
                        throwable -> {
                            getMvpView().showHistoryError(throwable);
                            getMvpView().hideProgress();
                        });

    }

    @Override
    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

    }

}
