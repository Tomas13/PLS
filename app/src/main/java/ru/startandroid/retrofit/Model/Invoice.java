package ru.startandroid.retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhangali on 22.12.16.
 */

public class Invoice {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("generalInvoices")
    @Expose
    private List<GeneralInvoice> generalInvoices = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeneralInvoice> getGeneralInvoices() {
        return generalInvoices;
    }

    public void setGeneralInvoices(List<GeneralInvoice> generalInvoices) {
        this.generalInvoices = generalInvoices;
    }

}