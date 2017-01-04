package ru.startandroid.retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 1/4/17.
 */

public class BodyForCreateInvoice {

    @SerializedName("flightId")
    @Expose
    private Long flightId;
    @SerializedName("isDepIndex")
    @Expose
    private Boolean isDepIndex;
    @SerializedName("toDepIndex")
    @Expose
    private String toDepIndex;
    @SerializedName("fromDepIndex")
    @Expose
    private String fromDepIndex;
    @SerializedName("labelIds")
    @Expose
    private List<Long> labelIds = null;
    @SerializedName("packetIds")
    @Expose
    private List<Long> packetIds = null;

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Boolean getIsDepIndex() {
        return isDepIndex;
    }

    public void setIsDepIndex(Boolean isDepIndex) {
        this.isDepIndex = isDepIndex;
    }

    public String getToDepIndex() {
        return toDepIndex;
    }

    public void setToDepIndex(String toDepIndex) {
        this.toDepIndex = toDepIndex;
    }

    public String getFromDepIndex() {
        return fromDepIndex;
    }

    public void setFromDepIndex(String fromDepIndex) {
        this.fromDepIndex = fromDepIndex;
    }

    public List<Long> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<Long> labelIds) {
        this.labelIds = labelIds;
    }

    public List<Long> getPacketIds() {
        return packetIds;
    }

    public void setPacketIds(List<Long> packetIds) {
        this.packetIds = packetIds;
    }

}

