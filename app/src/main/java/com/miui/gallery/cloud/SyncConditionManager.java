package com.miui.gallery.cloud;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.policy.IPolicy;
import com.miui.gallery.cloud.policy.SyncPolicyManager;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.SyncStrategy;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncLogger;

/* loaded from: classes.dex */
public class SyncConditionManager {
    public static SyncStrategy sSyncConfig;
    public static Object sSyncConfigLock = new Object();
    public static final CheckerFactory sFactory = new CheckerFactory();
    public static final Checker[] sCheckers = new Checker[14];
    public static long sSpaceCheckTime = 0;

    public static Checker instance(int i) {
        Checker checker;
        Checker[] checkerArr = sCheckers;
        synchronized (checkerArr) {
            if (checkerArr[i] == null) {
                checkerArr[i] = sFactory.createChecker(i);
            }
            checker = checkerArr[i];
        }
        return checker;
    }

    public static void setCancelled(int i, boolean z) {
        instance(i).setCanceled(z);
    }

    public static void setCancelledForAllBackground(boolean z) {
        for (int i = 0; i < 14; i++) {
            if (RequestItemBase.isBackgroundPriority(i)) {
                setCancelled(i, z);
            }
        }
    }

    public static int check(int i) {
        return checkInternal(i, SyncStateManager.getInstance().getSyncType());
    }

    public static int checkForItem(RequestItemBase requestItemBase) {
        int checkInternal = checkInternal(requestItemBase.priority, requestItemBase.getSpecificType() != SyncType.UNKNOW ? requestItemBase.getSpecificType() : SyncStateManager.getInstance().getSyncType());
        if (checkInternal != 0 || requestItemBase.getStatus() == 0) {
            return checkInternal;
        }
        return 3;
    }

    public static int checkIgnoreCancel(int i, SyncType syncType) {
        return checkInternalIgnoreCancel(i, syncType);
    }

