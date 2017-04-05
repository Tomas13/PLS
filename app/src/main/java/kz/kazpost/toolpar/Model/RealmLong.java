package kz.kazpost.toolpar.Model;

import io.realm.RealmObject;

/**
 * Created by root on 1/30/17.
 */

public class RealmLong  extends RealmObject {
    private Long aLong;

    public RealmLong(){

    }

    public Long getaLong() {
        return aLong;
    }

    public RealmLong(Long aLong) {
        this.aLong = aLong;
    }
}

