package kz.kazpost.toolpar.Model.acceptgen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by root on 2/3/17.
 */

public class PacketList extends RealmObject{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("packetListId")
    @Expose
    private String packetListid;
    @SerializedName("fromDep")
    @Expose
    private FromDep_ fromDep;
    @SerializedName("toDep")
    @Expose
    private ToDep_ toDep;

    private Boolean isAddedToInvoice = false;

    private Boolean isCollated = false;

    public Boolean getAddedToInvoice() {
        return isAddedToInvoice;
    }

    public void setAddedToInvoice(Boolean addedToInvoice) {
        isAddedToInvoice = addedToInvoice;
    }

    public Boolean getIsCollated() {
        return isCollated;
    }

    public void setIsCollated(Boolean collated) {
        isCollated = collated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPacketListid() {
        return packetListid;
    }

    public void setPacketListid(String labelListid) {
        this.packetListid = labelListid;
    }

    public FromDep_ getFromDep() {
        return fromDep;
    }

    public void setFromDep(FromDep_ fromDep) {
        this.fromDep = fromDep;
    }

    public ToDep_ getToDep() {
        return toDep;
    }

    public void setToDep(ToDep_ toDep) {
        this.toDep = toDep;
    }

}
