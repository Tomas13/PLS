package ru.startandroid.retrofit.Model.destinationlist;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDestinationList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("destinationLists")
    @Expose
    private List<DestinationList> destinationLists = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DestinationList> getDestinationLists() {
        return destinationLists;
    }

    public void setDestinationLists(List<DestinationList> destinationLists) {
        this.destinationLists = destinationLists;
    }

}