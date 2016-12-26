package ru.startandroid.retrofit.Model.routes;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.startandroid.retrofit.Model.routes.Flight;

public class Routes {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("flights")
    @Expose
    private List<Flight> flights = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public int getIndex(int i){
        return flights.get(i).getItineraryDTO().getEntries().get(i).getIndex();
    }
}