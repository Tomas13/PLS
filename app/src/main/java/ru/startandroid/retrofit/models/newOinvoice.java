package ru.startandroid.retrofit.models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;

public class newOinvoice {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("destinations")
    @Expose
    private List<Destination> destinations = null;
    @SerializedName("packets")
    @Expose
    private List<Packet> packets = null;
    @SerializedName("labels")
    @Expose
    private List<Label> labels = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public List<Packet> getPackets() {
        return packets;
    }

    public void setPackets(List<Packet> packets) {
        this.packets = packets;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

}