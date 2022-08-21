package com.xiaomi.micloudsdk.cloudinfo.utils;

import android.os.Build;
import android.text.TextUtils;
import com.xiaomi.stat.b.h;
import java.util.Locale;
import miui.cloud.os.SystemProperties;
import miui.cloud.sync.MiCloudStatusInfo;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class CloudInfoUtils {
    public static String sUserAgent;

    public static String getUserAgent() {
        if (sUserAgent == null) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            String str = Build.MODEL;
            sb2.append(str);
            sb2.append(h.g);
            sb.append(sb2.toString());
            String str2 = SystemProperties.get("ro.product.mod_device");
            if (!TextUtils.isEmpty(str2)) {
                sb.append(str2);
            } else {
                sb.append(str);
            }
            sb.append("; MIUI/");
            sb.append(Build.VERSION.INCREMENTAL);
            sb.append(" E/");
            sb.append(SystemProperties.get("ro.miui.ui.version.name"));
            sb.append(" B/");
            if (miui.os.Build.IS_ALPHA_BUILD) {
                sb.append("A");
            } else if (miui.os.Build.IS_DEVELOPMENT_VERSION) {
                sb.append("D");
            } else if (miui.os.Build.IS_STABLE_VERSION) {
                sb.append("S");
            } else {
                sb.append("null");
            }
            sb.append(" L/");
            Locale locale = Locale.getDefault();
            String language = locale.getLanguage();
            if (language != null) {
                sb.append(convertObsoleteLanguageCodeToNew(language));
                String country = locale.getCountry();
                if (country != null) {
                    sb.append("-");
                    sb.append(country.toUpperCase());
                }
            } else {
                sb.append("EN");
            }
            sb.append(" LO/");
            String region = miui.os.Build.getRegion();
            if (!TextUtils.isEmpty(region)) {
                sb.append(region.toUpperCase());
            } else {
                sb.append("null");
            }
            sUserAgent = sb.toString();
        }
        return sUserAgent;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0046 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static miui.cloud.sync.MiCloudStatusInfo getMiCloudStatusInfo(java.lang.String r3, org.json.JSONObject r4, java.lang.String r5) throws com.xiaomi.opensdk.exception.UnretriableException, com.xiaomi.opensdk.exception.RetriableException, com.xiaomi.opensdk.exception.AuthenticationException {
        /*
            java.lang.String r0 = getUserAgent()
            android.util.ArrayMap r1 = new android.util.ArrayMap
            r1.<init>()
            java.lang.String r2 = "version"
            r1.put(r2, r0)
            java.lang.String r0 = "_locale"
            r1.put(r0, r5)
            java.lang.String r5 = "CloudInfoUtils"
            if (r4 == 0) goto L25
            java.lang.String r4 = r4.toString()
            java.lang.String r0 = "query status with status."
            android.util.Log.d(r5, r0)
            java.lang.String r0 = "clientStatus"
            r1.put(r0, r4)
        L25:
            java.lang.String r4 = com.xiaomi.micloudsdk.utils.MiCloudConstants.URL.URL_MICLOUD_STATUS_QUERY
            com.xiaomi.micloudsdk.request.utils.HttpUtils$HttpMethod r0 = com.xiaomi.micloudsdk.request.utils.HttpUtils.HttpMethod.POST
            r2 = 0
            org.json.JSONObject r4 = com.xiaomi.micloudsdk.request.utils.Request.requestServer(r4, r0, r1, r2)
            java.lang.String r0 = "code"
            int r0 = r4.getInt(r0)     // Catch: org.json.JSONException -> L3d
            if (r0 != 0) goto L43
            java.lang.String r0 = "data"
            org.json.JSONObject r4 = r4.getJSONObject(r0)     // Catch: org.json.JSONException -> L3d
            goto L44
        L3d:
            r4 = move-exception
            java.lang.String r0 = "JSONException in getMiCloudStatusInfo"
            android.util.Log.e(r5, r0, r4)
        L43:
            r4 = r2
        L44:
            if (r4 != 0) goto L47
            return r2
        L47:
            java.util.Map r4 = com.xiaomi.micloudsdk.utils.JsonUtils.jsonToMap(r4)
            miui.cloud.sync.MiCloudStatusInfo r5 = new miui.cloud.sync.MiCloudStatusInfo
            r5.<init>(r3)
            r5.parseMap(r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.micloudsdk.cloudinfo.utils.CloudInfoUtils.getMiCloudStatusInfo(java.lang.String, org.json.JSONObject, java.lang.String):miui.cloud.sync.MiCloudStatusInfo");
    }

    public static MiCloudStatusInfo.QuotaInfo getQuotaInfo(MiCloudStatusInfo miCloudStatusInfo, JSONObject jSONObject) {
        long optLong = jSONObject.optLong("Total");
        long optLong2 = jSONObject.optLong("Used");
        String optString = jSONObject.optString("Warn");
        String optString2 = jSONObject.optString("YearlyPackageType");
        long optLong3 = jSONObject.optLong("YearlyPackageSize");
        long optLong4 = jSONObject.optLong("YearlyPackageCreateTime");
        long optLong5 = jSONObject.optLong("YearlyPackageExpireTime");
        miCloudStatusInfo.getClass();
        MiCloudStatusInfo.QuotaInfo quotaInfo = new MiCloudStatusInfo.QuotaInfo(optLong, optLong2, optString, optString2, optLong3, optLong4, optLong5);
        JSONArray optJSONArray = jSONObject.optJSONArray("ItemInfoList");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                MiCloudStatusInfo.ItemInfo itemInfo = getItemInfo(miCloudStatusInfo, optJSONArray.optJSONObject(i));
                if (itemInfo != null) {
                    quotaInfo.addItemInfo(itemInfo);
                }
            }
        }
        return quotaInfo;
    }

    public static MiCloudStatusInfo.ItemInfo getItemInfo(MiCloudStatusInfo miCloudStatusInfo, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        String optString = jSONObject.optString("Name");
        String optString2 = jSONObject.optString("LocalizedName");
        long optLong = jSONObject.optLong("Used");
        miCloudStatusInfo.getClass();
        return new MiCloudStatusInfo.ItemInfo(optString, optString2, optLong);
    }

    public static String convertObsoleteLanguageCodeToNew(String str) {
        if (str == null) {
            return null;
        }
        return "iw".equals(str) ? "he" : "in".equals(str) ? "id" : "ji".equals(str) ? "yi" : str;
    }
}