    public static int checkInternal(int i, SyncType syncType) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            SyncLogger.d("SyncConditionManager", "check cta false");
            return 2;
        }
        return instance(i).check(syncType);
    }

    public static int checkInternalIgnoreCancel(int i, SyncType syncType) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            SyncLogger.d("SyncConditionManager", "check cta false");
            return 2;
        }
        return instance(i).checkIgnoreCancel(syncType);
    }

    public static SyncStrategy sGetSyncConfig() {
        SyncStrategy syncStrategy;
        synchronized (sSyncConfigLock) {
            if (sSyncConfig == null) {
                sSyncConfig = CloudControlStrategyHelper.getSyncStrategy();
            }
            syncStrategy = sSyncConfig;
        }
        return syncStrategy;
    }

    public static int checkCloudSpace(Context context) {
        if (SpaceFullHandler.isOwnerSpaceFull()) {
            SyncLogger.d("SyncConditionManager", "Preference.sGetCloudGallerySpaceFull");
            long currentTimeMillis = System.currentTimeMillis() - sSpaceCheckTime;
            if (currentTimeMillis > 0 && currentTimeMillis < 10800000) {
                SyncLogger.d("SyncConditionManager", "interval %s", Long.valueOf(currentTimeMillis));
                return 2;
            }
            SyncLogger.d("SyncConditionManager", "try refresh space full pref entry");
            sSpaceCheckTime = System.currentTimeMillis();
            return 0;
        }
        return 0;
    }

    /* loaded from: classes.dex */
    public static abstract class Checker {
        public volatile boolean mCanceled = false;
        public final String TAG = getClass().getSimpleName();

        public abstract int checkInternal(SyncType syncType);

        public final boolean isCanceled() {
            return this.mCanceled;
        }

        public void setCanceled(boolean z) {
            if (z) {
                String str = this.TAG;
                SyncLogger.d(str, new Throwable("setCanceled: " + z));
            }
            this.mCanceled = z;
        }

        public int check(SyncType syncType) {
            if (isCanceled()) {
                SyncLogger.d(this.TAG, "isCanceled");
                return 2;
            } else if (!BaseNetworkUtils.isNetworkConnected()) {
                SyncLogger.d(this.TAG, "network not connected");
                return 2;
            } else {
                int checkInternal = checkInternal(syncType);
                if (checkInternal != 0) {
                    SyncLogger.d(this.TAG, "check result %d, sync type %s", Integer.valueOf(checkInternal), syncType);
                }
                return checkInternal;
            }
        }

        public int checkIgnoreCancel(SyncType syncType) {
            if (!BaseNetworkUtils.isNetworkConnected()) {
                SyncLogger.d(this.TAG, "network not connected");
                return 2;
            }
            return checkInternal(syncType);
        }
    }

    /* loaded from: classes.dex */
    public static class InvalidChecker extends Checker {
        @Override // com.miui.gallery.cloud.SyncConditionManager.Checker
        public int checkInternal(SyncType syncType) {
            return 2;
        }

        public InvalidChecker() {
        }
    }

    /* loaded from: classes.dex */
    public static class ServerTagChecker extends Checker {
        public ServerTagChecker() {
        }

        @Override // com.miui.gallery.cloud.SyncConditionManager.Checker
        public int checkInternal(SyncType syncType) {
            if (!GalleryCloudUtils.isGalleryCloudSyncable(GalleryApp.sGetAndroidContext())) {
                return 2;
            }
            IPolicy policy = SyncPolicyManager.getInstance().getPolicy(syncType);
            if (policy == null || !policy.isEnable()) {
                SyncLogger.e("SyncConditionManager", "policy not enable");
                return 2;
            } else if (policy.isIgnoreBatteryLow() || GalleryPreferences.Sync.getPowerCanSync()) {
                return 0;
            } else {
                SyncLogger.d("SyncConditionManager", "battery low");
                return 2;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class BackUploadChecker extends ServerTagChecker {
        public BackUploadChecker() {
            super();
        }

        @Override // com.miui.gallery.cloud.SyncConditionManager.ServerTagChecker, com.miui.gallery.cloud.SyncConditionManager.Checker
        public int checkInternal(SyncType syncType) {
            int checkInternal = super.checkInternal(syncType);
            if (checkInternal == 0) {
                IPolicy policy = SyncPolicyManager.getInstance().getPolicy(syncType);
                if (policy.isDisallowMetered() && BaseNetworkUtils.isActiveNetworkMetered()) {
                    SyncLogger.d("SyncConditionManager", "network is metered");
                    return 2;
                } else if (policy.isRequireCharging() && !GalleryPreferences.Sync.getIsPlugged()) {
                    SyncLogger.d("SyncConditionManager", "not charged");
                    return 2;
                }
            }
            return checkInternal;
        }
    }

    /* loaded from: classes.dex */
    public static class BackDownloadChecker extends Checker {
        public BackDownloadChecker() {
        }

        @Override // com.miui.gallery.cloud.SyncConditionManager.Checker
        public int checkInternal(SyncType syncType) {
            if (!GalleryCloudUtils.isGalleryCloudSyncable(GalleryApp.sGetAndroidContext())) {
                return 2;
            }
            boolean z = true;
            boolean z2 = !BaseNetworkUtils.isActiveNetworkMetered();
            boolean isPlugged = GalleryPreferences.Sync.getIsPlugged();
            if (z2 && isPlugged) {
                String priorStoragePath = StorageUtils.getPriorStoragePath();
                long totalBytes = StorageUtils.getTotalBytes(priorStoragePath);
                long availableBytes = StorageUtils.getAvailableBytes(priorStoragePath);
                float autoDownloadSpaceLimit = CloudControlStrategyHelper.getSyncStrategy().getAutoDownloadSpaceLimit();
                if (availableBytes <= 0 || totalBytes <= 0 || (((float) availableBytes) * 1.0f) / ((float) totalBytes) <= autoDownloadSpaceLimit) {
                    z = false;
                }
                if (z) {
                    return 0;
                }
            }
            return 2;
        }
    }

    /* loaded from: classes.dex */
    public static class BackDownloadWeakChecker extends Checker {
        public BackDownloadWeakChecker() {
        }

        @Override // com.miui.gallery.cloud.SyncConditionManager.Checker
        public int checkInternal(SyncType syncType) {
            if (!GalleryCloudUtils.isGalleryCloudSyncable(GalleryApp.sGetAndroidContext())) {
                return 2;
            }
            return (!(BaseNetworkUtils.isActiveNetworkMetered() ^ true) || !GalleryPreferences.Sync.getPowerCanSync() || !MiscUtil.isAppProcessInForeground()) ? 2 : 0;
        }
    }

    /* loaded from: classes.dex */
    public static class ForeDownloadOriginChecker extends Checker {
        public ForeDownloadOriginChecker() {
        }

        @Override // com.miui.gallery.cloud.SyncConditionManager.Checker
        public int checkInternal(SyncType syncType) {
            return BaseNetworkUtils.isActiveNetworkMetered() ^ true ? 0 : 3;
        }
    }

    /* loaded from: classes.dex */
    public static class ForceForeDownloadOriginChecker extends Checker {
        @Override // com.miui.gallery.cloud.SyncConditionManager.Checker
        public int checkInternal(SyncType syncType) {
            return 0;
        }

        public ForceForeDownloadOriginChecker() {
        }
    }

    /* loaded from: classes.dex */
    public static class ForeDownloadThumbnailChecker extends Checker {
        @Override // com.miui.gallery.cloud.SyncConditionManager.Checker
        public int checkInternal(SyncType syncType) {
            return 0;
        }

        public ForeDownloadThumbnailChecker() {
        }
    }

    /* loaded from: classes.dex */
    public static class CheckerFactory {
        public CheckerFactory() {
        }

        public Checker createChecker(int i) {
            switch (i) {
                case 0:
                case 1:
                    return new ServerTagChecker();
                case 2:
                case 3:
                case 4:
                case 5:
                    return new BackUploadChecker();
                case 6:
                case 8:
                    return new BackDownloadChecker();
                case 7:
                    return new BackDownloadWeakChecker();
                case 9:
                case 10:
                    return new ForeDownloadOriginChecker();
                case 11:
                    return new ForceForeDownloadOriginChecker();
                case 12:
                case 13:
                    return new ForeDownloadThumbnailChecker();
                default:
                    return new InvalidChecker();
            }
        }
    }
}
