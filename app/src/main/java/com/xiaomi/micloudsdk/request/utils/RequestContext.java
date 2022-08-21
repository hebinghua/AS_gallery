package com.xiaomi.micloudsdk.request.utils;

import android.content.Context;
import com.xiaomi.micloudsdk.data.IAuthToken;
import micloud.compat.independent.request.RequestEnvBuilderCompat;

/* loaded from: classes3.dex */
public class RequestContext {
    public static Context sContext;
    public static String sRegion;
    public static RequestEnv sRequestEnv = RequestEnvBuilderCompat.build();

    /* loaded from: classes3.dex */
    public interface RequestEnv {
        String getAccountName();

        String getUserAgent();

        void invalidateAuthToken();

        /* renamed from: queryAuthToken */
        IAuthToken mo2587queryAuthToken();

        String queryEncryptedAccountName();
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        Context context = sContext;
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("sContext=null! Please call Request.init(Context) at Application onCreate");
    }

    public static void setRegion(String str) {
        sRegion = str;
    }

    public static String getRegion() {
        return sRegion;
    }

    public static RequestEnv getRequestEnv() {
        RequestEnv requestEnv = sRequestEnv;
        if (requestEnv != null) {
            return requestEnv;
        }
        throw new IllegalStateException("RequestEnv has not been initialized yet, please call Request.setRequestEnv(RequestEnv) first!");
    }

    public static String getUserAgent() {
        RequestEnv requestEnv = sRequestEnv;
        if (requestEnv == null) {
            throw new IllegalStateException("RequestEnv has not been initialized yet, please call Request.setRequestEnv(RequestEnv) first!");
        }
        return requestEnv.getUserAgent();
    }
}
