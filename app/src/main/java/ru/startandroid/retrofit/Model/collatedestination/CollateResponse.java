package ru.startandroid.retrofit.Model.collatedestination;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollateResponse {

    @SerializedName("dto")
    @Expose
    private Dto dto;
    @SerializedName("status")
    @Expose
    private String status;

    public Dto getDto() {
        return dto;
    }

    public void setDto(Dto dto) {
        this.dto = dto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}