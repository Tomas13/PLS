package ru.startandroid.retrofit.Model;

/**
 * Created by zhangali on 22.12.16.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralInvoice {

    @SerializedName("generalInvoiceId")
    @Expose
    private String generalInvoiceId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdDate")
    @Expose
    private Integer createdDate;
    @SerializedName("fromDepName")
    @Expose
    private String fromDepName;
    @SerializedName("toDepName")
    @Expose
    private String toDepName;
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

    public Integer getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Integer createdDate) {
        this.createdDate = createdDate;
    }

    public String getFromDepName() {
        return fromDepName;
    }

    public void setFromDepName(String fromDepName) {
        this.fromDepName = fromDepName;
    }

    public String getToDepName() {
        return toDepName;
    }

    public void setToDepName(String toDepName) {
        this.toDepName = toDepName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}