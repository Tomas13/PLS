package ru.startandroid.retrofit.models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.startandroid.retrofit.Model.acceptgen.LabelList;
import ru.startandroid.retrofit.Model.acceptgen.PacketList;

public class newOinvoice {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("destinations")
    @Expose
    private List<Destination> destinations = null;
    @SerializedName("packets")
    @Expose
    private List<PacketList> packets = null;
    @SerializedName("labels")
    @Expose
    private List<LabelList> labels = null;

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

    public List<PacketList> getPackets() {
        return packets;
    }

    public void setPackets(List<PacketList> packets) {
        this.packets = packets;
    }

    public List<LabelList> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelList> labels) {
        this.labels = labels;
    }

}