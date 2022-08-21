package cn.kuaipan.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.text.TextUtils;
import org.apache.http.HttpHost;

/* loaded from: classes.dex */
public class NetworkHelpers {
    public static int getCurrentNetType(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
            return -1;
        }
        return activeNetworkInfo.getType();
    }

    public static HttpHost getCurrentProxy() {
        if (getCurrentNetType(ContextUtils.getContext()) != 0) {
            return null;
        }
        String defaultHost = Proxy.getDefaultHost();
        int defaultPort = Proxy.getDefaultPort();
        if (TextUtils.isEmpty(defaultHost)) {
            return null;
        }
        return new HttpHost(defaultHost, defaultPort);
    }
}
