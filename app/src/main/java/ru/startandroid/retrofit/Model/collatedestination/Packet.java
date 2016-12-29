package ru.startandroid.retrofit.Model.collatedestination;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import ru.startandroid.retrofit.Model.FromDep;
import ru.startandroid.retrofit.Model.ToDep;

public class Packet extends RealmObject{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("packetListId")
    @Expose
    private String packetListId;
    @SerializedName("fromDep")
    @Expose
    private FromDep fromDep;
    @SerializedName("toDep")
    @Expose
    private ToDep toDep;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPacketListId() {
        return packetListId;
    }

    public void setPacketListId(String packetListId) {
        this.packetListId = packetListId;
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

}