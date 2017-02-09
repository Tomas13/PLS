package ru.startandroid.retrofit.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import ru.startandroid.retrofit.Model.acceptgen.Example;
import ru.startandroid.retrofit.events.AcceptEmptyEvent;
import ru.startandroid.retrofit.events.AcceptErrorEvent;
import ru.startandroid.retrofit.events.AcceptGenInvoiceEvent;
import ru.startandroid.retrofit.models.NetworkService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.ACCEPT_INVOICE_PRIORITY;


/**
 * Created by root on 2/9/17.
 */

public class AcceptGeneralInvoiceJob extends Job {

    private Long generalInvId;

    public AcceptGeneralInvoiceJob(Long generalInvoiceId) {
        super(new Params(ACCEPT_INVOICE_PRIORITY).requireNetwork().persist());
        this.generalInvId = generalInvoiceId;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Observable<Example> acceptGeneralInvoice =
                NetworkService.getApiService().acceptGeneralInvoiceNew(generalInvId);

        acceptGeneralInvoice.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {

                                EventBus.getDefault().post(new AcceptGenInvoiceEvent(response));

                            } else if (response.getStatus().equals("list-empty")){

                                EventBus.getDefault().post(new AcceptEmptyEvent("В данной общей накладной нет S-накладных"));

                            } else{

//                                view.showRoutesEmptyData();

                            }
                        },
                        throwable -> {

                            EventBus.getDefault().post(new AcceptErrorEvent(throwable));

                        });

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.createExponentialBackoff(runCount, 1000);

    }
}
