package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.net.ParseException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.cloud.AsyncUpDownloadService;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.ServerErrorCode;
import com.miui.gallery.cloud.adapter.PullCardAdapter;
import com.miui.gallery.cloud.adapter.PullFaceDataAdapter;
import com.miui.gallery.cloud.adapter.PullOwnerDataAdapter;
import com.miui.gallery.cloud.adapter.PullSecretDataAdapter;
import com.miui.gallery.cloud.adapter.PullShareAdapter;
import com.miui.gallery.cloud.adapter.PushBabyInfoAdapter;
import com.miui.gallery.cloud.adapter.PushCardAdapter;
import com.miui.gallery.cloud.adapter.PushFaceDataAdapter;
import com.miui.gallery.cloud.adapter.PushOwnerDataAdapter;
import com.miui.gallery.cloud.adapter.PushShareDataAdapter;
import com.miui.gallery.cloud.base.AbstractSyncAdapter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.control.BatteryMonitor;
import com.miui.gallery.cloud.control.ServerTagCache;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.cloud.syncstate.SyncMonitor;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.settingssync.GallerySettingsSyncHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

/* loaded from: classes.dex */
public class GallerySyncAdapterImpl {
    public static List<Pair<Long, Class<? extends AbstractSyncAdapter>>> sSyncAdapters;
    public Context mContext;
    public AsyncUpDownloadService.SyncLock mSyncLock;

    public GallerySyncAdapterImpl(Context context) {
        this.mContext = context;
    }

    static {
        LinkedList linkedList = new LinkedList();
        sSyncAdapters = linkedList;
        linkedList.add(new Pair(1L, PullOwnerDataAdapter.class));
        sSyncAdapters.add(new Pair<>(2L, PullSecretDataAdapter.class));
        sSyncAdapters.add(new Pair<>(4L, PullCardAdapter.class));
        sSyncAdapters.add(new Pair<>(8L, PullFaceDataAdapter.class));
        sSyncAdapters.add(new Pair<>(16L, PullShareAdapter.class));
        sSyncAdapters.add(new Pair<>(32L, PushOwnerDataAdapter.class));
        sSyncAdapters.add(new Pair<>(64L, PushCardAdapter.class));
        sSyncAdapters.add(new Pair<>(128L, PushFaceDataAdapter.class));
        sSyncAdapters.add(new Pair<>(256L, PushBabyInfoAdapter.class));
        sSyncAdapters.add(new Pair<>(512L, PushShareDataAdapter.class));
    }

    public static List<AbstractSyncAdapter> maskAdapters(Context context, long j) {
        LinkedList linkedList = new LinkedList();
        for (Pair<Long, Class<? extends AbstractSyncAdapter>> pair : sSyncAdapters) {
            if ((((Long) pair.first).longValue() & j) == ((Long) pair.first).longValue()) {
                AbstractSyncAdapter create = create(context, (Class) pair.second);
                if (create != null) {
                    linkedList.add(create);
                } else {
                    SyncLogger.e("GallerySyncAdapterImpl", "create instance error for %s", ((Class) pair.second).getSimpleName());
                }
            }
        }
        return linkedList;
    }

