package kz.kazpost.toolpar.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import kz.kazpost.toolpar.Model.acceptgen.Example;
import kz.kazpost.toolpar.events.AcceptEmptyEvent;
import kz.kazpost.toolpar.events.AcceptErrorEvent;
import kz.kazpost.toolpar.events.AcceptGenInvoiceEvent;
import kz.kazpost.toolpar.models.NetworkService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.ACCEPT_INVOICE_PRIORITY;


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
