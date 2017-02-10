package ru.startandroid.retrofit;

import java.util.List;

import ru.startandroid.retrofit.Model.routes.Flight;
import ru.startandroid.retrofit.Model.routes.Routes;

/**
 * Created by zhangali on 19.12.16.
 */


public class Const {
    public static final int ScannerSelectionBarcodeActivity = 1;
    public static final String CACHE_FILE_NAME = "PLS_FILE";
    public static String Token = null;
    public static String FLIGHT_ROUTES = "";
    public static Boolean isConnected = false;

//    public static final String BASE_URL = "http://172.30.75.218/";
    public static final String BASE_URL = "http://pls-test.post.kz/";

    public static List<Flight> ROUTES;

    public static final String ACCEPT_HISTORY_STATUS = "ACCEPT_GENERAL_INVOICE";
    public static final String COLLATE_HISTORY_STATUS = "COLLATE_DESTINATION_LIST";
    public static final String CREATED_HISTORY_STATUS = "CREATED";

    public static final String NAV_SHARED_PREF = "NAV_PREF";
    public static final String TOKEN_SHARED_PREF = "TOKEN_PREF";
    public static final String TOKEN = "TOKEN";
    public static final String FLIGHT_SHARED_PREF = "FLIGHT_PREF";
    public static final String FLIGHT_NAME = "FLIGHT_NAME";
    public static final String FLIGHT_ID = "FLIGHT_ID";
    public static final String TRANSPONST_LIST_ID = "TRANSPONST_LIST_ID";
    public static final String NUMBER_OF_CITIES = "NUMBER_OF_CITIES";
    public static final String CURRENT_ROUTE_POSITION = "CURRENT_ROUTE_POSITION";
    public static final String FLIGHT_POS = "FLIGHT_POS";
    public static final String LOGIN_PREF = "LOGIN_PREF";
    public static final String LOGIN_BOOL = "LOGIN_BOOL";



    public static final int COLLATE_PRIORITY = 5;
    public static final int CREATE_INVOICE_PRIORITY = 5;
    public static final int ACCEPT_INVOICE_PRIORITY = 5;
    public static final int LOAD_INVOICE_PRIORITY = 5;
    public static final int LOAD_ROUTES_PRIORITY = 5;


    public static final int HISTORY_PRIORITY = 5;

}
