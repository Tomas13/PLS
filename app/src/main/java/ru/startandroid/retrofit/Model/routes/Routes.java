package ru.startandroid.retrofit.Model.routes;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Routes extends RealmObject{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("flights")
    @Expose
    private RealmList<Flight> flights = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RealmList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(RealmList<Flight> flights) {
        this.flights = flights;
    }

    public int getIndex(int i){
        return flights.get(i).getFlight().getItineraryDTO().getEntries().get(i).getIndex();
    }

    public String getArrival(int i){
        return flights.get(i).getFlight().getItineraryDTO().getEntries().get(i).getArrival();
    }

    public String getName(int i){
        return flights.get(i).getFlight().getItineraryDTO().getEntries().get(i).getDept().getName();
    }


}