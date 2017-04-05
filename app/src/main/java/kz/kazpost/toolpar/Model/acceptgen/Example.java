package kz.kazpost.toolpar.Model.acceptgen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Example extends RealmObject{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("destinations")
    @Expose
    private RealmList<Destination> destinations = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RealmList<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(RealmList<Destination> destinations) {
        this.destinations = destinations;
    }

}