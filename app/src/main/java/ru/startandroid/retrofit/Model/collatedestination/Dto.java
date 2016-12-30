package ru.startandroid.retrofit.Model.collatedestination;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Dto extends RealmObject{

    @SerializedName("labels")
    @Expose
    private RealmList<Label> labels = null;
    @SerializedName("packets")
    @Expose
    private RealmList<Packet> packets = null;

    public RealmList<Label> getLabels() {
        return labels;
    }

    public void setLabels(RealmList<Label> labels) {
        this.labels = labels;
    }

    public RealmList<Packet> getPackets() {
        return packets;
    }

    public void setPackets(RealmList<Packet> packets) {
        this.packets = packets;
    }

}