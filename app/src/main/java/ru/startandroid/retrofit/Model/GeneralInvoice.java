package ru.startandroid.retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhangali on 22.12.16.
 */

public class GeneralInvoice {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("invoices")
    @Expose
    private List<Invoice> invoices = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

}