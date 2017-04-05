package kz.kazpost.toolpar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 12/28/16.
 */

public class IdsCollate implements Serializable{
    @SerializedName("ids")
    List<Long> id;

    public IdsCollate(List<Long> id){
        this.id = id;
    }
}
