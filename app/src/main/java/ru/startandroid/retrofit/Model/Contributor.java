package ru.startandroid.retrofit.Model;

/**
 * Created by zhangali on 18.12.16.
 */

public class Contributor {

    String login;
    String html_url;

    int contributions;

    @Override
    public String toString() {
        return login + " (" + contributions + ")";
    }
}