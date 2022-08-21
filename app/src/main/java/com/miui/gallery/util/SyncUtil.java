package com.miui.gallery.util;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.account.AccountHelper;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.UpDownloadManager;
import com.miui.gallery.cloud.base.SyncConstants$Sync;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.policy.IPolicy;
import com.miui.gallery.cloud.policy.SyncPolicyManager;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import miui.cloud.sdk.CloudResolverCompat;
import miui.cloud.util.SyncAutoSettingUtil;
import miui.gallery.support.SyncCompat;
import miuix.net.ConnectivityHelper;

/* loaded from: classes2.dex */
public class SyncUtil {
    public static boolean existXiaomiAccount(Context context) {
        if (context != null) {
            return AccountHelper.getXiaomiAccount(context) != null;
        }
        DefaultLogger.e("SyncUtil", "existXiaomiAccount context null");
        return false;
    }

    public static boolean isGalleryCloudSyncable(Context context) {
        if (context == null) {
            DefaultLogger.e("SyncUtil", "isGalleryCloudSyncable context null");
            return false;
        }
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(context);
        if (xiaomiAccount != null) {
            return SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(xiaomiAccount, "com.miui.gallery.cloud.provider");
        }
        DefaultLogger.e("SyncUtil", "isGalleryCloudSyncable account null");
        return false;
    }

    public static void requestSync(Context context) {
        requestSync(context, new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(33L).setDelayUpload(true).build());
    }

    public static void requestSync(Context context, SyncRequest syncRequest) {
        if (context == null) {
            DefaultLogger.e("SyncUtil", "requestSync context null");
        } else if (!AgreementsUtils.isNetworkingAgreementAccepted()) {
            DefaultLogger.w("SyncUtil", "networking agreement hasn't accepted");
        } else if (Looper.getMainLooper() == Looper.myLooper()) {
            requestSyncInWorkThread(context, syncRequest);
        } else {
            doRequestSync(context, syncRequest);
        }
    }

