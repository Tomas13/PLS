package kz.kazpost.toolpar.Model;

import io.realm.RealmObject;

/**
 * Created by root on 1/30/17.
 */

public class SendInvoice extends RealmObject{
    private String from;
    private String to;
    private BodyForCreateInvoice bodyForCreateInvoice;

    public SendInvoice() {
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setBodyForCreateInvoice(BodyForCreateInvoice bodyForCreateInvoice) {
        this.bodyForCreateInvoice = bodyForCreateInvoice;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public BodyForCreateInvoice getBodyForCreateInvoice() {
        return bodyForCreateInvoice;
    }
}
