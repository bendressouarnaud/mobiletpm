package com.ankk.tpmtn.mesobjets;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class BoiteOutil {



    // M e t h o d s :
    public static boolean checkInternet(Context ct){
        ConnectivityManager cm =
                (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities ns = cm.getNetworkCapabilities(cm.getActiveNetwork());// .getActiveNetworkInfo();
        boolean isConnected = ns != null ? true : false;
        return isConnected;
    }
}
