package kz.kazpost.toolpar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Edges {

    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("frwd_start")
    @Expose
    private String frwdStart;
    @SerializedName("frwd_end")
    @Expose
    private String frwdEnd;
    @SerializedName("bkwd_start")
    @Expose
    private String bkwdStart;
    @SerializedName("bkwd_end")
    @Expose
    private String bkwdEnd;
    @SerializedName("interval")
    @Expose
    private String interval;
    @SerializedName("group")
    @Expose
    private Integer group;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getFrwdStart() {
        return frwdStart;
    }

    public void setFrwdStart(String frwdStart) {
        this.frwdStart = frwdStart;
    }

    public String getFrwdEnd() {
        return frwdEnd;
    }

    public void setFrwdEnd(String frwdEnd) {
        this.frwdEnd = frwdEnd;
    }

    public String getBkwdStart() {
        return bkwdStart;
    }

    public void setBkwdStart(String bkwdStart) {
        this.bkwdStart = bkwdStart;
    }

    public String getBkwdEnd() {
        return bkwdEnd;
    }

    public void setBkwdEnd(String bkwdEnd) {
        this.bkwdEnd = bkwdEnd;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }


    @Override
    public String toString() {
        return route + " " + start + " " + end;
    }
}