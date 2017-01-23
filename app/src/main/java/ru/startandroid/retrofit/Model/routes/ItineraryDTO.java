package ru.startandroid.retrofit.Model.routes;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import ru.startandroid.retrofit.Model.routes.Entry;

public class ItineraryDTO extends RealmObject{

    @SerializedName("entries")
    @Expose
    private RealmList<Entry> entries = null;
    @SerializedName("haveFlight")
    @Expose
    private Boolean haveFlight;

    public RealmList<Entry> getEntries() {
        return entries;
    }

    public void setEntries(RealmList<Entry> entries) {
        this.entries = entries;
    }

    public Boolean getHaveFlight() {
        return haveFlight;
    }

    public void setHaveFlight(Boolean haveFlight) {
        this.haveFlight = haveFlight;
    }

}