package ru.startandroid.retrofit.Model.routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.startandroid.retrofit.Model.routes.Dept;

public class Entry {

    @SerializedName("dept")
    @Expose
    private Dept dept;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("travelTime")
    @Expose
    private String travelTime;
    @SerializedName("arrival")
    @Expose
    private String arrival;

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

}