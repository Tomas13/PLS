package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.acceptgen.Destinations;
import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.view.InvoiceView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/17/17.
 */

public class InvoicePresenterImpl implements InvoicePresenter {

    private Subscription subscription;
    private InvoiceView view;

    public InvoicePresenterImpl(InvoiceView view, NetworkService service) {
        this.view = view;
    }

    @Override
    public void loadGeneralInvoice() {

        view.showProgress();

        Observable<InvoiceMain> callEdges =
                NetworkService.getApiService().getGeneralInvoice();

        subscription = callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.showGeneralInvoice(response);
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
    public void retrofitAcceptGeneralInvoice(Long generalInvoiceId) {
        view.showProgress();

        Observable<Destinations> acceptGeneralInvoice =
                NetworkService.getApiService().acceptGeneralInvoiceNew(generalInvoiceId);

        subscription = acceptGeneralInvoice.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.hideProgress();
                                view.showGeneralInvoiceId(response);
                            } else {

                                view.showRoutesEmptyData();

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
