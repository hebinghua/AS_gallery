package com.market.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.market.sdk.utils.AppGlobal;
import com.xiaomi.market.IAppDownloadManager;

/* loaded from: classes.dex */
public class FloatCardManager {
    @SuppressLint({"StaticFieldLeak"})
    public static volatile FloatCardManager sInstance;
    public String targetPackage;

    public static FloatCardManager get(Application application) {
        if (sInstance == null) {
            synchronized (FloatCardManager.class) {
                if (sInstance == null) {
                    sInstance = new FloatCardManager();
                    application.registerActivityLifecycleCallbacks(new AppActivityLifecycleTracker());
                }
            }
        }
        return sInstance;
    }

    public boolean downloadByFloat(String str) {
        if (!TextUtils.isEmpty(str) && str.contains("&overlayPosition=")) {
            try {
                FloatService.openService(AppGlobal.getContext(), this.targetPackage).downloadByUri(Uri.parse(str));
                return true;
            } catch (Exception e) {
                Log.e("MarketManager", e.toString());
            }
        }
        return false;
    }

    public boolean resumeByFloat(final String str) {
        if (!TextUtils.isEmpty(str) && str.contains("&overlayPosition=")) {
            try {
                final IAppDownloadManager openService = FloatService.openService(AppGlobal.getContext(), this.targetPackage);
                if (MarketManager.getManager().hasFeature(MarketFeatures.FLOAT_CARD)) {
                    openService.resumeByUri(Uri.parse(str));
                    return true;
                }
                new Thread(new Runnable() { // from class: com.market.sdk.FloatCardManager.4
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            openService.resume(FloatCardManager.this.getStringFromUri(Uri.parse(str), "packageName"), AppGlobal.getContext().getPackageName());
                        } catch (RemoteException unused) {
                        }
                    }
                }).start();
                return true;
            } catch (Exception e) {
                Log.e("MarketManager", e.toString());
            }
        }
        return false;
    }

    public boolean lifecycleChanged(Activity activity, int i) {
        try {
            FloatService.openService(AppGlobal.getContext(), this.targetPackage).lifecycleChanged(activity.toString(), i);
            return true;
        } catch (Exception e) {
            Log.e("MarketManager", e.toString());
            return false;
        }
    }

    public final String getStringFromUri(Uri uri, String str) {
        return (uri == null || !uri.isHierarchical()) ? "" : uri.getQueryParameter(str);
    }
}
