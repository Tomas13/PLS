package kz.kazpost.toolpar.presenter;

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

public class HistoryPresenterImpl implements HistoryPresenter {

    Subscription subscription;
    private HistoryView view;

    public HistoryPresenterImpl(HistoryView view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        view = null;
    }


    @Override
    public void loadHistory() {

        view.showProgress();


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
                                view.showHistoryData(response);
                                view.hideProgress();

                            } else {
                                view.showHistoryEmptyData();
                                view.hideProgress();

                            }
                        },
                        throwable -> {

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
