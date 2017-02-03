package ru.startandroid.retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by root on 1/4/17.
 */

public class BodyForCreateInvoice extends RealmObject {

    @SerializedName("flightId")
    @Expose
    private Long flightId;
    @SerializedName("tlId")
    @Expose
    private Long tlId;
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
    private RealmList<RealmLong> labelIds = null;
    @SerializedName("packetIds")
    @Expose
    private RealmList<RealmLong> packetIds = null;

    public BodyForCreateInvoice() {
    }

    public BodyForCreateInvoice(Long flightId, Long tlid, Boolean isDepIndex, String toDepIndex, String fromDepIndex, RealmList<RealmLong> labelIds, RealmList<RealmLong> packetIds) {
        this.flightId = flightId;
        this.tlId = tlid;
        this.isDepIndex = isDepIndex;
        this.toDepIndex = toDepIndex;
        this.fromDepIndex = fromDepIndex;
        this.labelIds = labelIds;
        this.packetIds = packetIds;
    }

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

    public RealmList<RealmLong> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(RealmList<RealmLong> labelIds) {
        this.labelIds = labelIds;
    }

    public RealmList<RealmLong> getPacketIds() {
        return packetIds;
    }

    public void setPacketIds(RealmList<RealmLong> packetIds) {
        this.packetIds = packetIds;
    }

}

