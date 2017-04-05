package kz.kazpost.toolpar.Model;
import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class LastActions extends RealmObject implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("history")
    @Expose
    private RealmList<History> history = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RealmList<History> getHistory() {
        return history;
    }

    public void setHistory(RealmList<History> history) {
        this.history = history;
    }
}