    public static <T extends AbstractSyncAdapter> T create(Context context, Class<T> cls) {
        try {
            return cls.getConstructor(Context.class).newInstance(context);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public final boolean fetchSyncExtraInfoFromV2ToV3(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) throws ClientProtocolException, IOException, JSONException, URISyntaxException, IllegalBlockSizeException, BadPaddingException, GalleryMiCloudServerException {
        if (Preference.getSyncFetchSyncExtraInfoFromV2ToV3()) {
            new FetchSyncExtraInfoTask(this.mContext, account, galleryExtendedAuthToken).run();
            if (!Preference.getSyncFetchSyncExtraInfoFromV2ToV3()) {
                return true;
            }
            SyncLogger.e("GallerySyncAdapterImpl", "fail to fetch syncExtraInfo when upgrade from v2 to v3");
            return false;
        }
        return true;
    }

    public final void acquireLock() {
        synchronized (this) {
            if (this.mSyncLock == null) {
                this.mSyncLock = AsyncUpDownloadService.newSyncLock("GallerySyncAdapterImpl");
            }
            this.mSyncLock.acquire();
        }
    }

    public final void releaseLock() {
        synchronized (this) {
            AsyncUpDownloadService.SyncLock syncLock = this.mSyncLock;
            if (syncLock != null) {
                syncLock.release();
                this.mSyncLock = null;
            }
        }
    }

    public final <T> GallerySyncResult<T> executeAdapter(AbstractSyncAdapter<T> abstractSyncAdapter, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, Bundle bundle) {
        abstractSyncAdapter.isAsynchronous();
        SyncMonitor.getInstance().onHandleReason(abstractSyncAdapter.getBindingReason());
        return abstractSyncAdapter.startSync(account, bundle, galleryExtendedAuthToken);
    }

    public static boolean isPush(Bundle bundle) {
        return !TextUtils.isEmpty(bundle.getString("pushName"));
    }

    public static Bundle parsePushParams(Context context, Account account, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle.getString("pushType");
        String string = bundle.getString("pushName");
        String string2 = bundle.getString("pushData");
        for (Map.Entry<Integer, GalleryCloudSyncTagUtils.SyncTagConstant> entry : GalleryCloudSyncTagUtils.sSyncTagConstantsMap.entrySet()) {
            if (TextUtils.equals(string, entry.getValue().pushName)) {
                bundle2.putInt("sync_tag_type", entry.getKey().intValue());
                bundle2.putString("sync_tag_data", string2);
                ArrayList arrayList = new ArrayList();
                arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(entry.getKey().intValue()));
                bundle2.putBoolean("sync_data_exist", TextUtils.equals(Long.toString(GalleryCloudSyncTagUtils.getSyncTag(context, account, arrayList).get(0).currentValue), string2));
            }
        }
        return bundle2;
    }

    public static long parsePushReason(Bundle bundle) {
        int i = bundle.getInt("sync_tag_type");
        String string = bundle.getString("sync_tag_data");
        SyncLogger.d("GallerySyncAdapterImpl", "request caused by push: type[%s], data[%s].", Integer.valueOf(i), string);
        if (bundle.getBoolean("sync_data_exist", false)) {
            SyncLogger.w("GallerySyncAdapterImpl", "push data[%s] exist, ignore!");
        } else if (i != 1) {
            if (i == 2 || i == 3 || i == 4 || i == 5) {
                return 16L;
            }
            if (i == 11) {
                return 8L;
            }
            SyncLogger.e("GallerySyncAdapterImpl", "unknown push %s", bundle);
        } else if (!ServerTagCache.getInstance().contains(string)) {
            return 1L;
        } else {
            SyncLogger.d("GallerySyncAdapterImpl", "operation server tag, ignore push: %s", string);
        }
        return 0L;
    }

    public final void handlePush(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, Bundle bundle) {
        Bundle parsePushParams = parsePushParams(this.mContext, account, bundle);
        int i = parsePushParams.getInt("sync_tag_type");
        SyncLogger.d("GallerySyncAdapterImpl", "request caused by push: type[%s], data[%s].", Integer.valueOf(i), parsePushParams.getString("sync_tag_data"));
        if (parsePushParams.getBoolean("sync_data_exist", false)) {
            SyncLogger.w("GallerySyncAdapterImpl", "push data[%s] exist, ignore!");
            return;
        }
        long parsePushReason = parsePushReason(parsePushParams);
        SyncLogger.d("GallerySyncAdapterImpl", "request by push, sync reason[%s]", Long.toBinaryString(parsePushReason));
        for (AbstractSyncAdapter abstractSyncAdapter : maskAdapters(this.mContext, parsePushReason)) {
            executeAdapter(abstractSyncAdapter, account, galleryExtendedAuthToken, parsePushParams);
        }
    }

    public final void handleRequest(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, Bundle bundle) {
        if (!Preference.sIsFirstSynced()) {
            long currentTimeMillis = System.currentTimeMillis();
            SyncLogger.w("GallerySyncAdapterImpl", "first sync start: %d", Long.valueOf(currentTimeMillis));
            GalleryPreferences.Sync.setFirstSyncStartTime(currentTimeMillis);
        }
        SyncType parseSyncType = parseSyncType(bundle);
        long parseSyncReason = parseSyncReason(bundle);
        SyncLogger.d("GallerySyncAdapterImpl", "request caused by local: syncType[%s], reason[%s].", parseSyncType, Long.toBinaryString(parseSyncReason));
        GalleryPreferences.Sync.setLastSyncTimestamp(System.currentTimeMillis());
        statSyncDispatchInterval(parseSyncType);
        GallerySettingsSyncHelper.doUpload(this.mContext);
        Boolean bool = null;
        for (AbstractSyncAdapter abstractSyncAdapter : maskAdapters(this.mContext, parseSyncReason)) {
            GallerySyncResult executeAdapter = executeAdapter(abstractSyncAdapter, account, galleryExtendedAuthToken, bundle);
            if (abstractSyncAdapter instanceof PullOwnerDataAdapter) {
                bool = Boolean.valueOf(executeAdapter != null && executeAdapter.code == GallerySyncCode.OK);
                GalleryPreferences.Sync.setSyncCompletelyFinish(bool.booleanValue());
            }
        }
        if (bool != null) {
            onPullOwnerEnd(bool.booleanValue());
        }
    }

    public final void statSyncDispatchInterval(SyncType syncType) {
        long requestStartTime = GalleryPreferences.Sync.getRequestStartTime(syncType);
        if (requestStartTime > 0) {
            long currentTimeMillis = System.currentTimeMillis() - requestStartTime;
            HashMap hashMap = new HashMap();
            hashMap.put("syncType", syncType.name());
            hashMap.put("interval", String.valueOf(currentTimeMillis));
            SamplingStatHelper.recordCountEvent("Sync", "sync_dispatch_interval", hashMap);
            GalleryPreferences.Sync.clearRequestStartTime();
            SyncLogger.d("GallerySyncAdapterImpl", "sync for %s dispatch cost %d", syncType, Long.valueOf(currentTimeMillis));
        }
    }

    public final void onPullOwnerEnd(boolean z) {
        if (!z || Preference.sIsFirstSynced()) {
            return;
        }
        SyncLogger.w("GallerySyncAdapterImpl", "first sync end, cost: %d", Long.valueOf(System.currentTimeMillis() - GalleryPreferences.Sync.getFirstSyncStartTime()));
        Preference.sSetFirstSynced();
        doPostFirstSyncJob();
    }

    public final SyncType parseSyncType(Bundle bundle) {
        if (isPush(bundle)) {
            return SyncType.NORMAL;
        }
        return SyncUtil.unpackSyncType(bundle);
    }

    public final long parseSyncReason(Bundle bundle) {
        if (isPush(bundle)) {
            return parsePushReason(parsePushParams(this.mContext, AccountCache.getAccount(), bundle));
        }
        return bundle.getLong("sync_reason", Long.MAX_VALUE);
    }

    public void onPerformMiCloudSync(Bundle bundle, Account account, SyncResult syncResult, GalleryExtendedAuthToken galleryExtendedAuthToken) throws GalleryMiCloudServerException {
        acquireLock();
        BatteryMonitor.getInstance().start();
        SyncMonitor.getInstance().onSyncStart(parseSyncType(bundle), parseSyncReason(bundle));
        long currentTimeMillis = System.currentTimeMillis();
        try {
            if (account != null) {
                try {
                    try {
                        try {
                            try {
                                try {
                                } catch (ParseException e) {
                                    e = e;
                                    syncResult.stats.numParseExceptions++;
                                    SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                                    releaseLock();
                                    BatteryMonitor.getInstance().end();
                                    SyncMonitor.getInstance().onSyncEnd();
                                    SyncLogger.e("GallerySyncAdapterImpl", e);
                                    SyncMonitor.getInstance().onSyncThrowable(e);
                                    recordSyncError(e);
                                    GalleryDBHelper.getInstance(this.mContext).analyze();
                                    DeleteAccount.deleteAccountAfterSyncIfNeeded();
                                    return;
                                }
                            } catch (IllegalArgumentException e2) {
                                e = e2;
                                syncResult.stats.numParseExceptions++;
                                SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                                releaseLock();
                                BatteryMonitor.getInstance().end();
                                SyncMonitor.getInstance().onSyncEnd();
                                SyncLogger.e("GallerySyncAdapterImpl", e);
                                SyncMonitor.getInstance().onSyncThrowable(e);
                                recordSyncError(e);
                                GalleryDBHelper.getInstance(this.mContext).analyze();
                                DeleteAccount.deleteAccountAfterSyncIfNeeded();
                                return;
                            }
                        } catch (Exception e3) {
                            e = e3;
                            if (e instanceof GalleryMiCloudServerException) {
                                ((GalleryMiCloudServerException) e).getCloudServerException();
                                throw ((GalleryMiCloudServerException) e);
                            }
                            SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                            releaseLock();
                            BatteryMonitor.getInstance().end();
                            SyncMonitor.getInstance().onSyncEnd();
                            SyncLogger.e("GallerySyncAdapterImpl", e);
                            SyncMonitor.getInstance().onSyncThrowable(e);
                            recordSyncError(e);
                            GalleryDBHelper.getInstance(this.mContext).analyze();
                            DeleteAccount.deleteAccountAfterSyncIfNeeded();
                            return;
                        }
                    } catch (IOException e4) {
                        e = e4;
                        syncResult.stats.numIoExceptions++;
                        SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                        releaseLock();
                        BatteryMonitor.getInstance().end();
                        SyncMonitor.getInstance().onSyncEnd();
                        SyncLogger.e("GallerySyncAdapterImpl", e);
                        SyncMonitor.getInstance().onSyncThrowable(e);
                        recordSyncError(e);
                        GalleryDBHelper.getInstance(this.mContext).analyze();
                        DeleteAccount.deleteAccountAfterSyncIfNeeded();
                        return;
                    }
                } catch (URISyntaxException e5) {
                    e = e5;
                    syncResult.stats.numParseExceptions++;
                    SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    releaseLock();
                    BatteryMonitor.getInstance().end();
                    SyncMonitor.getInstance().onSyncEnd();
                    SyncLogger.e("GallerySyncAdapterImpl", e);
                    SyncMonitor.getInstance().onSyncThrowable(e);
                    recordSyncError(e);
                    GalleryDBHelper.getInstance(this.mContext).analyze();
                    DeleteAccount.deleteAccountAfterSyncIfNeeded();
                    return;
                } catch (JSONException e6) {
                    e = e6;
                    syncResult.stats.numParseExceptions++;
                    SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    releaseLock();
                    BatteryMonitor.getInstance().end();
                    SyncMonitor.getInstance().onSyncEnd();
                    SyncLogger.e("GallerySyncAdapterImpl", e);
                    SyncMonitor.getInstance().onSyncThrowable(e);
                    recordSyncError(e);
                    GalleryDBHelper.getInstance(this.mContext).analyze();
                    DeleteAccount.deleteAccountAfterSyncIfNeeded();
                    return;
                }
                if (!TextUtils.isEmpty(account.name) && !TextUtils.isEmpty(account.type)) {
                    if (!CloudUtils.checkAccount(null, true, null)) {
                        SyncLogger.e("GallerySyncAdapterImpl", "check account failed, return.");
                    } else {
                        resetAccountInfo(account, galleryExtendedAuthToken);
                        GalleryCloudSyncTagUtils.insertAccountToDB(this.mContext, account);
                        ServerErrorCode.MiCloudServerExceptionHandler.checkMiCloudServerException();
                        SyncConditionManager.setCancelledForAllBackground(false);
                        if (fetchSyncExtraInfoFromV2ToV3(account, galleryExtendedAuthToken)) {
                            if (ClearDataManager.getInstance().clearDataBaseIfNeeded(this.mContext, account)) {
                                SyncLogger.w("GallerySyncAdapterImpl", "clear data before syncing");
                            }
                            SyncLogger.w("GallerySyncAdapterImpl", "handle sync start %d", Long.valueOf(currentTimeMillis));
                            if (isPush(bundle)) {
                                handlePush(account, galleryExtendedAuthToken, bundle);
                            } else {
                                handleRequest(account, galleryExtendedAuthToken, bundle);
                            }
                            DownloadUtil.resumeInterrupted();
                            SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                            releaseLock();
                            BatteryMonitor.getInstance().end();
                            SyncMonitor.getInstance().onSyncEnd();
                            GalleryDBHelper.getInstance(this.mContext).analyze();
                            DeleteAccount.deleteAccountAfterSyncIfNeeded();
                            return;
                        }
                        SyncLogger.e("GallerySyncAdapterImpl", "fetchSyncExtraInfoFromV2ToV3 Exit");
                    }
                    SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    releaseLock();
                    BatteryMonitor.getInstance().end();
                    SyncMonitor.getInstance().onSyncEnd();
                    GalleryDBHelper.getInstance(this.mContext).analyze();
                    DeleteAccount.deleteAccountAfterSyncIfNeeded();
                }
            }
            String str = "";
            String str2 = account == null ? str : account.name;
            if (account != null) {
                str = account.type;
            }
            SyncLogger.e("GallerySyncAdapterImpl", "account: %s, name: %s, type %s, have none value, return.", account, str2, str);
            SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            releaseLock();
            BatteryMonitor.getInstance().end();
            SyncMonitor.getInstance().onSyncEnd();
            GalleryDBHelper.getInstance(this.mContext).analyze();
            DeleteAccount.deleteAccountAfterSyncIfNeeded();
        } catch (Throwable th) {
            SyncLogger.w("GallerySyncAdapterImpl", "handle sync finished, cost: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            releaseLock();
            BatteryMonitor.getInstance().end();
            SyncMonitor.getInstance().onSyncEnd();
            if (0 != 0) {
                SyncLogger.e("GallerySyncAdapterImpl", (Throwable) null);
                SyncMonitor.getInstance().onSyncThrowable(null);
                recordSyncError(null);
            }
            GalleryDBHelper.getInstance(this.mContext).analyze();
            DeleteAccount.deleteAccountAfterSyncIfNeeded();
            throw th;
        }
    }

    public final void recordSyncError(Throwable th) {
        if (th != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("error", Log.getStackTraceString(th));
            SamplingStatHelper.recordCountEvent("Sync", "sync_error_message", hashMap);
            if (Preference.sIsFirstSynced()) {
                return;
            }
            GalleryPreferences.Sync.increaseFirstSyncFailCount();
        }
    }

    public static void resetAccountInfo(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        AccountCache.update(account, galleryExtendedAuthToken);
        SyncLogger.d("GallerySyncAdapterImpl", "reset AccountCache");
        GalleryKssManager.reset();
        SyncLogger.d("GallerySyncAdapterImpl", "reset GalleryKssManager");
    }

    public void onSyncCanceled() {
        SyncConditionManager.setCancelledForAllBackground(true);
        UpDownloadManager.cancelAllBackgroundPriority(true, true);
    }

    public final void statFirstSync(long j) {
        long firstSyncStartTime = GalleryPreferences.Sync.getFirstSyncStartTime();
        if (firstSyncStartTime == 0 || firstSyncStartTime > j) {
            SyncLogger.w("GallerySyncAdapterImpl", "invalid first sync time, start: %d, finish: %d", Long.valueOf(firstSyncStartTime), Long.valueOf(j));
            return;
        }
        int[] syncedCount = SyncStateUtil.getSyncedCount(this.mContext);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(j - firstSyncStartTime);
        HashMap hashMap = new HashMap();
        hashMap.put("photo_count", String.valueOf(syncedCount[0]));
        hashMap.put("video_count", String.valueOf(syncedCount[1]));
        hashMap.put("cost_time", String.valueOf(minutes));
        hashMap.put("fail_count", String.valueOf(GalleryPreferences.Sync.getFirstSyncFailCount()));
        SamplingStatHelper.recordCountEvent("Sync", "first_sync_duration", hashMap);
        GalleryPreferences.Sync.clearFirstSyncFailCount();
        SyncLogger.d("GallerySyncAdapterImpl", "first sync finished: %s", hashMap.toString());
    }

    public final void doPostFirstSyncJob() {
        long currentTimeMillis = System.currentTimeMillis();
        if (GalleryPreferences.CloudControl.getLastRequestSucceedTime() <= 0) {
            GallerySettingsSyncHelper.doUpload(this.mContext);
            SyncLogger.d("GallerySyncAdapterImpl", "Request cloud control after first sync, result %s", String.valueOf(new CloudControlRequestHelper(this.mContext).execRequestSync(true)));
        }
        TrashUtils.pullDeleteListFromServer();
        ScannerEngine.getInstance().triggerScan();
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(new Intent("com.miui.gallery.action.FIRST_SYNC_FINISHED"));
        if (BatchDownloadManager.canAutoDownload()) {
            BatchDownloadManager.getInstance().startBatchDownload(this.mContext, true);
        }
        statFirstSync(currentTimeMillis);
    }
}
