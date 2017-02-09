package ru.startandroid.retrofit.adapter;

/**
 * Created by root on 2/9/17.
 */

public class InvoiceErrorEvent {

    public Throwable error;

    public InvoiceErrorEvent(Throwable error) {
        this.error = error;
    }
}
