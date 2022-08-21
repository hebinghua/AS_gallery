package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Pair;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryOpenApiProvider;
import com.miui.gallery.provider.deprecated.GalleryCloudProvider;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.FileSizeFormatter;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.micloudsdk.cloudinfo.utils.CloudInfoUtils;
import java.util.HashMap;
import java.util.Locale;
import miui.cloud.sync.MiCloudStatusInfo;
import miui.cloud.util.SyncAutoSettingUtil;
import miuix.os.Build;

/* loaded from: classes.dex */
public class GalleryMiCloudUtil {
    public static final int[] STAGES = {0, 1, 5, 10, 30, 50, 100, 500, 1000};
    public static int MAX_SPACE_FULL_TIP_SHOW_COUNT = 7;
    public static long MIN_SPACE_FULL_TIP_SHOW_DURATION = 259200000;
    public static String SPACE_COMPLETELY_FULL_SOURCE = "Popup_Gallery_full";
    public static String SPACE_ALMOST_FULL_SOURCE = "Popup_Gallery_almostfull";
    public static double ALMOST_FULL_RATE = 0.9d;

    public static void sendMiCloudBroadcast(final Context context) {
        DefaultLogger.d("GalleryMiCloudUtil", "try to send space full broadcast to micloud");
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.cloud.GalleryMiCloudUtil.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                if (!SpaceFullHandler.isOwnerSpaceFull()) {
                    DefaultLogger.d("GalleryMiCloudUtil", "space not full");
                    return null;
                }
                Context context2 = context;
                if (context2 == null) {
                    DefaultLogger.d("GalleryMiCloudUtil", "context is null");
                    return null;
                }
                try {
                    if (SyncUtil.isGalleryCloudSyncable(context2) && BaseNetworkUtils.isNetworkConnected() && BaseGalleryPreferences.CTA.canConnectNetwork()) {
                        Account account = AccountCache.getAccount();
                        boolean z = true;
                        DefaultLogger.d("GalleryMiCloudUtil", "account is null : %s", Boolean.valueOf(account == null));
                        if (account != null) {
                            String str = account.name;
                            Locale localeFromContext = FileSizeFormatter.localeFromContext(context);
                            DefaultLogger.d("GalleryMiCloudUtil", "locale is null : %s", Boolean.valueOf(localeFromContext == null));
                            if (localeFromContext != null) {
                                MiCloudStatusInfo miCloudStatusInfo = CloudInfoUtils.getMiCloudStatusInfo(str, null, localeFromContext.toString());
                                DefaultLogger.d("GalleryMiCloudUtil", "statusInfo is null : %s", Boolean.valueOf(miCloudStatusInfo == null));
                                if (miCloudStatusInfo != null) {
                                    MiCloudStatusInfo.QuotaInfo quotaInfo = miCloudStatusInfo.getQuotaInfo();
                                    if (quotaInfo != null) {
                                        z = false;
                                    }
                                    DefaultLogger.d("GalleryMiCloudUtil", "quotaInfo is null : %s", Boolean.valueOf(z));
                                    if (quotaInfo != null && quotaInfo.isSpaceFull()) {
                                        Intent intent = new Intent("com.miui.cloudservice.CLOUD_SAPCE_FULL_CHECK");
                                        intent.putExtra("increase_count", GalleryOpenApiProvider.getIncreaseMediaInCurDay(context));
                                        intent.putExtra("unsynced_count", GalleryMiCloudUtil.getUnsyncedCount(context));
                                        intent.setPackage("com.miui.cloudservice");
                                        context.sendBroadcast(intent);
                                        DefaultLogger.d("GalleryMiCloudUtil", "send broadcast when cloud space full");
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    DefaultLogger.d("GalleryMiCloudUtil", (Throwable) e);
                }
                return null;
            }
        });
    }

    public static void handleSpaceFullOrNot(boolean z) {
        DefaultLogger.d("GalleryMiCloudUtil", "try to send back up status broadcast to micloud");
        try {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            if (!SyncUtil.isGalleryCloudSyncable(sGetAndroidContext) || !BaseNetworkUtils.isNetworkConnected() || !BaseGalleryPreferences.CTA.canConnectNetwork()) {
                return;
            }
            Account account = AccountCache.getAccount();
            DefaultLogger.d("GalleryMiCloudUtil", "account is null : %s", Boolean.valueOf(account == null));
            if (account == null) {
                return;
            }
            Intent intent = new Intent("com.miui.cloudservice.CLOUD_SAPCE_FULL_UPLOAD");
            intent.putExtra("is_space_full", z);
            intent.putExtra("unsynced_count", getUnsyncedCount(sGetAndroidContext));
            intent.putExtra("is_foreground", MiscUtil.isAppProcessInForeground());
            intent.setPackage("com.miui.cloudservice");
            sGetAndroidContext.sendBroadcast(intent);
            DefaultLogger.d("GalleryMiCloudUtil", "send broadcast when cloud space full");
        } catch (Exception e) {
            DefaultLogger.d("GalleryMiCloudUtil", (Throwable) e);
        }
    }

    public static void statUnSyncedCount() {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (!SyncUtil.isGalleryCloudSyncable(sGetAndroidContext) || !BaseGalleryPreferences.CTA.canConnectNetwork()) {
            return;
        }
        int generateStageCount = generateStageCount(getUnsyncedCount(sGetAndroidContext));
        SamplingStatHelper.recordStringPropertyEvent("unsynced_count", String.valueOf(generateStageCount));
        DefaultLogger.d("GalleryMiCloudUtil", "unsynced count %d", Integer.valueOf(generateStageCount));
    }

    public static int generateStageCount(int i) {
        for (int length = STAGES.length - 1; length >= 0; length--) {
            int[] iArr = STAGES;
            if (i >= iArr[length]) {
                return iArr[length];
            }
        }
        return i;
    }

    public static int getUnsyncedCount(Context context) {
        if (context == null) {
            return 0;
        }
        return ((Integer) SafeDBUtil.safeQuery(context, GalleryCloudProvider.SYNC_INFO_URI, new String[]{"syncableCount"}, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.cloud.GalleryMiCloudUtil.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Integer mo1808handle(Cursor cursor) {
                if (cursor != null && cursor.moveToFirst()) {
                    return Integer.valueOf(cursor.getInt(0));
                }
                return 0;
            }
        })).intValue();
    }

    public static boolean isSpaceFullCheckEnable(Context context) {
        if (!isAccountSpaceInfoCheckable(context)) {
            return false;
        }
        if (System.currentTimeMillis() - GalleryPreferences.MiCloud.getCloudSpaceFullTipLastCheckTime() > MIN_SPACE_FULL_TIP_SHOW_DURATION) {
            return true;
        }
        DefaultLogger.d("GalleryMiCloudUtil", "less than 3 days");
        return false;
    }

    public static boolean checkCompletelyFullShowCount() {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.74.0.1.18946");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "full");
        if (GalleryPreferences.MiCloud.getCloudSpaceCompletelyFullTipCounts() >= MAX_SPACE_FULL_TIP_SHOW_COUNT) {
            if (System.currentTimeMillis() - GalleryPreferences.MiCloud.getCloudSpaceCompletelyFullTipLastPopped() >= 7776000000L) {
                GalleryPreferences.MiCloud.setCloudSpaceCompletelyFullTipCounts(0);
                hashMap.put("status", "overtime");
            } else {
                DefaultLogger.d("GalleryMiCloudUtil", "completely full tip has show 7 more times");
                TrackController.trackStats(hashMap);
                return false;
            }
        }
        TrackController.trackStats(hashMap);
        return true;
    }

    public static boolean checkAlmostFullShowCount() {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.74.0.1.18946");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "almost_full");
        if (GalleryPreferences.MiCloud.getCloudSpaceAlmostFullTipCounts() >= MAX_SPACE_FULL_TIP_SHOW_COUNT) {
            if (System.currentTimeMillis() - GalleryPreferences.MiCloud.getCloudSpaceAlmostFullTipLastPopped() >= 15552000000L) {
                GalleryPreferences.MiCloud.setCloudSpaceAlmostFullTipCounts(0);
                hashMap.put("status", "overtime");
            } else {
                DefaultLogger.d("GalleryMiCloudUtil", "almost full tip has show 7 more times");
                TrackController.trackStats(hashMap);
                return false;
            }
        }
        TrackController.trackStats(hashMap);
        return true;
    }

    public static void clearSpaceFullTipsShowCount() {
        if (GalleryPreferences.MiCloud.getCloudSpaceAlmostFullTipCounts() <= 0) {
            return;
        }
        ThreadManager.getMiscPool().submit(new SpaceQueryJob(false), new SpaceChangeListener());
    }

    /* loaded from: classes.dex */
    public static class SpaceQueryJob implements ThreadPool.Job<SyncStateUtil.CloudSpaceInfo> {
        public boolean mCheckCondition;

        public SpaceQueryJob(boolean z) {
            this.mCheckCondition = z;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public SyncStateUtil.CloudSpaceInfo mo1807run(ThreadPool.JobContext jobContext) {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            if (!this.mCheckCondition || GalleryMiCloudUtil.isSpaceFullCheckEnable(sGetAndroidContext)) {
                if (this.mCheckCondition) {
                    GalleryPreferences.MiCloud.setCloudSpaceFullTipLastCheckTime(System.currentTimeMillis());
                }
                return SyncStateUtil.getCloudSpaceInfo(sGetAndroidContext);
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class FullSpaceTipDialogFragment extends AlertDialogFragment {
        public String mNegativeBtnText;
        public String mPositiveBtnText;
        public String mRate;
        public long mShowTimeStamp;
        public String mSource;

        public FullSpaceTipDialogFragment() {
        }

        public FullSpaceTipDialogFragment(Context context, String str) {
            if (str.equals("100.00")) {
                this.mSource = GalleryMiCloudUtil.SPACE_COMPLETELY_FULL_SOURCE;
                setTitle(context.getString(R.string.cloud_full_space_tip_title_completely));
            } else {
                this.mSource = GalleryMiCloudUtil.SPACE_ALMOST_FULL_SOURCE;
                setTitle(context.getString(R.string.cloud_full_space_tip_title));
            }
            this.mRate = str;
            setMessage(context.getString(R.string.cloud_full_space_tip_message));
            this.mPositiveBtnText = context.getString(R.string.cloud_full_space_tip_positive);
            this.mNegativeBtnText = context.getString(R.string.cloud_full_space_tip_negative);
            setCanceledOnTouchOutside(false);
        }

        public void trackClick(boolean z) {
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.74.0.1.18945");
            if (this.mRate.equals("100.00")) {
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "full");
                hashMap.put("value", Integer.valueOf(GalleryPreferences.MiCloud.getCloudSpaceCompletelyFullTipCounts()));
                if (!z) {
                    GalleryPreferences.MiCloud.setCloudSpaceCompletelyFullTipCounts(GalleryPreferences.MiCloud.getCloudSpaceCompletelyFullTipCounts() + 1);
                }
            } else {
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "almost_full");
                hashMap.put("value", Integer.valueOf(GalleryPreferences.MiCloud.getCloudSpaceAlmostFullTipCounts()));
                if (!z) {
                    GalleryPreferences.MiCloud.setCloudSpaceAlmostFullTipCounts(GalleryPreferences.MiCloud.getCloudSpaceAlmostFullTipCounts() + 1);
                }
            }
            hashMap.put("status", this.mRate);
            hashMap.put("duration", Long.valueOf(System.currentTimeMillis() - this.mShowTimeStamp));
            hashMap.put("success", Boolean.valueOf(z));
            TrackController.trackClick(hashMap);
        }

        public void trackExpose() {
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.74.1.1.18943");
            if (this.mRate.equals("100.00")) {
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "full");
                hashMap.put("value", Integer.valueOf(GalleryPreferences.MiCloud.getCloudSpaceCompletelyFullTipCounts()));
                GalleryPreferences.MiCloud.setCloudSpaceCompletelyFullTipLastPopped(System.currentTimeMillis());
            } else {
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "almost_full");
                hashMap.put("value", Integer.valueOf(GalleryPreferences.MiCloud.getCloudSpaceAlmostFullTipCounts()));
                GalleryPreferences.MiCloud.setCloudSpaceAlmostFullTipLastPopped(System.currentTimeMillis());
            }
            TrackController.trackExpose(hashMap);
        }

        public void show(FragmentManager fragmentManager) {
            if (fragmentManager == null || fragmentManager.isDestroyed()) {
                return;
            }
            setNegativeButton(this.mNegativeBtnText, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.cloud.GalleryMiCloudUtil.FullSpaceTipDialogFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    FullSpaceTipDialogFragment.this.trackClick(false);
                    SamplingStatHelper.recordCountEvent("Sync", "cloud_space_full_dialog_negative");
                }
            });
            setPositiveButton(this.mPositiveBtnText, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.cloud.GalleryMiCloudUtil.FullSpaceTipDialogFragment.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    FullSpaceTipDialogFragment.this.trackClick(true);
                    IntentUtil.gotoMiCloudVipPage(FullSpaceTipDialogFragment.this.getActivity(), new Pair("source", FullSpaceTipDialogFragment.this.mSource));
                    HashMap hashMap = new HashMap();
                    hashMap.put("from", FullSpaceTipDialogFragment.this.mSource);
                    SamplingStatHelper.recordCountEvent("Sync", "cloud_space_full_dialog_positive", hashMap);
                }
            });
            trackExpose();
            this.mShowTimeStamp = System.currentTimeMillis();
            showAllowingStateLoss(fragmentManager, "SpaceHandler");
        }

        @Override // com.miui.gallery.ui.AlertDialogFragment
        public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            super.setOnDismissListener(onDismissListener);
        }
    }

    public static String getRate(SyncStateUtil.CloudSpaceInfo cloudSpaceInfo) {
        if (cloudSpaceInfo == null) {
            return null;
        }
        long total = cloudSpaceInfo.getTotal();
        long used = cloudSpaceInfo.getUsed();
        int i = (total > 0L ? 1 : (total == 0L ? 0 : -1));
        if (i == 0 && used == 0) {
            return null;
        }
        if (used >= total) {
            SamplingStatHelper.recordCountEvent("Sync", "cloud_space_full");
            if (checkCompletelyFullShowCount()) {
                return "100.00";
            }
            return null;
        } else if (i > 0 && (used * 1.0d) / total >= ALMOST_FULL_RATE) {
            SamplingStatHelper.recordCountEvent("Sync", "cloud_space_almost_full");
            if (checkAlmostFullShowCount()) {
                return String.format("%.2f", Float.valueOf((((float) used) * 100.0f) / ((float) total)));
            }
            return null;
        } else {
            SamplingStatHelper.recordCountEvent("Sync", "cloud_space_not_full");
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.74.0.1.18946");
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "not_full");
            TrackController.trackStats(hashMap);
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class SpaceChangeListener implements FutureListener<SyncStateUtil.CloudSpaceInfo> {
        public SpaceChangeListener() {
        }

        @Override // com.miui.gallery.concurrent.FutureListener
        public void onFutureDone(Future<SyncStateUtil.CloudSpaceInfo> future) {
            if (future == null || future.get() == null) {
                return;
            }
            SyncStateUtil.CloudSpaceInfo cloudSpaceInfo = future.get();
            long total = cloudSpaceInfo.getTotal();
            long used = cloudSpaceInfo.getUsed();
            if (total <= 0 || (used * 1.0d) / total >= GalleryMiCloudUtil.ALMOST_FULL_RATE) {
                return;
            }
            DefaultLogger.d("GalleryMiCloudUtil", "clear space full count");
            SamplingStatHelper.recordCountEvent("Sync", "cloud_space_not_full");
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.74.0.1.18946");
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "not_full");
            hashMap.put("status", "pay");
            TrackController.trackStats(hashMap);
            SamplingStatHelper.recordCountEvent("Sync", "cloud_space_full_show_count_clear");
            GalleryPreferences.MiCloud.setCloudSpaceAlmostFullTipCounts(0);
            GalleryPreferences.MiCloud.setCloudSpaceCompletelyFullTipCounts(0);
        }
    }

    public static boolean isAccountSpaceInfoCheckable(Context context) {
        if (context == null) {
            DefaultLogger.d("GalleryMiCloudUtil", "activity is null");
            return false;
        } else if (FileSizeFormatter.localeFromContext(context) == null) {
            DefaultLogger.d("GalleryMiCloudUtil", "local is null");
            return false;
        } else if (Build.IS_INTERNATIONAL_BUILD) {
            DefaultLogger.d("GalleryMiCloudUtil", "global version");
            return false;
        } else if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("GalleryMiCloudUtil", "cta failed");
            return false;
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.d("GalleryMiCloudUtil", "no network");
            return false;
        } else {
            Account account = AccountCache.getAccount();
            if (account == null) {
                DefaultLogger.d("GalleryMiCloudUtil", "no account");
                return false;
            }
            if (SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")) {
                return true;
            }
            DefaultLogger.d("GalleryMiCloudUtil", "sync disable");
            return false;
        }
    }
}
