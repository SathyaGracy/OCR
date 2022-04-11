package com.zeyaly.extractor.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InternetLostReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}