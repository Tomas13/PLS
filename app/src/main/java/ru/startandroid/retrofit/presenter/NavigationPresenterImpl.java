package ru.startandroid.retrofit.presenter;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.NavigationActView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

/**
 * Created by root on 1/19/17.
 */

public class NavigationPresenterImpl implements NavitationPresenter {
    private Subscription subscription;
    private NavigationActView view;

    public NavigationPresenterImpl(NavigationActView view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadMembershipInfo(String accessToken) {
        view.showProgress();

        Retrofit retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient("Bearer " + accessToken))
                .build();

        GitHubService gitHubServ = retrofitRoutes.create(GitHubService.class);


        Observable<Member> callEdges =
                gitHubServ.getMembershipInfo();

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
