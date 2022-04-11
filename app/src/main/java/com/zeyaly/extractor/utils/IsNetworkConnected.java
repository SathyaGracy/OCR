package com.zeyaly.extractor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by admin on 9/14/2017.
 */

public class IsNetworkConnected {
    Context mContext;

    public IsNetworkConnected(Context mContext) {
        this.mContext = mContext;
    }

    Boolean connected = false;

    public boolean isNetworkConnected() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;

    }
}
