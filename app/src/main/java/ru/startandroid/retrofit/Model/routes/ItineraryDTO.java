package ru.startandroid.retrofit.Model.routes;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.startandroid.retrofit.Model.routes.Entry;

public class ItineraryDTO {

    @SerializedName("entries")
    @Expose
    private List<Entry> entries = null;
    @SerializedName("haveFlight")
    @Expose
    private Boolean haveFlight;

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public Boolean getHaveFlight() {
        return haveFlight;
    }

    public void setHaveFlight(Boolean haveFlight) {
        this.haveFlight = haveFlight;
    }

}