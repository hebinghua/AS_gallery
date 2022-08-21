package com.xiaomi.micloudsdk.request.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.micloudsdk.data.IAuthToken;
import com.xiaomi.micloudsdk.exception.CipherException;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.request.utils.HttpUtils;
import com.xiaomi.micloudsdk.utils.CryptCoder;
import com.xiaomi.micloudsdk.utils.MiCloudConstants;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import miui.cloud.log.PrivacyFilter;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class Request {
    public static void init(Context context) {
        RequestContext.init(context);
    }

    public static Context getContext() {
        return RequestContext.getContext();
    }

    public static String secureGet(String str, Map<String, String> map) throws IllegalBlockSizeException, BadPaddingException, IOException, CloudServerException {
        return secureGet(str, map, null);
    }

    public static String secureGet(String str, Map<String, String> map, Map<String, String> map2) throws IllegalBlockSizeException, BadPaddingException, IOException, CloudServerException {
        return execute(str, HttpUtils.HttpMethod.GET, null, map, map2);
    }

    public static String securePost(String str, Map<String, String> map) throws IllegalBlockSizeException, BadPaddingException, IOException, CloudServerException {
        return securePost(str, map, null);
    }

    public static String securePost(String str, Map<String, String> map, Map<String, String> map2) throws IllegalBlockSizeException, BadPaddingException, IOException, CloudServerException {
        return execute(str, HttpUtils.HttpMethod.POST, null, map, map2);
    }

    public static JSONObject requestServer(String str, HttpUtils.HttpMethod httpMethod, Map<String, String> map, Map<String, String> map2) throws UnretriableException, RetriableException, AuthenticationException {
        return requestServer(str, httpMethod, null, map, map2);
    }

    public static JSONObject requestServer(String str, HttpUtils.HttpMethod httpMethod, Map<String, String> map, Map<String, String> map2, Map<String, String> map3) throws UnretriableException, RetriableException, AuthenticationException {
        try {
            return new JSONObject(execute(str, httpMethod, map, map2, map3));
        } catch (CloudServerException e) {
            Log.e("Request", "requestServer error:" + e.getStatusCode() + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e)));
            int statusCode = e.getStatusCode();
            if (statusCode == 401 || statusCode == 403) {
                throw new AuthenticationException();
            }
            if (statusCode == 500) {
                throw new RetriableException(e.getMessage(), 300000L);
            }
            if (statusCode == 503) {
                throw new RetriableException(e.getMessage(), e.retryTime);
            }
            throw new UnretriableException(e);
        } catch (UnsupportedEncodingException e2) {
            Log.e("Request", "requestServer error:" + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e2)));
            throw new UnretriableException(e2);
        } catch (IOException e3) {
            Log.e("Request", "requestServer error:" + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e3)));
            if (RetriableException.isRetriableException(e3)) {
                throw new RetriableException(e3.getMessage(), 300000L);
            }
            throw new UnretriableException(e3);
        } catch (BadPaddingException e4) {
            Log.e("Request", "requestServer error:" + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e4)));
            throw new UnretriableException(e4);
        } catch (IllegalBlockSizeException e5) {
            Log.e("Request", "requestServer error:" + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e5)));
            throw new UnretriableException(e5);
        } catch (JSONException e6) {
            Log.e("Request", "requestServer error:" + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e6)));
            throw new UnretriableException(e6);
        } catch (ClientProtocolException e7) {
            Log.e("Request", "requestServer error:" + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e7)));
            throw new UnretriableException((Throwable) e7);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:61:0x01c3, code lost:
        micloud.compat.independent.request.RequestInjectorCompat.checkResponse(com.xiaomi.micloudsdk.request.utils.RequestContext.getContext(), r7);
        micloud.compat.independent.request.NetworkAvailabilityManagerCompat.setAvailability(com.xiaomi.micloudsdk.request.utils.RequestContext.getContext(), true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01d2, code lost:
        return r7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String execute(java.lang.String r19, com.xiaomi.micloudsdk.request.utils.HttpUtils.HttpMethod r20, java.util.Map<java.lang.String, java.lang.String> r21, java.util.Map<java.lang.String, java.lang.String> r22, java.util.Map<java.lang.String, java.lang.String> r23) throws java.io.IOException, com.xiaomi.micloudsdk.exception.CloudServerException, javax.crypto.IllegalBlockSizeException, javax.crypto.BadPaddingException {
        /*
            Method dump skipped, instructions count: 477
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.micloudsdk.request.utils.Request.execute(java.lang.String, com.xiaomi.micloudsdk.request.utils.HttpUtils$HttpMethod, java.util.Map, java.util.Map, java.util.Map):java.lang.String");
    }

    public static String checkHostLocation(String str) throws CloudServerException {
        String str2 = MiCloudConstants.URL.URL_RELOCATION_BASE;
        if (!str.startsWith(str2)) {
            return CloudRelocationUtils.updateRequestHost(str, false);
        }
        Log.d("Request", "relocation url " + str2);
        return str;
    }

    public static IAuthToken getAuthTokenOrThrow() throws CloudServerException {
        IAuthToken mo2587queryAuthToken = RequestContext.getRequestEnv().mo2587queryAuthToken();
        if (mo2587queryAuthToken != null) {
            return mo2587queryAuthToken;
        }
        throw new CloudServerException(0, "queryAuthToken() returned null");
    }

    public static ArrayList<NameValuePair> encodeParameters(CryptCoder cryptCoder, String str, Map<String, String> map) throws CipherException {
        ArrayList<NameValuePair> arrayList;
        if (map == null) {
            map = new HashMap<>();
        }
        if (str != null && !map.containsKey("_nonce")) {
            arrayList = new ArrayList<>(map.size() + 1);
            arrayList.add(new BasicNameValuePair("_nonce", str));
        } else {
            arrayList = new ArrayList<>(map.size());
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.startsWith("_")) {
                if (value == null) {
                    value = "";
                }
                arrayList.add(new BasicNameValuePair(key, cryptCoder.encrypt(value)));
            } else {
                arrayList.add(new BasicNameValuePair(key, value));
            }
        }
        return arrayList;
    }

    public static Header getCookies(String str, String str2, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (str2 != null) {
            sb.append("serviceToken=");
            sb.append(str2);
        }
        if (str != null) {
            sb.append(sb.length() == 0 ? "cUserId=" : "; cUserId=");
            sb.append(str);
        }
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                if (!"cUserId".equals(key) && !"userId".equals(key) && !"serviceToken".equals(key) && !TextUtils.isEmpty(key)) {
                    sb.append(sb.length() == 0 ? "" : "; ");
                    sb.append(key);
                    sb.append("=");
                    sb.append(entry.getValue());
                }
            }
        }
        return new BasicHeader("Cookie", sb.toString());
    }

    public static void addApkVersionWithinParams(Map<String, String> map, Map<String, String> map2) {
        Context context = RequestContext.getContext();
        String packageName = context.getPackageName();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            String str = packageInfo.versionName;
            String valueOf = String.valueOf(packageInfo.versionCode);
            if (TextUtils.isEmpty(map.get("apk-version"))) {
                map.put("apk-version", str);
            }
            if (TextUtils.isEmpty(map.get("apk-version-code"))) {
                map.put("apk-version-code", valueOf);
            }
            if (TextUtils.isEmpty(map2.get("_apkversion"))) {
                map2.put("_apkversion", str);
            }
            if (!TextUtils.isEmpty(map2.get("_apkversioncode"))) {
                return;
            }
            map2.put("_apkversioncode", valueOf);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Request", packageName + " get apk version error —— Exception: " + e.getMessage());
        }
    }

    public static void addFilterTagsToParams(String str, Map<String, String> map, String str2, Set<String> set, long j) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            arrayList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int i = 0;
        for (String str3 : set) {
            if (Long.parseLong(str3) > j) {
                if (i < 100) {
                    if (sb2.length() > 0) {
                        sb2.append(",");
                    }
                    sb2.append(str3);
                    i++;
                    if (i % 10 != 0 && i != set.size()) {
                    }
                }
                BasicNameValuePair basicNameValuePair = new BasicNameValuePair("filterTag", CloudRequestHelper.encodeData(str, str2, sb.length() == 0 ? sb2.toString() : ((Object) sb) + "," + ((Object) sb2)));
                arrayList.add(basicNameValuePair);
                BasicNameValuePair basicNameValuePair2 = new BasicNameValuePair("signature", HttpUtils.getSignature(HttpUtils.HttpMethod.GET, str, arrayList, str2));
                arrayList.add(basicNameValuePair2);
                int length = HttpUtils.appendUrl(str, arrayList).length();
                arrayList.remove(basicNameValuePair);
                arrayList.remove(basicNameValuePair2);
                if (length >= 1024) {
                    break;
                }
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((CharSequence) sb2);
                sb2.setLength(0);
                if (i >= 100) {
                    break;
                }
            }
        }
        String sb3 = sb.toString();
        if (Log.isLoggable("Micloud", 3)) {
            Log.d("Micloud", "The filterTag is :" + sb3);
        }
        map.put("filterTag", sb3);
    }
}
