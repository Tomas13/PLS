package kz.kazpost.toolpar.Model.acceptgen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ToDep_ extends RealmObject {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("postindex")
    @Expose
    private String postindex;
    @SerializedName("nameRu")
    @Expose
    private String nameRu;
    @SerializedName("techindex")
    @Expose
    private String techindex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostindex() {
        return postindex;
    }

    public void setPostindex(String postindex) {
        this.postindex = postindex;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getTechindex() {
        return techindex;
    }

    public void setTechindex(String techindex) {
        this.techindex = techindex;
    }

}