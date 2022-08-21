package com.miui.gallery.util;

import android.text.TextUtils;
import ch.qos.logback.classic.spi.CallerData;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.model.PersistentResponse;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public class PersistentResponseHelper {
    public static void clearData() {
        GalleryEntityManager.getInstance().delete(PersistentResponse.class, "", null);
    }

    public static void cleanupInvalidRecords() {
        GalleryEntityManager.getInstance().delete(PersistentResponse.class, "lastVisitTime <= ? ", new String[]{String.valueOf(System.currentTimeMillis() - 2592000000L)});
    }

    public static void save(String str, Map<String, String> map, String str2, long j) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        String appendUrlParams = appendUrlParams(str, map);
        long currentTimeMillis = System.currentTimeMillis();
        GalleryEntityManager.getInstance().insertWithOnConflict(new PersistentResponse(appendUrlParams, str2, currentTimeMillis, currentTimeMillis, j), 5);
    }

    public static void saveAsync(final String str, final Map<String, String> map, final String str2, final long j) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.util.PersistentResponseHelper.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                PersistentResponseHelper.save(str, map, str2, j);
                return null;
            }
        });
    }

    public static PersistentResponse find(String str, Map<String, String> map) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String appendUrlParams = appendUrlParams(str, map);
        long currentTimeMillis = System.currentTimeMillis();
        PersistentResponse persistentResponse = (PersistentResponse) GalleryEntityManager.getInstance().find(PersistentResponse.class, "url = ?", new String[]{appendUrlParams});
        if (persistentResponse != null) {
            persistentResponse.setLastVisitTime(currentTimeMillis);
            GalleryEntityManager.getInstance().update(persistentResponse);
        }
        return persistentResponse;
    }

    public static String appendUrlParams(String str, Map<String, String> map) {
        if (str == null || map == null || map.isEmpty()) {
            return str;
        }
        TreeMap treeMap = new TreeMap();
        treeMap.putAll(map);
        StringBuilder sb = new StringBuilder(str);
        if (str.indexOf(63) > 0) {
            if (!str.endsWith(CallerData.NA) && !str.endsWith("&")) {
                sb.append("&");
            }
            sb.append(encodeParameters(treeMap, Keyczar.DEFAULT_ENCODING));
            return sb.toString();
        }
        sb.append(CallerData.NA);
        sb.append(encodeParameters(treeMap, Keyczar.DEFAULT_ENCODING));
        return sb.toString();
    }

    public static String encodeParameters(Map<String, String> map, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    sb.append(URLEncoder.encode(entry.getKey(), str));
                    sb.append('=');
                    sb.append(URLEncoder.encode(entry.getValue(), str));
                    sb.append('&');
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported: " + str, e);
        }
    }
}
