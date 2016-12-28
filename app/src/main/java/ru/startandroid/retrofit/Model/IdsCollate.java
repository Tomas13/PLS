package ru.startandroid.retrofit.Model;

import com.google.gson.annotations.SerializedName;

import java.sql.Array;
import java.util.List;

/**
 * Created by root on 12/28/16.
 */

public class IdsCollate {
    @SerializedName("ids")
    List<Long> id;

    public IdsCollate(List<Long> id){
        this.id = id;
    }
}
