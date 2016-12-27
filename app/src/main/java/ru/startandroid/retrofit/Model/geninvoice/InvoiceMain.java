package ru.startandroid.retrofit.Model.geninvoice;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceMain {

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