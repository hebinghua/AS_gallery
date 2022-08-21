package cn.wps.kmo.kmoservice_sdk.service;

import android.text.TextUtils;
import java.util.HashMap;

/* loaded from: classes.dex */
public class CorServiceFac {
    public static CorServiceFac mCorServiceFac;
    public static HashMap<String, CorServiceManager> mCorServiceManager;

    public static synchronized CorServiceFac getInstance() {
        CorServiceFac corServiceFac;
        synchronized (CorServiceFac.class) {
            if (mCorServiceFac == null) {
                synchronized (CorServiceFac.class) {
                    mCorServiceFac = new CorServiceFac();
                    mCorServiceManager = new HashMap<>();
                }
            }
            corServiceFac = mCorServiceFac;
        }
        return corServiceFac;
    }

    public void registerCorServiceManager(String str, String str2) {
        HashMap<String, CorServiceManager> hashMap;
        String corServiceKey = getCorServiceKey(str, str2);
        if (TextUtils.isEmpty(corServiceKey) || (hashMap = mCorServiceManager) == null) {
            return;
        }
        hashMap.put(corServiceKey, new CorServiceManager());
    }

    public CorServiceManager getCorServiceManager(String str, String str2) {
        HashMap<String, CorServiceManager> hashMap;
        String corServiceKey = getCorServiceKey(str, str2);
        if (!TextUtils.isEmpty(corServiceKey) && (hashMap = mCorServiceManager) != null && hashMap.size() > 0 && mCorServiceManager.containsKey(corServiceKey)) {
            return mCorServiceManager.get(corServiceKey);
        }
        return null;
    }

    public void removeCorServiceManager(String str, String str2) {
        HashMap<String, CorServiceManager> hashMap;
        String corServiceKey = getCorServiceKey(str, str2);
        if (!TextUtils.isEmpty(corServiceKey) && (hashMap = mCorServiceManager) != null && hashMap.containsKey(corServiceKey)) {
            mCorServiceManager.remove(corServiceKey);
        }
    }

    public final String getCorServiceKey(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            return str + "&&" + str2;
        }
        return "";
    }
}
