package com.miui.gallery.cleaner.slim;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.PhotoSlimActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cleaner.BaseScanner;
import com.miui.gallery.cleaner.CleanerContract$Projection;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.cleaner.slim.SlimController;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CloudTableUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.cloud.syncstate.OnSyncStateChangeObserver;
import com.miui.gallery.cloud.syncstate.SyncStateInfo;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.cloud.syncstate.SyncStatus;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.ui.CleanerPhotoPickFragment;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.ui.MergeDataDialogFragment;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import miui.cloud.util.SyncAutoSettingUtil;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SlimScanner extends BaseScanner implements SlimController.SpaceSlimObserver, OnSyncStateChangeObserver {
    public static final String SLIM_SCAN_ORDER = String.format("%s DESC", "alias_sort_time");
    public static final String SLIM_SCAN_SELECTION;
    public static final String SYNCED_SLIM_SCAN_SELECTION;
    public static final String TO_SYNCED_SLIM_SCAN_SELECTION;
    public boolean mHasRefreshedSync;
    public boolean mHasRefreshedUnsync;
    public boolean mHasRegister;
    public boolean mHasValidSlimResult;
    public ScanResult.OnScanResultClickListener mOnAutoBackupDisableClickListener;
    public ScanResult.OnScanResultClickListener mOnSlimScanResultClickListener;
    public Future mScanFuture;
    public FutureHandler mScanFutureHandler;
    public ThreadPool.Job<Void> mScanJob;
    public Future mScanToSyncFuture;
    public FutureHandler mScanToSyncFutureHandler;
    public ThreadPool.Job<Void> mScanToSyncedJob;

    @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
    public void onSlimPaused() {
    }

    @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
    public void onSlimResumed() {
    }

    static {
        String str = "mimeType != 'image/gif' AND localFile NOT NULL AND localFile != '' AND localGroupId != " + CloudTableUtils.getCloudIdForGroupWithoutRecord(1000L) + " AND localGroupId != " + CloudTableUtils.getCloudIdForGroupWithoutRecord(1001L) + " AND alias_create_date < " + GalleryDateUtils.format(System.currentTimeMillis() - 2592000000L);
        SLIM_SCAN_SELECTION = str;
        SYNCED_SLIM_SCAN_SELECTION = "alias_sync_state = 0 AND " + str;
        TO_SYNCED_SLIM_SCAN_SELECTION = "alias_sync_state != 2147483647 AND alias_sync_state != 4 AND " + str;
    }

    public void doSlim(Context context) {
        boolean z;
        int i = 0;
        if (!GalleryPreferences.Sync.isAutoDownload() || !DownloadType.ORIGIN.equals(GalleryPreferences.Sync.getDownloadType())) {
            z = false;
        } else {
            GalleryPreferences.Sync.setDownloadType(DownloadType.THUMBNAIL);
            BatchDownloadManager.getInstance().startBatchDownload(context, true);
            z = true;
        }
        long[] ListToArray = MiscUtil.ListToArray(getScanResultIds());
        SlimController.getInstance().start(ListToArray);
        GalleryPreferences.Sync.setSlimDialogShowCount(0);
        GalleryPreferences.Sync.setSlimDialogPoppedUpTimestamp(0L);
        Intent intent = new Intent(context, PhotoSlimActivity.class);
        intent.putExtra("download_type_changed", z);
        if (context instanceof FragmentActivity) {
            ((FragmentActivity) context).startActivityForResult(intent, 62);
        } else {
            context.startActivity(intent);
        }
        HashMap hashMap = new HashMap();
        if (ListToArray != null) {
            i = ListToArray.length;
        }
        hashMap.put(MiStat.Param.COUNT, SamplingStatHelper.formatValueStage(i, CleanerPhotoPickFragment.DELETE_COUNT_STAGE));
        SamplingStatHelper.recordCountEvent("cleaner", "cleaner_slim_used", hashMap);
        if (GalleryPreferences.Sync.getUpdateSlimProfileStatus() != 2) {
            GalleryPreferences.Sync.setUpdateSlimProfileStatus(1);
        }
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.2
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                SlimScanner.requestUpdateProfile();
                return null;
            }
        });
    }

    public final void showSyncSettingDialog(final FragmentActivity fragmentActivity) {
        ConfirmDialog.showConfirmDialog(fragmentActivity.getSupportFragmentManager(), fragmentActivity.getString(R.string.backup_sync_dialog_title), fragmentActivity.getString(R.string.backup_sync_dialog_tips), fragmentActivity.getString(R.string.cancel), fragmentActivity.getString(R.string.backup_sync_dialog_confirm), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.4
            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onCancel(DialogFragment dialogFragment) {
            }

            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onConfirm(DialogFragment dialogFragment) {
                Intent intent = new Intent("android.settings.ACCOUNT_LIST");
                intent.setPackage("com.android.settings");
                DefaultLogger.d("SlimScanner", "go to sync settings");
                fragmentActivity.startActivity(intent);
                TrackController.trackClick("403.22.1.1.11335", AutoTracking.getRef(), "open");
            }
        });
    }

    public SlimScanner() {
        super(0);
        this.mOnSlimScanResultClickListener = new ScanResult.OnScanResultClickListener() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.1
            @Override // com.miui.gallery.cleaner.ScanResult.OnScanResultClickListener
            public void onClick(Context context) {
                SlimScanner.this.doSlim(context);
                SlimScanner.this.recordClickScanResultEvent("slimming");
                TrackController.trackClick("403.27.1.1.11314", AutoTracking.getRef());
            }
        };
        this.mOnAutoBackupDisableClickListener = new ScanResult.OnScanResultClickListener() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.3
            @Override // com.miui.gallery.cleaner.ScanResult.OnScanResultClickListener
            public void onClick(Context context) {
                if (context instanceof FragmentActivity) {
                    if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
                        SlimScanner.this.showSyncSettingDialog((FragmentActivity) context);
                        return;
                    }
                    MergeDataDialogFragment.newInstance(false).showAllowingStateLoss(((FragmentActivity) context).getSupportFragmentManager(), "MergeDataDialogFragment");
                    SlimScanner.this.recordClickScanResultEvent("merge");
                    TrackController.trackClick("403.27.7.1.14544", AutoTracking.getRef());
                }
            }
        };
        this.mHasRegister = false;
        this.mHasRefreshedUnsync = false;
        this.mHasRefreshedSync = false;
        this.mScanJob = new ThreadPool.Job<Void>() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.7
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                SlimScanner.this.scan();
                return null;
            }
        };
        this.mScanToSyncedJob = new ThreadPool.Job<Void>() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.8
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                SlimScanner.this.scanToSynced();
                return null;
            }
        };
        this.mScanFutureHandler = new FutureHandler<Void>() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.9
            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<Void> future) {
                if (!future.isCancelled()) {
                    SlimScanner slimScanner = SlimScanner.this;
                    slimScanner.updateScanResult(slimScanner.buildScanResult());
                }
            }
        };
        this.mScanToSyncFutureHandler = new FutureHandler<Void>() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.10
            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<Void> future) {
                if (!future.isCancelled()) {
                    SlimScanner slimScanner = SlimScanner.this;
                    slimScanner.updateScanResult(slimScanner.buildToSyncScanResult());
                    SlimScanner.this.recordClickScanResultEvent("syncing");
                    TrackController.trackExpose("403.27.7.1.14545", AutoTracking.getRef());
                }
            }
        };
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public ScanResult scan() {
        Account account = AccountCache.getAccount();
        if (account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")) {
            ArrayList<BaseScanner.MediaItem> arrayList = (ArrayList) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA, CleanerContract$Projection.SLIM_SCAN_PROJECTION, getSelection(), (String[]) null, SLIM_SCAN_ORDER, new SafeDBUtil.QueryHandler<ArrayList<BaseScanner.MediaItem>>() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.5
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public ArrayList<BaseScanner.MediaItem> mo1808handle(Cursor cursor) {
                    boolean z;
                    ArrayList<BaseScanner.MediaItem> arrayList2 = null;
                    if (cursor == null || !cursor.moveToFirst()) {
                        return arrayList2;
                    }
                    HashMap hashMap = new HashMap();
                    do {
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList<>();
                        }
                        BaseScanner.MediaItem mediaItem = new BaseScanner.MediaItem();
                        mediaItem.mId = cursor.getLong(0);
                        mediaItem.mSize = cursor.getLong(1);
                        mediaItem.mPath = cursor.getString(2);
                        mediaItem.mWidth = cursor.getInt(3);
                        mediaItem.mHeight = cursor.getInt(4);
                        if (!TextUtils.isEmpty(mediaItem.mPath)) {
                            String str = BaseFileUtils.getParentFolderPath(mediaItem.mPath) + File.separator + "1.jpg";
                            if (hashMap.containsKey(str)) {
                                z = ((Boolean) hashMap.get(str)).booleanValue();
                            } else {
                                boolean checkStoragePermission = SlimScanner.this.checkStoragePermission(str);
                                hashMap.put(str, Boolean.valueOf(checkStoragePermission));
                                z = checkStoragePermission;
                            }
                            if (z) {
                                arrayList2.add(mediaItem);
                            }
                        }
                    } while (cursor.moveToNext());
                    return arrayList2;
                }
            });
            if (!BaseMiscUtil.isValid(arrayList)) {
                return null;
            }
            this.mMediaItems = arrayList;
            this.mHasValidSlimResult = true;
            SlimController.getInstance().registerObserver(this);
            return buildScanResult();
        }
        return scanToSynced();
    }

    public ScanResult scanToSynced() {
        Account account = AccountCache.getAccount();
        boolean z = account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider");
        ArrayList<BaseScanner.MediaItem> arrayList = (ArrayList) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA, CleanerContract$Projection.SLIM_SCAN_PROJECTION, TO_SYNCED_SLIM_SCAN_SELECTION, (String[]) null, SLIM_SCAN_ORDER, new SafeDBUtil.QueryHandler<ArrayList<BaseScanner.MediaItem>>() { // from class: com.miui.gallery.cleaner.slim.SlimScanner.6
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public ArrayList<BaseScanner.MediaItem> mo1808handle(Cursor cursor) {
                boolean z2;
                ArrayList<BaseScanner.MediaItem> arrayList2 = null;
                if (cursor == null || !cursor.moveToFirst()) {
                    return arrayList2;
                }
                HashMap hashMap = new HashMap();
                do {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList<>();
                    }
                    BaseScanner.MediaItem mediaItem = new BaseScanner.MediaItem();
                    mediaItem.mId = cursor.getLong(0);
                    mediaItem.mSize = cursor.getLong(1);
                    mediaItem.mPath = cursor.getString(2);
                    mediaItem.mWidth = cursor.getInt(3);
                    mediaItem.mHeight = cursor.getInt(4);
                    if (!TextUtils.isEmpty(mediaItem.mPath)) {
                        String str = BaseFileUtils.getParentFolderPath(mediaItem.mPath) + File.separator + "1.jpg";
                        if (hashMap.containsKey(str)) {
                            z2 = ((Boolean) hashMap.get(str)).booleanValue();
                        } else {
                            boolean checkStoragePermission = SlimScanner.this.checkStoragePermission(str);
                            hashMap.put(str, Boolean.valueOf(checkStoragePermission));
                            z2 = checkStoragePermission;
                        }
                        if (z2) {
                            arrayList2.add(mediaItem);
                        }
                    }
                } while (cursor.moveToNext());
                return arrayList2;
            }
        });
        if (!BaseMiscUtil.isValid(arrayList)) {
            return null;
        }
        this.mMediaItems = arrayList;
        this.mHasValidSlimResult = false;
        if (!z) {
            if (BaseMiscUtil.isValid(arrayList)) {
                return buildAutoBackupDisableScanResult();
            }
            return null;
        }
        return buildToSyncScanResult();
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public void recordClickScanResultEvent() {
        HashMap hashMap = new HashMap();
        hashMap.put("result", String.valueOf(this.mMediaItems.size()));
        SamplingStatHelper.recordCountEvent("cleaner", "cleaner_result_slim_click", hashMap);
        TrackController.trackClick("403.27.1.1.11314", AutoTracking.getRef());
    }

    public void recordClickScanResultEvent(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("result", String.valueOf(this.mMediaItems.size()));
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        SamplingStatHelper.recordCountEvent("cleaner", "cleaner_result_slim_click", hashMap);
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public String getSelection() {
        return SYNCED_SLIM_SCAN_SELECTION;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:45:0x00d9 -> B:46:0x00da). Please submit an issue!!! */
    @Override // com.miui.gallery.cleaner.BaseScanner
    public ScanResult buildScanResult() {
        int i;
        ScanResult.ResultImage[] resultImageArr;
        int i2 = 0;
        if (!this.mHasValidSlimResult) {
            Account account = AccountCache.getAccount();
            if (account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")) {
                i2 = 1;
            }
            if (i2 == 0) {
                if (BaseMiscUtil.isValid(this.mMediaItems)) {
                    return buildAutoBackupDisableScanResult();
                }
                return null;
            }
            return buildToSyncScanResult();
        }
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();
        ScanResult.ResultImage[] resultImageArr2 = new ScanResult.ResultImage[10];
        synchronized (this.mMediaItemLock) {
            try {
                Iterator<BaseScanner.MediaItem> it = this.mMediaItems.iterator();
                long j = 0;
                while (it.hasNext()) {
                    try {
                        BaseScanner.MediaItem next = it.next();
                        if (Math.max(next.mWidth, next.mHeight) <= screenWidth || TextUtils.isEmpty(next.mPath)) {
                            i = screenWidth;
                            resultImageArr = resultImageArr2;
                        } else {
                            float computeThumbNailScaleSize = BitmapUtils.computeThumbNailScaleSize(next.mWidth, next.mHeight, screenWidth, screenHeight);
                            long j2 = next.mSize;
                            resultImageArr = resultImageArr2;
                            double d = computeThumbNailScaleSize;
                            i = screenWidth;
                            long pow = j2 - ((long) ((j2 * Math.pow(d, 2.0d)) * 0.4000000059604645d));
                            if (pow > 0) {
                                j += pow;
                            } else {
                                screenWidth = i;
                                resultImageArr2 = resultImageArr;
                            }
                        }
                        if (i2 < 10) {
                            resultImageArr[i2] = new ScanResult.ResultImage(next.mId, next.mPath);
                        }
                        i2++;
                        screenWidth = i;
                        resultImageArr2 = resultImageArr;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                return new ScanResult.Builder().setType(this.mType).setSize(j).setImages(resultImageArr2).setCount(i2).setOnScanResultClickListener(this.mOnSlimScanResultClickListener).build();
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public ScanResult buildToSyncScanResult() {
        int i;
        ScanResult.ResultImage[] resultImageArr;
        long j;
        ScanResult.ResultImage[] resultImageArr2;
        long j2;
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();
        ScanResult.ResultImage[] resultImageArr3 = new ScanResult.ResultImage[10];
        synchronized (this.mMediaItemLock) {
            Iterator<BaseScanner.MediaItem> it = this.mMediaItems.iterator();
            long j3 = 0;
            i = 0;
            while (it.hasNext()) {
                BaseScanner.MediaItem next = it.next();
                if (Math.max(next.mWidth, next.mHeight) <= screenWidth || TextUtils.isEmpty(next.mPath)) {
                    resultImageArr2 = resultImageArr3;
                    j2 = j3;
                } else {
                    float computeThumbNailScaleSize = BitmapUtils.computeThumbNailScaleSize(next.mWidth, next.mHeight, screenWidth, screenHeight);
                    resultImageArr2 = resultImageArr3;
                    long j4 = next.mSize;
                    long j5 = j3;
                    long pow = j4 - ((long) ((j4 * Math.pow(computeThumbNailScaleSize, 2.0d)) * 0.4000000059604645d));
                    if (pow > 0) {
                        j2 = j5 + pow;
                    } else {
                        resultImageArr3 = resultImageArr2;
                        j3 = j5;
                    }
                }
                if (i < 10) {
                    resultImageArr2[i] = new ScanResult.ResultImage(next.mId, next.mPath);
                }
                i++;
                resultImageArr3 = resultImageArr2;
                j3 = j2;
            }
            resultImageArr = resultImageArr3;
            j = j3;
        }
        ScanResult build = new ScanResult.Builder().setType(this.mType).setSize(j).setImages(resultImageArr).setCount(i).setOnScanResultClickListener(null).build();
        build.setAction(0);
        build.setDescription(R.string.slim_action_auto_backup_enable_description);
        return build;
    }

    public ScanResult buildAutoBackupDisableScanResult() {
        if (!this.mHasRegister) {
            this.mHasRegister = true;
            SyncStateManager.getInstance().registerSyncStateObserver(GalleryApp.sGetAndroidContext(), this);
        }
        return new ScanResult.Builder().setType(this.mType).setSize(-1L).setImages(null).setCount(0).setOnScanResultClickListener(this.mOnAutoBackupDisableClickListener).build();
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public void onReset() {
        super.onReset();
        SlimController.getInstance().unregisterObserver(this);
        if (this.mHasRegister) {
            this.mHasRegister = false;
            SyncStateManager.getInstance().unregisterSyncStateObserver(GalleryApp.sGetAndroidContext(), this);
        }
        Future future = this.mScanFuture;
        if (future != null) {
            future.cancel();
            this.mScanFuture = null;
        }
        Future future2 = this.mScanToSyncFuture;
        if (future2 != null) {
            future2.cancel();
            this.mScanToSyncFuture = null;
        }
    }

    @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
    public void onSlimProgress(long j, long j2, int i) {
        removeMediaItem(j);
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public void onMediaItemDeleted(long j) {
        removeMediaItem(j);
    }

    public final void removeMediaItem(long j) {
        boolean z;
        synchronized (this.mMediaItemLock) {
            Iterator<BaseScanner.MediaItem> it = this.mMediaItems.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                } else if (it.next().mId == j) {
                    it.remove();
                    z = true;
                    break;
                }
            }
        }
        if (z) {
            updateScanResult(buildScanResult());
        }
    }

    public static void requestUpdateProfile() {
        JSONObject jSONObject;
        if (GalleryPreferences.Sync.getUpdateSlimProfileStatus() != 1) {
            return;
        }
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.e("SlimScanner", "CTA not confirmed");
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.e("SlimScanner", "No network");
        } else {
            Account account = AccountCache.getAccount();
            if (account == null) {
                DefaultLogger.e("SlimScanner", "No account");
                return;
            }
            try {
                JSONObject fromXiaomi = CloudUtils.getFromXiaomi(HostManager.Slim.getUpdateProfileUrl(), null, account, CloudUtils.getExtToken(GalleryApp.sGetAndroidContext(), account), 0, false);
                if (fromXiaomi == null || fromXiaomi.getInt("code") != 0 || (jSONObject = fromXiaomi.getJSONObject("data")) == null || !"ACTIVE".equalsIgnoreCase(jSONObject.getString("user_profile"))) {
                    return;
                }
                GalleryPreferences.Sync.setUpdateSlimProfileStatus(2);
            } catch (Exception e) {
                DefaultLogger.d("SlimScanner", "SlimUpdateProfileRequest error", e);
            }
        }
    }

    @Override // com.miui.gallery.cloud.syncstate.OnSyncStateChangeObserver
    public void onSyncStateChanged(SyncStateInfo syncStateInfo) {
        DefaultLogger.d("SlimScanner", "onSyncStateChanged" + syncStateInfo.getSyncStatus());
        SyncStateManager.getInstance().trackSyncStateChanged(syncStateInfo);
        if (!this.mHasRefreshedUnsync && syncStateInfo.getSyncStatus() != SyncStatus.NO_ACCOUNT && syncStateInfo.getSyncStatus() != SyncStatus.MASTER_SYNC_OFF && syncStateInfo.getSyncStatus() != SyncStatus.SYNC_OFF && syncStateInfo.getSyncStatus() != SyncStatus.CTA_NOT_ALLOW && syncStateInfo.getSyncStatus() != SyncStatus.MI_MOVER_RUNNING) {
            Future future = this.mScanToSyncFuture;
            if (future != null) {
                future.cancel();
            }
            this.mScanToSyncFuture = ThreadManager.getMiscPool().submit(this.mScanToSyncedJob, this.mScanToSyncFutureHandler);
            this.mHasRefreshedUnsync = true;
        } else if (this.mHasRefreshedSync || syncStateInfo.getSyncStatus() != SyncStatus.SYNCED) {
        } else {
            this.mHasRefreshedSync = true;
            Future future2 = this.mScanFuture;
            if (future2 != null) {
                future2.cancel();
            }
            this.mScanFuture = ThreadManager.getMiscPool().submit(this.mScanJob, this.mScanFutureHandler);
            if (!this.mHasRegister) {
                return;
            }
            SyncStateManager.getInstance().unregisterSyncStateObserver(GalleryApp.sGetAndroidContext(), this);
            this.mHasRegister = false;
        }
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public boolean checkStoragePermission(String str) {
        if (!StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE).granted) {
            return false;
        }
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        StringBuilder sb = new StringBuilder();
        sb.append(BaseFileUtils.getParentFolderPath(str));
        sb.append(File.separator);
        sb.append(BaseFileUtils.getFileNameWithoutExtension(str));
        sb.append(".jpg");
        return storageStrategyManager.checkPermission(sb.toString(), IStoragePermissionStrategy.Permission.INSERT).granted;
    }
}
