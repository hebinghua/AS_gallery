package com.miui.gallery.cloud;

import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.util.Encode;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.stat.DownloadFileFailedStatParam;
import com.xiaomi.micloudsdk.stat.GetDownloadFileUrlFailedStatParam;
import com.xiaomi.micloudsdk.stat.IMiCloudDownloadFileNetEventStatCallback;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class MiCloudDownloadFileNetEventStatCallback implements IMiCloudDownloadFileNetEventStatCallback {
    public static final Pattern sRequestOriginUrlPattern = Pattern.compile("(.*)/user/full/(.*)/storage(.*)");
    public static final Pattern sRequestThumbnailUrlPattern = Pattern.compile("(.*)/user/thumbnails(.*)");

    @Override // com.xiaomi.micloudsdk.stat.IMiCloudDownloadFileNetEventStatCallback
    public boolean isGetDownloadFileRequestUrl(String str, String str2) {
        boolean matches;
        if (TextUtils.isEmpty(str)) {
            matches = false;
        } else if (sRequestOriginUrlPattern.matcher(str).matches()) {
            matches = "GET".equalsIgnoreCase(str2);
        } else {
            matches = sRequestThumbnailUrlPattern.matcher(str).matches();
        }
        DefaultLogger.d("MiCloudDownloadFileNetEventStatCallback", "is get download file request url: %s", Boolean.valueOf(matches));
        return matches;
    }

    @Override // com.xiaomi.micloudsdk.stat.IMiCloudDownloadFileNetEventStatCallback
    public void onAddGetDownloadFileUrlsFailedEvent(GetDownloadFileUrlFailedStatParam getDownloadFileUrlFailedStatParam) {
        String str;
        boolean z;
        if (getDownloadFileUrlFailedStatParam == null) {
            return;
        }
        String str2 = getDownloadFileUrlFailedStatParam.url;
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        if (sRequestOriginUrlPattern.matcher(str2).matches()) {
            str = "403.60.2.1.14930";
            z = true;
        } else if (!sRequestThumbnailUrlPattern.matcher(str2).matches()) {
            return;
        } else {
            str = "403.60.1.1.14913";
            z = false;
        }
        String ipAddress = getIpAddress(str2);
        HashMap hashMap = new HashMap();
        hashMap.put("tip", str);
        hashMap.put("incode", Integer.valueOf(getDownloadFileUrlFailedStatParam.responseCode));
        hashMap.put("outcode", Integer.valueOf(getDownloadFileUrlFailedStatParam.httpStatusCode));
        hashMap.put("description", getDownloadFileUrlFailedStatParam.description);
        hashMap.put("reason", getDownloadFileUrlFailedStatParam.reason);
        hashMap.put("severip", ipAddress);
        hashMap.put("errortype", getDownloadFileUrlFailedStatParam.exceptionName);
        TrackController.trackError(hashMap);
        DefaultLogger.fd("MiCloudDownloadFileNetEventStatCallback", "get download url fail, is origin: %s, in code: %s, out code: %s, description: %s, reason : %s, address: %s, exception : %s ", Boolean.valueOf(z), Integer.valueOf(getDownloadFileUrlFailedStatParam.responseCode), Integer.valueOf(getDownloadFileUrlFailedStatParam.httpStatusCode), getDownloadFileUrlFailedStatParam.description, getDownloadFileUrlFailedStatParam.reason, encodeIp(ipAddress), getDownloadFileUrlFailedStatParam.exceptionName);
    }

    @Override // com.xiaomi.micloudsdk.stat.IMiCloudDownloadFileNetEventStatCallback
    public void onAddDownloadFileFailedEvent(DownloadFileFailedStatParam downloadFileFailedStatParam) {
        String str;
        String str2;
        String str3 = "";
        if (downloadFileFailedStatParam == null) {
            return;
        }
        String str4 = downloadFileFailedStatParam.url;
        if (TextUtils.isEmpty(str4)) {
            return;
        }
        String ipAddress = getIpAddress(str4);
        Uri parse = Uri.parse(str4);
        String host = parse.getHost();
        if (TextUtils.isEmpty(host) || !host.endsWith(".micloud.mi.com")) {
            return;
        }
        try {
            str = parse.getQueryParameter("sig");
            try {
                str2 = parse.getQueryParameter("block");
                try {
                    str3 = parse.getQueryParameter("ck");
                } catch (Exception e) {
                    e = e;
                    DefaultLogger.e("MiCloudDownloadFileNetEventStatCallback", "get download fail param fail: ", e);
                    String str5 = "{sig='" + str + CoreConstants.SINGLE_QUOTE_CHAR + ", block='" + str2 + CoreConstants.SINGLE_QUOTE_CHAR + ", ck='" + str3 + CoreConstants.SINGLE_QUOTE_CHAR + '}';
                    HashMap hashMap = new HashMap();
                    hashMap.put("tip", "403.60.2.1.14931");
                    hashMap.put("severip", ipAddress);
                    hashMap.put("httpcode", Integer.valueOf(downloadFileFailedStatParam.httpStatusCode));
                    hashMap.put("host", host);
                    hashMap.put("error", str5);
                    hashMap.put("errortype", downloadFileFailedStatParam.exceptionName);
                    TrackController.trackError(hashMap);
                    DefaultLogger.fd("MiCloudDownloadFileNetEventStatCallback", "download origin fail, address: %s, http code: %s, host: %s, error: %s, exception : %s ", encodeIp(ipAddress), Integer.valueOf(downloadFileFailedStatParam.httpStatusCode), host, str5, downloadFileFailedStatParam.exceptionName);
                }
            } catch (Exception e2) {
                e = e2;
                str2 = str3;
            }
        } catch (Exception e3) {
            e = e3;
            str = str3;
            str2 = str;
        }
        String str52 = "{sig='" + str + CoreConstants.SINGLE_QUOTE_CHAR + ", block='" + str2 + CoreConstants.SINGLE_QUOTE_CHAR + ", ck='" + str3 + CoreConstants.SINGLE_QUOTE_CHAR + '}';
        HashMap hashMap2 = new HashMap();
        hashMap2.put("tip", "403.60.2.1.14931");
        hashMap2.put("severip", ipAddress);
        hashMap2.put("httpcode", Integer.valueOf(downloadFileFailedStatParam.httpStatusCode));
        hashMap2.put("host", host);
        hashMap2.put("error", str52);
        hashMap2.put("errortype", downloadFileFailedStatParam.exceptionName);
        TrackController.trackError(hashMap2);
        DefaultLogger.fd("MiCloudDownloadFileNetEventStatCallback", "download origin fail, address: %s, http code: %s, host: %s, error: %s, exception : %s ", encodeIp(ipAddress), Integer.valueOf(downloadFileFailedStatParam.httpStatusCode), host, str52, downloadFileFailedStatParam.exceptionName);
    }

    public static void trackThumbnailDownloadFailed(String str, int i, String str2, int i2, long j) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String ipAddress = getIpAddress(str);
        Uri parse = Uri.parse(str);
        String host = parse.getHost();
        String str3 = "{sig='" + parse.getQueryParameter("sig") + CoreConstants.SINGLE_QUOTE_CHAR + ", data='" + parse.getQueryParameter("data") + CoreConstants.SINGLE_QUOTE_CHAR + ", timeOut='" + i2 + CoreConstants.SINGLE_QUOTE_CHAR + ", cost='" + j + CoreConstants.SINGLE_QUOTE_CHAR + '}';
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.60.1.1.14929");
        hashMap.put("severip", ipAddress);
        hashMap.put("httpcode", Integer.valueOf(i));
        hashMap.put("host", host);
        hashMap.put("error", str3);
        hashMap.put("errortype", str2);
        TrackController.trackError(hashMap);
        DefaultLogger.fd("MiCloudDownloadFileNetEventStatCallback", "download thumb fail, address: %s, http code: %s, host: %s, error: %s, exception : %s ", encodeIp(ipAddress), Integer.valueOf(i), host, str3, str2);
    }

    public static String getIpAddress(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            return InetAddress.getByName(Uri.parse(str).getHost()).getHostAddress();
        } catch (SecurityException e) {
            DefaultLogger.e("MiCloudDownloadFileNetEventStatCallback", "get ipAddress fail: ", e);
            return "";
        } catch (UnknownHostException e2) {
            DefaultLogger.e("MiCloudDownloadFileNetEventStatCallback", "get ipAddress fail: ", e2);
            return "";
        }
    }

    public static String encodeIp(String str) {
        return TextUtils.isEmpty(str) ? "" : Encode.encodeBase64(str.replace(".", ""));
    }
}
