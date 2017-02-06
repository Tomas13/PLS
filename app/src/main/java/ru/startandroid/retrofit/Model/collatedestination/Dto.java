package ru.startandroid.retrofit.Model.collatedestination;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import ru.startandroid.retrofit.Model.acceptgen.LabelList;
import ru.startandroid.retrofit.Model.acceptgen.PacketList;

public class Dto extends RealmObject{

    @SerializedName("labels")
    @Expose
    private RealmList<LabelList> labels = null;
    @SerializedName("packets")
    @Expose
    private RealmList<PacketList> packets = null;

    public RealmList<LabelList> getLabels() {
        return labels;
    }

    public void setLabels(RealmList<LabelList> labels) {
        this.labels = labels;
    }

    public RealmList<PacketList> getPackets() {
        return packets;
    }

    public void setPackets(RealmList<PacketList> packets) {
        this.packets = packets;
    }

}