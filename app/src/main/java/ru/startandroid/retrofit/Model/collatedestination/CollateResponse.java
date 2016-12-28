package ru.startandroid.retrofit.Model.collatedestination;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollateResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("labels")
    @Expose
    private List<Label> labels = null;
    @SerializedName("packets")
    @Expose
    private List<Packet> packets = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Packet> getPackets() {
        return packets;
    }

    public void setPackets(List<Packet> packets) {
        this.packets = packets;
    }

}