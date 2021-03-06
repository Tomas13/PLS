package kz.kazpost.toolpar;

import java.util.List;

import kz.kazpost.toolpar.Model.routes.Flight;

/**
 * Created by zhangali on 19.12.16.
 */


public class Const {
    public static final int ScannerSelectionBarcodeActivity = 1;
    public static final String CACHE_FILE_NAME = "PLS_FILE";
    public static final String PREF_NAME =  "pls_pref";
    ;
    public static String Token = "";
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
    public static final String INVOICE_PREF = "INVOICE_PREF";
    public static final String INVOICE_NAME = "INVOICE_NAME";
    public static final String TOKEN = "TOKEN";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String FLIGHT_SHARED_PREF = "FLIGHT_PREF";
    public static final String FAKE = "FAKE";
    public static final String FLIGHT_NAME = "FLIGHT_NAME";
    public static final String FLIGHT_ID = "FLIGHT_ID";
    public static final String TRANSPONST_LIST_ID = "TRANSPONST_LIST_ID";
    public static final String NUMBER_OF_CITIES = "NUMBER_OF_CITIES";
    public static final String CURRENT_ROUTE_POSITION = "CURRENT_ROUTE_POSITION";
    public static final String COPY_ROUTE= "COPY_ROUTE";
    public static final String FLIGHT_POS = "FLIGHT_POS";
    public static final String LOGIN_PREF = "LOGIN_PREF";
    public static final String LOGIN_BOOL = "LOGIN_BOOL";


    public static String usernameConst;
    public static String passwordConst;
    public static String AccessTokenConst;



    public static final int COLLATE_PRIORITY = 40;
    public static final int CREATE_INVOICE_PRIORITY = 30;
    public static final int ACCEPT_INVOICE_PRIORITY = 50;
    public static final int LOAD_INVOICE_PRIORITY = 5;
    public static final int LOAD_ROUTES_PRIORITY = 5;


    public static final int HISTORY_PRIORITY = 5;
    public static final int ACCESS_TOKEN_PRIORITY = 100;

}
