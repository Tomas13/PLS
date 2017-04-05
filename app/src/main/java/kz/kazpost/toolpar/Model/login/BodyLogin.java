package kz.kazpost.toolpar.Model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by root on 3/7/17.
 */

public class BodyLogin implements Serializable {

    @Expose
    @SerializedName("Cache-Control")
    String CacheControl = "no-cache";

    @Expose
    @SerializedName("grant_type")
    String grant_type = "password";

    @Expose
    @SerializedName("client_id")
    String client_id = "toolpar-mobile";

    @Expose
    @SerializedName("scope")
    String scope = "offline_access";

    @Expose
    @SerializedName("username")
    String username;

    @Expose
    @SerializedName("password")
    String password;

    public BodyLogin(String username1, String password1) {
        this.username = username1;
        this.password = password1;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
