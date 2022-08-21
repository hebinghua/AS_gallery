package com.miui.gallery.job;

import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.ranges.IntRange;

/* compiled from: JobSchedulerConst.kt */
/* loaded from: classes2.dex */
public final class JobSchedulerConst {
    public static final List<Integer> DEPRECATED_JOB_IDS;
    public static final JobSchedulerConst INSTANCE = new JobSchedulerConst();
    public static final int SPACE_SLIM_JOB = 1;
    public static final int SLIM_NOTIFICATION_JOB = 2;
    public static final int RECENT_CLEANUP_JOB = 3;
    public static final int COLLECT_LOCATION_JOB = 4;
    public static final int CLOUD_CONTROL_JOB = 5;
    public static final int STATISTICS_JOB = 6;
    public static final int SEARCH_STAT_JOB = 7;
    public static final int REQUEST_SYNC_JOB = 8;
    public static final int CARD_OPERATION_JOB = 9;
    public static final int DAILY_CHECK_JOB = 10;
    public static final int FILE_HANDLE_SERVICE_CHECK_JOB = 11;
    public static final int PERSISTENT_RESPONSE_CLEANUP_JOB = 12;
    public static final int PEOPLE_COVER_JOB = 13;
    public static final int DELETE_RECORD_CLEAN_JOB = 15;
    public static final int CARD_SCENARIO_JOB = 16;
    public static final int CARD_COVER_UPDATE_JOB = 17;
    public static final int TRASH_BIN_CLEAN_JOB = 18;
    public static final int TRASH_BIN_CLEAN_VLOG_TRANS_CODE_JOB = 19;
    public static final int PUSH_SETTING_DOWNLOAD_JOB = 20;
    public static final int DELETE_SCRAP_SCREENSHOT_JOB = 21;
    public static final int TRASH_REQUEST_JOB_PURGE = 997;
    public static final int TRASH_REQUEST_JOB_RECOVERY = 998;
    public static final int CLEAN_JOB = 999;
    public static final int JOB_ID_BACK_DOWNLOAD = 10000;

    public final List<Integer> getDEPRECATED_JOB_IDS() {
        return DEPRECATED_JOB_IDS;
    }

    static {
        ArrayList arrayList = new ArrayList();
        DEPRECATED_JOB_IDS = arrayList;
        CollectionsKt__MutableCollectionsKt.addAll(arrayList, new IntRange(0, 21));
        arrayList.add(997);
        arrayList.add(998);
        arrayList.add(999);
        arrayList.add(10000);
    }

    public final int getTRASH_REQUEST_JOB_PURGE() {
        return TRASH_REQUEST_JOB_PURGE;
    }

    public final int getTRASH_REQUEST_JOB_RECOVERY() {
        return TRASH_REQUEST_JOB_RECOVERY;
    }
}
