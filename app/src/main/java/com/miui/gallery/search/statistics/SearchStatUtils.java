package com.miui.gallery.search.statistics;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import com.google.gson.Gson;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.os.Rom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import miuix.os.Build;

/* loaded from: classes2.dex */
public class SearchStatUtils {
    public static boolean sHasRegisteredLogPrefListener = false;
    public static ContentObserver sSettingObserver = new ContentObserver(ThreadManager.getMainHandler()) { // from class: com.miui.gallery.search.statistics.SearchStatUtils.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            SearchStatUtils.ENABLED.reset();
        }
    };
    public static final LazyValue<Context, Boolean> ENABLED = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.search.statistics.SearchStatUtils.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            boolean z = true;
            if (!SearchStatUtils.sHasRegisteredLogPrefListener) {
                context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("upload_log_pref"), false, SearchStatUtils.sSettingObserver);
                boolean unused = SearchStatUtils.sHasRegisteredLogPrefListener = true;
            }
            if (Build.IS_INTERNATIONAL_BUILD || !BaseMiscUtil.isCheckedImprovementProgram(GalleryApp.sGetAndroidContext())) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
    };
    public static volatile Stack<Pair<String, String>> sSerialStack = null;
    public static final Object sSerialLock = new Object();
    public static SearchStatLogItem sCachedLog = null;
    public static final Object sCachedLogLock = new Object();
    public static final double POW = Math.pow(10.0d, 8.0d);
    public static Random sSerialRandom = null;
    public static Gson sGson = new Gson();

    public static Bundle buildSearchEventExtras(Bundle bundle, String[] strArr, String[] strArr2) {
        if (strArr != null && strArr2 != null && strArr.length == strArr2.length) {
            if (bundle == null || bundle == Bundle.EMPTY) {
                bundle = new Bundle();
            }
            bundle.putStringArray("search_event_keys", strArr);
            bundle.putStringArray("search_event_values", strArr2);
        }
        return bundle;
    }

    public static Map<String, String> buildSearchEventParams(Bundle bundle) {
        HashMap hashMap = null;
        if (bundle == null) {
            return null;
        }
        String[] stringArray = bundle.getStringArray("search_event_keys");
        String[] stringArray2 = bundle.getStringArray("search_event_values");
        if (stringArray != null && stringArray2 != null && stringArray.length == stringArray2.length) {
            hashMap = new HashMap();
            for (int i = 0; i < stringArray.length; i++) {
                try {
                    hashMap.put(stringArray[i], stringArray2[i]);
                } catch (Exception e) {
                    SearchLog.e("SearchStatUtils", "Error occurred while build search event extras %s", e);
                }
            }
        }
        return hashMap;
    }

    public static String createNewSerial(String str) {
        if (sSerialStack == null) {
            synchronized (sSerialLock) {
                if (sSerialStack == null) {
                    sSerialStack = new Stack<>();
                }
            }
        }
        String createNewSerialId = createNewSerialId();
        synchronized (sSerialLock) {
            sSerialStack.push(new Pair<>(str, createNewSerialId));
        }
        return createNewSerialId;
    }

    public static String onCompleteSerial(String str) {
        Pair<String, String> serialStackHead;
        Object obj;
        if (sSerialStack == null) {
            return null;
        }
        synchronized (sSerialLock) {
            serialStackHead = getSerialStackHead();
            if (serialStackHead != null && (obj = serialStackHead.first) != null && ((String) obj).contentEquals(str)) {
                sSerialStack.pop();
            }
        }
        if (serialStackHead != null) {
            return (String) serialStackHead.second;
        }
        SearchLog.e("SearchStatUtils", "Current serial stack empty or head is not from %s, stack size %d!", str, Integer.valueOf(sSerialStack.size()));
        return null;
    }

    public static String getCurrentSerial() {
        Pair<String, String> serialStackHead = getSerialStackHead();
        if (serialStackHead != null) {
            return (String) serialStackHead.second;
        }
        return null;
    }

    public static Pair<String, String> getSerialStackHead() {
        synchronized (sSerialLock) {
            if (sSerialStack == null || sSerialStack.empty()) {
                return null;
            }
            return sSerialStack.peek();
        }
    }

    public static void cacheEvent(String str, String str2, Map<String, String> map) {
        SearchStatLogItem searchStatLogItem;
        SearchStatLogItem formLogItem = formLogItem(str, str2, map);
        synchronized (sCachedLogLock) {
            searchStatLogItem = sCachedLog;
            if (searchStatLogItem == null) {
                searchStatLogItem = null;
            }
            sCachedLog = formLogItem;
        }
        if (searchStatLogItem != null) {
            reportEvent(searchStatLogItem);
        }
    }

    public static String pullCachedEvent() {
        synchronized (sCachedLogLock) {
            SearchStatLogItem searchStatLogItem = sCachedLog;
            if (searchStatLogItem == null) {
                return null;
            }
            sCachedLog = null;
            if (searchStatLogItem != null) {
                return getDataJson(searchStatLogItem);
            }
            return null;
        }
    }

    public static void reportEvent(String str, String str2) {
        reportEvent(str, str2, null);
    }

    public static void reportEvent(String str, String str2, String str3, String str4) {
        reportEvent(str, str2, str3, str4, null, null);
    }

    public static void reportEvent(String str, String str2, String str3, String str4, String str5, String str6) {
        HashMap hashMap = new HashMap();
        if (str3 != null && str4 != null) {
            hashMap.put(str3, str4);
        }
        if (str5 != null && str6 != null) {
            hashMap.put(str5, str6);
        }
        reportEvent(str, str2, hashMap);
    }

    public static void reportEvent(String str, String str2, Map<String, String> map) {
        reportEvent(formLogItem(str, str2, map));
    }

    public static void reportEvent(SearchStatLogItem searchStatLogItem) {
        if (ENABLED.get(GalleryApp.sGetAndroidContext()).booleanValue()) {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            Intent intent = new Intent(sGetAndroidContext, SearchStatReportService.class);
            intent.putExtra(SearchStatReportService.EXTRA_LOG_ITEM, searchStatLogItem);
            intent.putExtra(SearchStatReportService.EXTRA_UPLOAD, true);
            BackgroundServiceHelper.startForegroundServiceIfNeed(sGetAndroidContext, intent);
            return;
        }
        SearchLog.d("SearchStatUtils", "On log [%s]", searchStatLogItem);
    }

    public static SearchStatLogItem formLogItem(String str, String str2, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        map.put("isInternational", String.valueOf(Rom.IS_INTERNATIONAL));
        if (str != null) {
            map.put("from", str);
        }
        Pair<String, String> serialStackHead = getSerialStackHead();
        return new SearchStatLogItem(serialStackHead == null ? null : (String) serialStackHead.second, str2, map);
    }

    public static String createNewSerialId() {
        if (sSerialRandom == null) {
            sSerialRandom = new Random();
        }
        return String.valueOf((int) ((sSerialRandom.nextDouble() + 1.0d) * POW)).substring(1);
    }

    public static String getDataJson(List<SearchStatLogItem> list) {
        HashMap hashMap = new HashMap();
        hashMap.put("items", list);
        return sGson.toJson(hashMap);
    }

    public static String getDataJson(SearchStatLogItem searchStatLogItem) {
        return sGson.toJson(searchStatLogItem);
    }

    public static void trackEnterImagePage(Bundle bundle) {
        String[] stringArray;
        String str;
        if (bundle == null || (stringArray = bundle.getStringArray("search_event_values")) == null || stringArray.length <= 0) {
            return;
        }
        if (stringArray.length >= 2 && "from_navigation".equals(stringArray[0])) {
            if ("people".equals(stringArray[1])) {
                str = "403.20.0.1.11267";
            } else {
                str = "location".equals(stringArray[1]) ? "403.20.0.1.11269" : "403.20.0.1.11271";
            }
            TrackController.trackClick(str, AutoTracking.getRef());
        } else if ("from_location_list".equals(stringArray[0])) {
            TrackController.trackClick("403.48.0.1.11263", AutoTracking.getRef());
        } else if (!"from_tag_list".equals(stringArray[0])) {
        } else {
            TrackController.trackClick("403.49.0.1.11264", AutoTracking.getRef());
        }
    }

    public static String extractSourceFromBundle(Bundle bundle) {
        if (bundle != null && !bundle.isEmpty()) {
            String[] stringArray = bundle.getStringArray("search_event_keys");
            String[] stringArray2 = bundle.getStringArray("search_event_values");
            if (stringArray != null && stringArray2 != null && stringArray.length == stringArray2.length) {
                for (int i = 0; i < stringArray.length; i++) {
                    if ("from".equals(stringArray[i])) {
                        return stringArray2[i];
                    }
                }
            }
        }
        return null;
    }
}