    public static void requestSyncInWorkThread(Context context, final SyncRequest syncRequest) {
        final Context applicationContext = context.getApplicationContext();
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.util.SyncUtil.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                SyncUtil.doRequestSync(applicationContext, syncRequest);
                return null;
            }
        });
    }

    public static void doRequestSync(Context context, SyncRequest syncRequest) {
        Context applicationContext = context.getApplicationContext();
        if (!isGalleryCloudSyncable(applicationContext)) {
            DefaultLogger.e("SyncUtil", "isGalleryCloudSyncable false");
        } else if (isSyncPause(applicationContext)) {
            SyncLogger.w("SyncUtil", "sync has pause");
        } else {
            SyncType wrapSyncType = wrapSyncType(syncRequest.getSyncType());
            SyncRequest build = new SyncRequest.Builder().cloneFrom(syncRequest).setSyncType(wrapSyncType).build();
            SyncLogger.d("SyncUtil", "requestSync: request[%s] %s", build, TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
            if (AccountHelper.getXiaomiAccount(applicationContext) == null) {
                SyncLogger.w("SyncUtil", "account is null");
                return;
            }
            statSyncOvertimeInterval(wrapSyncType);
            android.content.SyncRequest packSyncParams = packSyncParams(applicationContext, build);
            if (packSyncParams != null) {
                ContentResolver.requestSync(packSyncParams);
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(new Intent("com.miui.gallery.SYNC_COMMAND_DISPATCHED"));
                GalleryPreferences.Sync.markRequestStartTimeIfNone(wrapSyncType);
                return;
            }
            SyncLogger.e("SyncUtil", "sync request null");
        }
    }

    public static void statSyncOvertimeInterval(SyncType syncType) {
        long requestStartTime = GalleryPreferences.Sync.getRequestStartTime(syncType);
        if (requestStartTime <= 0 || syncType == null) {
            return;
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - requestStartTime);
        if (minutes <= 30 || !checkSyncCondition(syncType.isForce())) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("syncType", syncType.name());
        hashMap.put("interval", String.valueOf(minutes));
        SamplingStatHelper.recordCountEvent("Sync", "sync_overtime_interval", hashMap);
    }

    public static boolean checkSyncCondition(boolean z) {
        if (BaseGalleryPreferences.CTA.canConnectNetwork() && BaseNetworkUtils.isNetworkConnected()) {
            if (!z && !ConnectivityHelper.getInstance(StaticContext.sGetAndroidContext()).isUnmeteredNetworkConnected()) {
                return false;
            }
            return z || GalleryPreferences.Sync.getPowerCanSync();
        }
        return false;
    }

    public static boolean isSyncPause(Context context) {
        if (context == null) {
            DefaultLogger.e("SyncUtil", "isSyncPause context null");
            return false;
        }
        Account account = AccountCache.getAccount();
        if (account == null) {
            DefaultLogger.e("SyncUtil", "isSyncPause account null");
            return false;
        }
        return CloudResolverCompat.isSyncPaused(context, account, "com.miui.gallery.cloud.provider");
    }

    public static void pauseSync(Context context, long j) {
        if (context == null) {
            DefaultLogger.e("SyncUtil", "pauseSync context null");
            return;
        }
        Account account = AccountCache.getAccount();
        if (account == null) {
            DefaultLogger.e("SyncUtil", "pauseSync account null");
            return;
        }
        CloudResolverCompat.pauseSync(context.getApplicationContext(), account, "com.miui.gallery.cloud.provider", j);
        stopSync(context.getApplicationContext());
    }

    public static void resumeSync(Context context) {
        if (context == null) {
            DefaultLogger.e("SyncUtil", "resumeSync context null");
            return;
        }
        Account account = AccountCache.getAccount();
        if (account == null) {
            DefaultLogger.e("SyncUtil", "pauseSync account null");
        } else {
            CloudResolverCompat.resumeSync(context.getApplicationContext(), account, "com.miui.gallery.cloud.provider");
        }
    }

    public static void stopSync(Context context) {
        if (context == null) {
            DefaultLogger.e("SyncUtil", "stopSync context null");
            return;
        }
        Account account = AccountCache.getAccount();
        if (account == null) {
            DefaultLogger.e("SyncUtil", "stopSync account null");
            return;
        }
        ContentResolver.cancelSync(account, "com.miui.gallery.cloud.provider");
        SyncConditionManager.setCancelledForAllBackground(true);
        UpDownloadManager.cancelAllBackgroundPriority(true, true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.miui.gallery.SYNC_COMMAND_DISPATCHED"));
    }

    public static boolean setSyncAutomatically(Context context, boolean z) {
        return setSyncAutomatically(context, z, -1);
    }

    public static boolean setSyncAutomatically(Context context, boolean z, int i) {
        if (context == null) {
            DefaultLogger.e("SyncUtil", "switchBackup context null");
            return false;
        }
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(context);
        if (z) {
            if (!PrivacyAgreementUtils.isCloudServiceAgreementEnable(context) && IntentUtil.startCloudMainPage(context)) {
                return false;
            }
            if (xiaomiAccount == null) {
                if ((context instanceof GalleryActivity) && i > 0) {
                    IntentUtil.guideToLoginXiaomiAccount((GalleryActivity) context, GalleryIntent$CloudGuideSource.AUTOBACKUP, i);
                } else {
                    IntentUtil.guideToLoginXiaomiAccount(context, GalleryIntent$CloudGuideSource.AUTOBACKUP);
                }
                return false;
            }
        } else {
            stopSync(context);
        }
        statSyncEnabledEvent(z);
        if (xiaomiAccount == null) {
            return true;
        }
        ContentResolver.setSyncAutomatically(xiaomiAccount, "com.miui.gallery.cloud.provider", z);
        return true;
    }

    public static void setBackupOnlyWifi(Context context, boolean z) {
        Account xiaomiAccount;
        if (context == null) {
            DefaultLogger.e("SyncUtil", "setBackupOnlyWifi context null");
            return;
        }
        GalleryPreferences.Sync.setBackupOnlyInWifi(z);
        if (z || (xiaomiAccount = AccountHelper.getXiaomiAccount(context)) == null || ContentResolver.isSyncActive(xiaomiAccount, "com.miui.gallery.cloud.provider") || !ContentResolver.isSyncPending(xiaomiAccount, "com.miui.gallery.cloud.provider")) {
            return;
        }
        SyncType wrapSyncType = wrapSyncType(SyncStateManager.getInstance().getSyncType());
        if (SyncConditionManager.checkIgnoreCancel(5, wrapSyncType) != 0) {
            return;
        }
        long syncReason = SyncStateManager.getInstance().getSyncReason();
        if (syncReason == 0) {
            syncReason = 33;
        }
        SyncLogger.d("SyncUtil", "not back only wifi, sync pending, but condition ok, request reason[%s]", Long.toBinaryString(syncReason));
        requestSync(context, new SyncRequest.Builder().setSyncType(wrapSyncType).setSyncReason(syncReason).build());
    }

    public static long getResumeTime(Context context) {
        if (context == null) {
            DefaultLogger.e("SyncUtil", "getResumeTime context null");
            return -1L;
        }
        Account account = AccountCache.getAccount();
        if (account == null) {
            DefaultLogger.e("SyncUtil", "getResumeTime account null");
            return -1L;
        }
        return CloudResolverCompat.getResumeTime(context.getApplicationContext(), account, "com.miui.gallery.cloud.provider");
    }

    public static SyncType wrapSyncType(SyncType syncType) {
        SyncType syncType2 = SyncType.NORMAL;
        return (syncType == syncType2 || syncType == SyncType.UNKNOW || syncType == SyncType.BACKGROUND) ? MiscUtil.isAppProcessInForeground() ? syncType2 : SyncType.BACKGROUND : syncType;
    }

    public static android.content.SyncRequest packSyncParams(Context context, SyncRequest syncRequest) {
        boolean z;
        boolean z2;
        if (context == null) {
            DefaultLogger.e("SyncUtil", "packSyncParams context null");
            return null;
        }
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(context);
        if (xiaomiAccount == null) {
            DefaultLogger.e("SyncUtil", "packSyncParams xiaomiAccount null");
            return null;
        }
        SyncRequest.Builder syncOnce = new SyncRequest.Builder().setSyncAdapter(xiaomiAccount, "com.miui.gallery.cloud.provider").syncOnce();
        IPolicy policy = SyncPolicyManager.getInstance().getPolicy(syncRequest.getSyncType());
        if (policy == null || !policy.isEnable()) {
            DefaultLogger.w("SyncUtil", "no policy for %s", syncRequest.getSyncType());
            policy = SyncPolicyManager.getInstance().getPolicy(SyncType.NORMAL);
        }
        boolean z3 = false;
        boolean z4 = policy != null && policy.isEnable();
        if (!z4) {
            DefaultLogger.w("SyncUtil", "policy not valid %s", policy);
        }
        boolean isManual = syncRequest.isManual();
        boolean z5 = syncRequest.getSyncType().isForce() || syncRequest.isManual();
        if (isMetaDataRequest()) {
            z2 = false;
            z = true;
        } else if (z4) {
            z3 = policy.isDisallowMetered();
            z2 = policy.isRequireCharging();
            z = policy.isIgnoreBatteryLow();
        } else {
            z = false;
            z2 = false;
            z3 = true;
        }
        syncOnce.setDisallowMetered(z3).setExpedited(z5).setManual(isManual);
        if (z2) {
            SyncCompat.setRequiresCharging(syncOnce, z2);
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("sync_no_delay", !syncRequest.isDelayUpload());
        bundle.putString("sync_type", syncRequest.getSyncType().getIdentifier());
        bundle.putLong("sync_reason", syncRequest.getSyncReason());
        if (!z3) {
            bundle.putBoolean("micloud_ignore_wifi_settings", true);
        }
        if (z) {
            bundle.putBoolean("micloud_ignore_battery_low", true);
        }
        if (isManual) {
            bundle.putBoolean(SyncConstants$Sync.EXTRA_SYNC_FORCE, true);
            bundle.putBoolean("force", true);
        }
        syncOnce.setExtras(bundle);
        return syncOnce.build();
    }

    public static boolean isMetaDataRequest() {
        return !Preference.sIsFirstSynced();
    }

    public static SyncType unpackSyncType(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey("sync_type")) {
                return SyncType.fromIdentifier(bundle.getString("sync_type"));
            }
            if (bundle.getBoolean("micloud_ignore_wifi_settings", false)) {
                return SyncType.GPRS_FORCE;
            }
            if (bundle.getBoolean("micloud_ignore_battery_low", false)) {
                return SyncType.POWER_FORCE;
            }
        }
        return SyncType.NORMAL;
    }

    public static void statSyncEnabledEvent(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("state", Boolean.toString(z));
        SamplingStatHelper.recordCountEvent("micloud_sync", "sync_enabled", hashMap);
    }
}
