package ru.startandroid.retrofit.Model.routes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Flight extends RealmObject{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("transportListId")
    @Expose
    private String transportListId;
    @SerializedName("flight")
    @Expose
    private Flight_ flight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransportListId() {
        return transportListId;
    }

    public void setTransportListId(String transportListId) {
        this.transportListId = transportListId;
    }

    public Flight_ getFlight() {
        return flight;
    }

    public void setFlight(Flight_ flight) {
        this.flight = flight;
    }
}