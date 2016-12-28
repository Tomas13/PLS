package ru.startandroid.retrofit.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FromDep {

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
    @SerializedName("newPostindex")
    @Expose
    private String newPostindex;

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

    public String getNewPostindex() {
        return newPostindex;
    }

    public void setNewPostindex(String newPostindex) {
        this.newPostindex = newPostindex;
    }

}