package com.miui.gallery.cloud.base;

import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.NetworkTestUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.stat.MiStat;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import javax.net.ssl.SSLException;
import org.apache.http.conn.ConnectTimeoutException;

/* loaded from: classes.dex */
public class RetryRequestHelper {
    public static <T> GallerySyncResult<T> retryTask(SyncTask<T> syncTask) {
        GallerySyncCode gallerySyncCode;
        GallerySyncResult<T> gallerySyncResult = null;
        int i = 0;
        while (i < 3) {
            try {
                try {
                    gallerySyncResult = syncTask.run();
                    gallerySyncCode = gallerySyncResult.code;
                } catch (Exception e) {
                    DefaultLogger.w("RetryRequestHelper", e);
                    if (e instanceof UnknownHostException) {
                        testDNS(e.getMessage());
                    }
                    gallerySyncResult = new GallerySyncResult.Builder().setCode(GallerySyncCode.UNKNOWN).setException(e).build();
                    if (!isRetriableException(e)) {
                        break;
                    }
                }
                if (gallerySyncCode != GallerySyncCode.OK && isRetriableErrorCode(gallerySyncCode) && i < 2) {
                    if (gallerySyncResult.retryAfter > 0) {
                        DefaultLogger.w("RetryRequestHelper", "%s retry after %s", syncTask.getIdentifier(), Long.valueOf(gallerySyncResult.retryAfter));
                        ThreadManager.sleepThread(Math.min(gallerySyncResult.retryAfter, 30L) * 1000);
                    } else {
                        DefaultLogger.d("RetryRequestHelper", "%s return %s, retry", syncTask.getIdentifier(), gallerySyncResult.code);
                    }
                    i++;
                } else {
                    statResult(syncTask.getIdentifier(), gallerySyncResult, i);
                    return gallerySyncResult;
                }
            } finally {
                if (gallerySyncResult == null) {
                    gallerySyncResult = new GallerySyncResult.Builder().setCode(GallerySyncCode.UNKNOWN).build();
                }
                statResult(syncTask.getIdentifier(), gallerySyncResult, i);
            }
        }
        return gallerySyncResult;
    }

    public static void statResult(String str, GallerySyncResult gallerySyncResult, int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("request", str);
        hashMap.put("name", gallerySyncResult.code.name());
        Exception exc = gallerySyncResult.exception;
        if (exc != null) {
            hashMap.put("error", exc.getMessage());
        }
        hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
        SamplingStatHelper.recordCountEvent("Sync", "sync_request_result", hashMap);
    }

    public static void testDNS(String str) {
        if (Rom.IS_INTERNATIONAL || Rom.IS_STABLE || !BaseGalleryPreferences.CTA.canConnectNetwork()) {
            return;
        }
        boolean testDNS = NetworkTestUtils.testDNS(str);
        DefaultLogger.d("RetryRequestHelper", "test DNS result: %s", Boolean.valueOf(testDNS));
        HashMap hashMap = new HashMap();
        hashMap.put("result", String.valueOf(testDNS));
        hashMap.put(CallMethod.ARG_EXTRA_STRING, str);
        SamplingStatHelper.recordCountEvent("Sync", "sync_dns_test_result", hashMap);
    }

    public static boolean isRetriableException(Exception exc) {
        return (exc instanceof ConnectException) || (exc instanceof ConnectTimeoutException) || (exc instanceof SocketException) || (exc instanceof SocketTimeoutException) || (exc instanceof UnknownHostException) || (exc instanceof SSLException) || (exc instanceof RetriableException);
    }

    /* renamed from: com.miui.gallery.cloud.base.RetryRequestHelper$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$cloud$base$GallerySyncCode;

        static {
            int[] iArr = new int[GallerySyncCode.values().length];
            $SwitchMap$com$miui$gallery$cloud$base$GallerySyncCode = iArr;
            try {
                iArr[GallerySyncCode.RETRY_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$base$GallerySyncCode[GallerySyncCode.NEED_RE_REQUEST.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$base$GallerySyncCode[GallerySyncCode.TIMEOUT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static boolean isRetriableErrorCode(GallerySyncCode gallerySyncCode) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$cloud$base$GallerySyncCode[gallerySyncCode.ordinal()];
        return i == 1 || i == 2 || i == 3;
    }
}
