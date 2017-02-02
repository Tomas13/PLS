package ru.startandroid.retrofit.Model.acceptgen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Destinations extends RealmObject {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("destinations")
    @Expose
    private RealmList<Sinvoice> destinations = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RealmList<Sinvoice> getDestinations() {
        return destinations;
    }

    public void setDestinations(RealmList<Sinvoice> destinations) {
        this.destinations = destinations;
    }

}