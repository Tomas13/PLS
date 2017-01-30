package ru.startandroid.retrofit.Model;

import io.realm.RealmObject;

/**
 * Created by root on 1/30/17.
 */

public class RealmLong  extends RealmObject {
    private Long aLong;

    public RealmLong(){

    }

    public RealmLong(Long aLong) {
        this.aLong = aLong;
    }
}

