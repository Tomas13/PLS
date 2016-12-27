package ru.startandroid.retrofit.Model.geninvoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.startandroid.retrofit.Model.FromDep;
import ru.startandroid.retrofit.Model.ToDep;

public class GeneralInvoice {

    @SerializedName("generalInvoiceId")
    @Expose
    private String generalInvoiceId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdDate")
    @Expose
    private Long createdDate;
    @SerializedName("fromDep")
    @Expose
    private FromDep fromDep;
    @SerializedName("toDep")
    @Expose
    private ToDep toDep;
    @SerializedName("status")
    @Expose
    private String status;

    public String getGeneralInvoiceId() {
        return generalInvoiceId;
    }

    public void setGeneralInvoiceId(String generalInvoiceId) {
        this.generalInvoiceId = generalInvoiceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}