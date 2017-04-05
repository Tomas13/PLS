package kz.kazpost.toolpar.Model.acceptgen;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Destination extends RealmObject{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("destinationListId")
    @Expose
    private String destinationListId;
    @SerializedName("fromDep")
    @Expose
    private FromDepNew fromDep;
    @SerializedName("toDep")
    @Expose
    private ToDepNew toDep;
    @SerializedName("packetList")
    @Expose
    private RealmList<PacketList> packetList = null;
    @SerializedName("labelList")
    @Expose
    private RealmList<LabelList> labelList = null;


    private Boolean isChecked = false;

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean checked) {
        isChecked = checked;
    }
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

    public FromDepNew getFromDep() {
        return fromDep;
    }

    public void setFromDep(FromDepNew fromDep) {
        this.fromDep = fromDep;
    }

    public ToDepNew getToDeNewp() {
        return toDep;
    }

    public void setToDep(ToDepNew toDep) {
        this.toDep = toDep;
    }

    public RealmList<PacketList> getPacketList() {
        return packetList;
    }

    public void setPacketList(RealmList<PacketList> packetList) {
        this.packetList = packetList;
    }

    public RealmList<LabelList> getLabelList() {
        return labelList;
    }

    public void setLabelList(RealmList<LabelList> labelList) {
        this.labelList = labelList;
    }

}