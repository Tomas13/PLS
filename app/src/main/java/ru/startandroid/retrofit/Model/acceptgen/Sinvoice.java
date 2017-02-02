package ru.startandroid.retrofit.Model.acceptgen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import ru.startandroid.retrofit.Model.FromDep;
import ru.startandroid.retrofit.Model.ToDep;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;


public class Sinvoice extends RealmObject{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("destinationListId")
    @Expose
    private String destinationListId;
    @SerializedName("fromDep")
    @Expose
    private FromDep fromDep;
    @SerializedName("toDep")
    @Expose
    private ToDep toDep;
    @SerializedName("packetList")
    @Expose
    private RealmList<Packet> packetList = null;
    @SerializedName("labelList")
    @Expose
    private RealmList<Label> labelList = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestinationListId() {
        return destinationListId;
    }

    public void setDestinationListId(String destinationListId) {
        this.destinationListId = destinationListId;
    }

    public FromDep getFromDep() {
        return fromDep;
    }

    public void setFromDep(FromDep fromDep) {
        this.fromDep = fromDep;
    }

    public ToDep getToDep() {
        return toDep;
    }

    public void setToDep(ToDep toDep) {
        this.toDep = toDep;
    }

    public RealmList<Packet> getPacket() {
        return packetList;
    }

    public void setPacket(RealmList<Packet> packetList) {
        this.packetList = packetList;
    }

    public RealmList<Label> getLabel() {
        return labelList;
    }

    public void setLabel(RealmList<Label> labelList) {
        this.labelList = labelList;
    }
}
