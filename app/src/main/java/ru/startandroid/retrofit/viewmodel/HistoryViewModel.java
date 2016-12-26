package ru.startandroid.retrofit.viewmodel;

import android.databinding.BaseObservable;

import ru.startandroid.retrofit.Model.History;

/**
 * Created by root on 12/26/16.
 */

public class HistoryViewModel extends BaseObservable {
    private final History model;

    public HistoryViewModel(History model) {
        this.model = model;
    }

    public History getModel()
    {
        return model;
    }

    public void getInvoice(){
        model.getInvoiceId();
    }

    public void getFromDep(){
        model.getFromDep();
    }

    public void getToDep(){
        model.getToDep();
    }


}
