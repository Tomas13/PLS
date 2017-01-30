package ru.startandroid.retrofit.Model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by root on 1/30/17.
 */

public class SendInvoice extends RealmObject{
    private String where;
    private BodyForCreateInvoice bodyForCreateInvoice;

    public SendInvoice() {
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setBodyForCreateInvoice(BodyForCreateInvoice bodyForCreateInvoice) {
        this.bodyForCreateInvoice = bodyForCreateInvoice;
    }

    public String getWhere() {
        return where;
    }

    public BodyForCreateInvoice getBodyForCreateInvoice() {
        return bodyForCreateInvoice;
    }
}
