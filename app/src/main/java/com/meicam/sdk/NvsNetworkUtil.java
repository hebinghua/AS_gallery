package com.meicam.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* loaded from: classes.dex */
public class NvsNetworkUtil {
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_NONE = -1;
    public static final int TYPE_WIFI = 1;

    public static final int getNetWorkStates(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return -1;
        }
        int type = activeNetworkInfo.getType();
        if (type == 0) {
            return 0;
        }
        return type != 1 ? -1 : 1;
    }
}
