package ru.startandroid.retrofit.presenter;

import ru.startandroid.retrofit.Model.BodyForCreateInvoiceWithout;
import ru.startandroid.retrofit.Model.CreateResponse;
import ru.startandroid.retrofit.Model.acceptgen.Example;
import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.R;
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

        Observable<Example> acceptGeneralInvoice =
                NetworkService.getApiService().acceptGeneralInvoiceNew(generalInvoiceId);

        subscription = acceptGeneralInvoice.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {
                                view.hideProgress();
                                view.showGeneralInvoiceId(response);
                            } else if (response.getStatus().equals("list-empty")){
                                view.hideProgress();
                                view.showEmptyToast("В данной общей накладной нет S-накладных");
                            } else{

                                view.showRoutesEmptyData();

                            }
                        },
                        throwable -> {
                            view.showRoutesError(throwable);
                            view.hideProgress();
                        });
    }




  /*  @Override
    public void postCreateInvoice(BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout) {
        view.showProgress();

        Observable<CreateResponse> callCreate =
                NetworkService.getApiService().postCreateGeneralInvoiceWithout(bodyForCreateInvoiceWithout);

        subscription = callCreate
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.getPostResponse(response);
                            view.hideProgress();
                        },
                        throwable -> {
                            view.showRoutesError(throwable);
                            view.hideProgress();
                        });

    }
*/


    @Override
    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public String handleStatus(String message) {
        String status = message;

        switch (message){
            case "pl-not-found":
                status = "количество присланных B-накладных и обработанных не совпадает";
                break;
            case "ll-not-found":
                status = "количество присланных G-накладных и обработанных не совпадает";
                break;
            case "fl-not-found":
                status = "присланное расписание не найдено";
                break;
            case "tl-not-found":
                status = "не найдена текущая Т-накладная, которая ищется в Базе \tданных по слудющим параметрам: status = «DEPARTED» и fligh = \tflighId(присланный от клиента);";
                break;
            case "fl-id-null":
                status = "присланный Id расписания null";
                break;
            case "from-dep-not-found":
                status = "не найден департамент, откуда был отправлен транспорт";
                break;
            case "dep-not-found":
                status = "департамент, куда направляется О-накладная, не найден";
                break;
            case "empty-data":
                status = "пришла пустая модель от клиента";
                break;
            case "empty-to-dep-index":
                status = "Вы отправили пустой индекс департамента, куда напраляется О-накладная";
                break;
            case "empty-from-dep-index":
                status = "пришел пустой индекс, откуда был отправлен транспорт";
                break;
        }
        return status;
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
