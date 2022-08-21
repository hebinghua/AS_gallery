package com.xiaomi.micloudsdk.request.utils;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.utils.JsonUtils;
import com.xiaomi.micloudsdk.utils.MiCloudConstants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import micloud.compat.independent.utils.RelocationCacheCompat;
import miui.cloud.common.XLogger;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"NewApi"})
/* loaded from: classes3.dex */
public class CloudRelocationUtils {
    public static volatile Object sNeedInitHostList;
    public static volatile boolean sNeedUpdateHostList;
    public static UpdateStatus sUpdateStatus;
    public static final int[] SERVER_RETRY_INTERVALS = {1000, 2000, 5000, 10000};
    public static final String URL_RELOCATION_QUERY = MiCloudConstants.URL.URL_RELOCATION_BASE + "/mic/relocation/v3/user/record";
    public static volatile Map<String, Object> sHostsCacheInner = new HashMap();
    public static Object sUpdateMiCloudHostsLock = new Object();

    /* loaded from: classes3.dex */
    public enum UpdateStatus {
        UPDATING,
        SUCCESS,
        FAILED
    }

    public static void updateHostCache(Map<String, Object> map) {
        sHostsCacheInner = new HashMap(map);
    }

    public static Map<String, Object> getHostCache() {
        return new HashMap(sHostsCacheInner);
    }

    public static boolean needUpdateHostListAndInitIfNeeded() {
        if (sNeedInitHostList == null) {
            synchronized (sUpdateMiCloudHostsLock) {
                if (sNeedInitHostList == null) {
                    sNeedInitHostList = new Object();
                    sNeedUpdateHostList = TextUtils.isEmpty(RelocationCacheCompat.getCachedHostList(RequestContext.getContext()));
                }
            }
        }
        return sNeedUpdateHostList;
    }

    public static void setNeedUpdateHostsList(boolean z) {
        sNeedUpdateHostList = z;
    }

    public static String checkRedirect(String str, int i) throws CloudServerException {
        if (i >= 15) {
            throw new CloudServerException(503, 10034, 10);
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.getInt("code") == 308) {
                if (jSONObject.getJSONObject("data").optBoolean("isPermanent")) {
                    setNeedUpdateHostsList(true);
                }
                return jSONObject.getJSONObject("data").getString("redirectUrl");
            } else if (jSONObject.getInt("code") == 503) {
                throw new CloudServerException(503, 503, jSONObject.getJSONObject("data").getInt("retryAfter"));
            } else {
                if (jSONObject.getInt("code") == 10034) {
                    throw new CloudServerException(503, 10034, jSONObject.getJSONObject("data").getInt("retryAfter"));
                }
                return null;
            }
        } catch (JSONException e) {
            Log.e("Micloud", "JSONException in checkRedirect():" + str, e);
            return null;
        }
    }

    public static int isInternationalAccount(boolean z) {
        String host;
        if (z) {
            updateHostCache(new HashMap());
        }
        if (getHostCache().isEmpty()) {
            setNeedUpdateHostsList(true);
            try {
                updateMiCloudHosts(z);
                if (getHostCache().isEmpty()) {
                    return 2;
                }
            } catch (CloudServerException e) {
                Log.e("Micloud", "CloudServerException in isInternationalAccount", e);
                return 2;
            }
        }
        for (Map.Entry<String, Object> entry : getHostCache().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if ((value instanceof String) && (host = Uri.parse((String) value).getHost()) != null) {
                return host.equals(key) ? 0 : 1;
            }
        }
        return 2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:115:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0178, code lost:
        r0 = com.xiaomi.micloudsdk.request.utils.CloudRelocationUtils.sUpdateMiCloudHostsLock;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x017a, code lost:
        monitor-enter(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x017b, code lost:
        com.xiaomi.micloudsdk.request.utils.CloudRelocationUtils.sUpdateStatus = com.xiaomi.micloudsdk.request.utils.CloudRelocationUtils.UpdateStatus.SUCCESS;
        com.xiaomi.micloudsdk.request.utils.CloudRelocationUtils.sUpdateMiCloudHostsLock.notifyAll();
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0184, code lost:
        monitor-exit(r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void updateMiCloudHosts(boolean r7) throws com.xiaomi.micloudsdk.exception.CloudServerException {
        /*
            Method dump skipped, instructions count: 581
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.micloudsdk.request.utils.CloudRelocationUtils.updateMiCloudHosts(boolean):void");
    }

    public static String updateRequestHost(String str, boolean z) throws CloudServerException {
        updateMiCloudHosts(z);
        try {
            XLogger.info("Micloud", "Original URL: " + str + ". ", new Object[0]);
            URL url = new URL(str);
            String hostWithScheme = getHostWithScheme(url.getHost());
            if (!TextUtils.isEmpty(hostWithScheme)) {
                XLogger.info("Micloud", "New URL: " + hostWithScheme + ". ", new Object[0]);
                URL url2 = new URL(hostWithScheme);
                str = new URL(url2.getProtocol(), url2.getHost(), url.getFile()).toString();
            }
        } catch (MalformedURLException e) {
            XLogger.error("Micloud", "MalformedURLException in updateHost %s", e);
        }
        XLogger.info("Micloud", "Final URL: " + str + ". ", new Object[0]);
        return str;
    }

    public static String getHostWithScheme(String str) {
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", "Enter getHost key=" + str);
        }
        Object obj = getHostCache().get(str);
        String str2 = (obj == null || !(obj instanceof String)) ? null : (String) obj;
        if (!TextUtils.isEmpty(str2)) {
            if (Log.isLoggable("Micloud", 3)) {
                Log.d("Micloud", "Hit host cache directly return host = " + str2);
            }
            return str2;
        }
        String cachedHostList = RelocationCacheCompat.getCachedHostList(RequestContext.getContext());
        if (TextUtils.isEmpty(cachedHostList)) {
            if (Log.isLoggable("Micloud", 3)) {
                Log.d("Micloud", "Hosts in SystemSettings/sp = null, return null");
            }
            return null;
        }
        try {
            updateHostCache(JsonUtils.jsonToMap(new JSONObject(cachedHostList)));
            Object obj2 = getHostCache().get(str);
            if (obj2 != null && (obj2 instanceof String)) {
                str2 = (String) obj2;
            }
            if (Log.isLoggable("Micloud", 3)) {
                Log.d("Micloud", "find host in SystemSettings/sp return host = " + str2);
            }
            return str2;
        } catch (JSONException e) {
            Log.e("Micloud", "JSONException in getHost, return null", e);
            return null;
        }
    }
}
