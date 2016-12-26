package ru.startandroid.retrofit.viewmodel;

import android.databinding.BaseObservable;

import java.util.List;

import ru.startandroid.retrofit.Model.routes.Flight;
import ru.startandroid.retrofit.Model.routes.Routes;

/**
 * Created by root on 12/26/16.
 */

public class RoutesViewModel extends BaseObservable {
    private final Routes model;

    public RoutesViewModel(Routes model) {
        this.model = model;
    }

    public Routes getModel() {
        return model;
    }

    public List<Flight> getFlights(){
        return model.getFlights();
    }


}
