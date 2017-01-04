package ru.startandroid.retrofit.Model.collatedestination;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import ru.startandroid.retrofit.Model.FromDep;
import ru.startandroid.retrofit.Model.ToDep;

public class Label extends RealmObject {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("labelListid")
    @Expose
    private String labelListid;
    @SerializedName("fromDep")
    @Expose
    private FromDep fromDep;
    @SerializedName("toDep")
    @Expose
    private ToDep toDep;

   /* private  String ClickStatus = "UnClick";

    public String getClickStatus() {
        return ClickStatus;
    }

    public void setClickStatus(String clickStatus) {
        ClickStatus = clickStatus;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelListid() {
        return labelListid;
    }

    public void setLabelListid(String labelListid) {
        this.labelListid = labelListid;
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