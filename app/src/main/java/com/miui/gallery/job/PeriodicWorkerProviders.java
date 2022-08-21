package com.miui.gallery.job;

import com.miui.gallery.job.workers.CardUpdateWorkerProvider;
import com.miui.gallery.job.workers.CloudControlWorkerProvider;
import com.miui.gallery.job.workers.DailyChargingWorkerProvider;
import com.miui.gallery.job.workers.DailyIdleWorkerProvider;
import com.miui.gallery.job.workers.GeoCollectWorkerProvider;
import com.miui.gallery.job.workers.LocalPeopleCoverWorkerProvider;
import com.miui.gallery.job.workers.RequestSyncWorkerProvider;
import com.miui.gallery.job.workers.ScanMediaWorkerProvider;
import com.miui.gallery.job.workers.SearchStatsWorkerProvider;
import com.miui.gallery.job.workers.SettingsSyncWorkerProvider;
import com.miui.gallery.job.workers.StatisticsWorkerProvider;
import com.miui.gallery.job.workers.WeeklyIdleWorkerProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class PeriodicWorkerProviders {
    public final Map<String, Class> mRegistry;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final PeriodicWorkerProviders INSTANCE = new PeriodicWorkerProviders();
    }

    public PeriodicWorkerProviders() {
        HashMap hashMap = new HashMap(12);
        this.mRegistry = hashMap;
        hashMap.put("SearchStatsWorkerProvider", SearchStatsWorkerProvider.class);
        hashMap.put("SettingsSyncWorkerProvider", SettingsSyncWorkerProvider.class);
        hashMap.put("GeoCollectWorkerProvider", GeoCollectWorkerProvider.class);
        hashMap.put("StatisticsWorkerProvider", StatisticsWorkerProvider.class);
        hashMap.put("RequestSyncWorkerProvider", RequestSyncWorkerProvider.class);
        hashMap.put("CloudControlWorkerProvider", CloudControlWorkerProvider.class);
        hashMap.put("CardUpdateWorkerProvider", CardUpdateWorkerProvider.class);
        hashMap.put("DailyIdleWorkerProvider", DailyIdleWorkerProvider.class);
        hashMap.put("LocalPeopleCoverWorkerProvider", LocalPeopleCoverWorkerProvider.class);
        hashMap.put("ScanMediaWorkerProvider", ScanMediaWorkerProvider.class);
        hashMap.put("DailyChargingWorkerProvider", DailyChargingWorkerProvider.class);
        hashMap.put("WeeklyIdleWorkerProvider", WeeklyIdleWorkerProvider.class);
    }

    public static PeriodicWorkerProviders getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Map<String, Class> getAll() {
        return Collections.unmodifiableMap(this.mRegistry);
    }
}
