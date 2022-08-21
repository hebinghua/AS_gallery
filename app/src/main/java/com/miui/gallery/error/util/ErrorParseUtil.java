package com.miui.gallery.error.util;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryMiCloudServerException;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.security.net.NetworkRestrict;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

/* loaded from: classes2.dex */
public class ErrorParseUtil {
    public static ErrorCode parseError(Throwable th, String str) {
        if (th instanceof ConnectTimeoutException) {
            return ErrorCode.CONNECT_TIMEOUT;
        }
        if (th instanceof SocketTimeoutException) {
            return ErrorCode.SOCKET_TIMEOUT;
        }
        if (isHostConnectException(th)) {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            if (BaseNetworkUtils.isActiveNetworkMetered()) {
                if (NetworkRestrict.isMobileRestrict(sGetAndroidContext, "com.miui.gallery")) {
                    DefaultLogger.d("ErrorParseUtil", "mobile net is restrict");
                    return ErrorCode.NETWORK_RESTRICT;
                }
            } else if (NetworkRestrict.isWifiRestrict(sGetAndroidContext, "com.miui.gallery")) {
                DefaultLogger.d("ErrorParseUtil", "wifi is restrict");
                return ErrorCode.NETWORK_RESTRICT;
            }
        }
        if (th instanceof IOException) {
            String message = th.getCause() != null ? th.getCause().getMessage() : th.getMessage();
            if (!TextUtils.isEmpty(message) && (message.contains("EROFS") || message.contains("Read-only file system") || message.contains("EPERM") || message.contains("ENOTCONN") || message.contains("EBUSY") || message.contains("ENOSPC") || message.contains("Permission denied"))) {
                String secondaryStoragePath = StorageUtils.getSecondaryStoragePath();
                if (!TextUtils.isEmpty(secondaryStoragePath) && message.contains(secondaryStoragePath)) {
                    return ErrorCode.SECONDARY_STORAGE_WRITE_ERROR;
                }
                return ErrorCode.PRIMARY_STORAGE_WRITE_ERROR;
            }
        }
        if (th instanceof FileNotFoundException) {
            if ((TextUtils.isEmpty(th.getMessage()) ? th.toString() : th.getMessage()).contains("No space left on device")) {
                return ErrorCode.STORAGE_FULL;
            }
        }
        if (th instanceof GalleryMiCloudServerException) {
            Exception cloudServerException = ((GalleryMiCloudServerException) th).getCloudServerException();
            if ((cloudServerException instanceof CloudServerException) && ((CloudServerException) cloudServerException).is5xxServerException()) {
                return ErrorCode.OVER_QUOTA;
            }
            return ErrorCode.SERVER_ERROR;
        } else if (th instanceof RetriableException) {
            return ErrorCode.RETRIABLE_ERROR;
        } else {
            if (th instanceof UnretriableException) {
                String th2 = TextUtils.isEmpty(th.getMessage()) ? th.toString() : th.getMessage();
                if (!TextUtils.isEmpty(th2) && th2.contains("Local IO error")) {
                    try {
                        testWriteStorage(str);
                    } catch (IOException e) {
                        return parseError(e, null);
                    }
                }
            }
            return ErrorCode.UNKNOWN;
        }
    }

    public static void testWriteStorage(String str) throws IOException {
        FileOutputStream fileOutputStream;
        Throwable th;
        String volumePath = StorageUtils.getVolumePath(GalleryApp.sGetAndroidContext(), str);
        if (TextUtils.isEmpty(volumePath)) {
            volumePath = StorageUtils.getPriorStoragePath();
        }
        File file = new File(volumePath, ".test");
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (Throwable th2) {
            fileOutputStream = null;
            th = th2;
        }
        try {
            fileOutputStream.write("test".getBytes());
            file.delete();
            BaseMiscUtil.closeSilently(fileOutputStream);
        } catch (Throwable th3) {
            th = th3;
            file.delete();
            BaseMiscUtil.closeSilently(fileOutputStream);
            throw th;
        }
    }

    public static boolean isHostConnectException(Throwable th) {
        if (!(th instanceof HttpHostConnectException) && !(th instanceof UnknownHostException)) {
            String th2 = th.toString();
            return !TextUtils.isEmpty(th2) && th2.contains("HttpHostConnectException");
        }
        return true;
    }
}
