package ru.startandroid.retrofit.Model.acceptgen;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Destination {

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
    private List<Object> packetList = null;
    @SerializedName("labelList")
    @Expose
    private List<LabelList> labelList = null;

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

    public List<Object> getPacketList() {
        return packetList;
    }

    public void setPacketList(List<Object> packetList) {
        this.packetList = packetList;
    }

    public List<LabelList> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<LabelList> labelList) {
        this.labelList = labelList;
    }

}