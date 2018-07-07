package com.example.prarthana.newsapp.Sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public boolean checkNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo!=null && networkInfo.isConnectedOrConnecting();

        //to check if connected to wifi
        if(isConnected){
            boolean isWifi= networkInfo.getType()==ConnectivityManager.TYPE_WIFI;
            return  isWifi;
        }
        return isConnected;

    }
}
