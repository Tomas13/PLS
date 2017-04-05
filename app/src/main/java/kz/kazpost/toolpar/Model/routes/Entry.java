package kz.kazpost.toolpar.Model.routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Index;

public class Entry extends RealmObject {

    @SerializedName("dept")
    @Expose
    private Dept dept;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("index")
    @Expose
    @Index
    private Integer index;
    @SerializedName("travelTime")
    @Expose
    private String travelTime;
    @SerializedName("stationTime")
    @Expose
    private String stationTime;
    @SerializedName("arrival")
    @Expose
    private String arrival;

    @SerializedName("departure")
    @Expose
    private String departure;


    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getStationTime() {
        return stationTime;
    }

    public void setStationTime(String stationTime) {
        this.stationTime = stationTime;
    }


}