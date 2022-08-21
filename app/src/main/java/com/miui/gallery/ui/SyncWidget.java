package com.miui.gallery.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.activity.BackupDetailActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.cloud.syncstate.SyncStateInfo;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.cloud.syncstate.SyncStatus;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.error.core.ErrorTip;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.ui.DownloadManager;
import com.miui.gallery.ui.SyncDownloadManager;
import com.miui.gallery.ui.SyncManager;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.IntentUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public abstract class SyncWidget extends SyncDownloadManager.HomePageWidget implements SyncManager.SyncStatusListener, DownloadManager.DownloadStatusUpdateListener {
    public static LinkedList<SyncStatus> sDownloadOverlayStatus;
    public boolean isModeChange;
    public View.OnClickListener mDownloadClickListener;
    public DownloadManager mDownloadManager;
    public boolean mIsShowSync;
    public SyncClickListener mSyncClickListener;
    public SyncManager mSyncManager;

    static {
        LinkedList<SyncStatus> linkedList = new LinkedList<>();
        sDownloadOverlayStatus = linkedList;
        linkedList.add(SyncStatus.MASTER_SYNC_OFF);
        sDownloadOverlayStatus.add(SyncStatus.SYNC_OFF);
        sDownloadOverlayStatus.add(SyncStatus.SYNCED);
    }

    public SyncWidget(Context context) {
        super(context);
    }

    public void setManager(SyncManager syncManager, DownloadManager downloadManager) {
        this.mSyncManager = syncManager;
        this.mDownloadManager = downloadManager;
    }

    public void refreshByStatus() {
        this.isModeChange = true;
        if (this.mIsShowSync) {
            onSyncStatusChange(this.mSyncManager.getCurrentSyncStateInfo());
        } else {
            onDownloadStatusUpdate(this.mDownloadManager.getDownloadState(), this.mDownloadManager.getSuccessSize(), this.mDownloadManager.getTotalSize(), this.mDownloadManager.getErrorTip());
        }
        this.isModeChange = false;
    }

    public void statSyncBarClickEvent(String str, HashMap<String, String> hashMap) {
        SamplingStatHelper.recordCountEvent("home_sync_bar", String.format(Locale.US, "sync_bar_click_%s", str), hashMap);
    }

    public void trackSyncClickEvent(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.1.4.1.9885");
        hashMap.put("ref_tip", "403.1.2.1.9881");
        hashMap.put("status", str);
        TrackController.trackClick(hashMap);
    }

    public void statClickWhenDownloading() {
        SamplingStatHelper.recordCountEvent("Sync", "sync_auto_download_click_when_downloading");
    }

    /* loaded from: classes2.dex */
    public class DownloadClickListener implements View.OnClickListener {
        public DownloadClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            AlertDialogFragment.Builder builder;
            Resources resources = SyncWidget.this.getContext().getResources();
            int downloadState = SyncWidget.this.mDownloadManager.getDownloadState();
            if (downloadState == 1) {
                SyncWidget.this.statClickWhenDownloading();
                builder = new AlertDialogFragment.Builder().setTitle(resources.getString(R.string.downloading_title_file)).setMessage(resources.getString(R.string.downloading_dialog_message)).setPositiveButton(resources.getString(R.string.ok), null);
            } else {
                builder = null;
            }
            if (downloadState == 3) {
                final ErrorTip errorTip = SyncWidget.this.mDownloadManager.getErrorHandler().getErrorTip();
                if (errorTip == null) {
                    return;
                }
                if (AnonymousClass1.$SwitchMap$com$miui$gallery$error$core$ErrorCode[errorTip.getCode().ordinal()] == 1) {
                    errorTip.action(SyncWidget.this.getContext(), null);
                    return;
                }
                builder = new AlertDialogFragment.Builder().setMessage(errorTip.getMessage(SyncWidget.this.getContext())).setPositiveButton(errorTip.getActionStr(SyncWidget.this.getContext()), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.SyncWidget.DownloadClickListener.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        errorTip.action(SyncWidget.this.getContext(), null);
                    }
                }).setNegativeButton(resources.getString(R.string.cancel_download_btn_text), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.SyncWidget.DownloadClickListener.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GalleryPreferences.Sync.setAutoDownload(false);
                        BatchDownloadManager.getInstance().stopBatchDownload(SyncWidget.this.getContext());
                    }
                });
            }
            if (builder == null || !(SyncWidget.this.getContext() instanceof FragmentActivity)) {
                return;
            }
            builder.create().showAllowingStateLoss(((FragmentActivity) SyncWidget.this.getContext()).getSupportFragmentManager(), "download");
        }
    }

    /* loaded from: classes2.dex */
    public class SyncClickListener implements View.OnClickListener {
        public boolean autoShow;

        public SyncClickListener() {
        }

        public void setAutoShow(boolean z) {
            this.autoShow = z;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Context context = SyncWidget.this.getContext();
            SyncStateInfo syncState = SyncStateManager.getInstance().getSyncState();
            SyncStatus syncStatus = syncState.getSyncStatus();
            HashMap<String, String> hashMap = new HashMap<>();
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[syncStatus.ordinal()];
            if (i == 1) {
                hashMap.put(CallMethod.ARG_EXTRA_STRING, String.valueOf(SyncWidget.this.mSyncManager.isPermanent()));
                if (SyncWidget.this.getContext() instanceof FragmentActivity) {
                    MergeDataDialogFragment.newInstance(true).showAllowingStateLoss(((FragmentActivity) SyncWidget.this.getContext()).getSupportFragmentManager(), "MergeDataDialogFragment");
                } else {
                    IntentUtil.guideToLoginXiaomiAccount(context, GalleryIntent$CloudGuideSource.TOPBAR);
                }
            } else if (i == 2 || i == 3) {
                IntentUtil.gotoTurnOnSyncSwitchInner(context);
            } else if (i == 4) {
                List<Exception> expectedExceptions = syncState.getExpectedExceptions();
                if (expectedExceptions.size() > 0 && (expectedExceptions.get(0) instanceof StoragePermissionMissingException)) {
                    IntentUtil.gotoAlbumPermissionActivity(context);
                }
            } else {
                context.startActivity(new Intent(context, BackupDetailActivity.class));
            }
            SyncWidget.this.statSyncBarClickEvent(syncStatus.name(), hashMap);
            SyncWidget.this.trackSyncClickEvent(syncStatus.name());
        }
    }

    /* renamed from: com.miui.gallery.ui.SyncWidget$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$error$core$ErrorCode;

        static {
            int[] iArr = new int[SyncStatus.values().length];
            $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus = iArr;
            try {
                iArr[SyncStatus.NO_ACCOUNT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.MASTER_SYNC_OFF.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNC_OFF.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.EXCEPTED_ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[ErrorCode.values().length];
            $SwitchMap$com$miui$gallery$error$core$ErrorCode = iArr2;
            try {
                iArr2[ErrorCode.STORAGE_NO_WRITE_PERMISSION.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class TrackStatusType {
        public static HashMap<SyncStatus, String> trackStatusType;

        static {
            HashMap<SyncStatus, String> hashMap = new HashMap<>();
            trackStatusType = hashMap;
            hashMap.put(SyncStatus.SYNCING, "syncing");
            trackStatusType.put(SyncStatus.SYNCED, "sync success");
            trackStatusType.put(SyncStatus.SYNC_PAUSE, "sync pause");
            trackStatusType.put(SyncStatus.SYNCED_WITH_OVERSIZED_FILE, "file large");
            trackStatusType.put(SyncStatus.CTA_NOT_ALLOW, "cta");
            trackStatusType.put(SyncStatus.MI_MOVER_RUNNING, "mi mover running");
            trackStatusType.put(SyncStatus.DISCONNECTED, "no network");
            trackStatusType.put(SyncStatus.NO_WIFI, "no WiFi");
            trackStatusType.put(SyncStatus.BATTERY_LOW, "battery low");
            trackStatusType.put(SyncStatus.SYSTEM_SPACE_LOW, "keep fit");
            trackStatusType.put(SyncStatus.CLOUD_SPACE_FULL, "cloud full");
        }

        public static void trackExpose(SyncStatus syncStatus, boolean z) {
            String str = trackStatusType.get(syncStatus);
            if (str != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("tip", "403.1.10.1.16307");
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
                hashMap.put("status", z ? "auto" : "manual");
                TrackController.trackExpose(hashMap);
            }
        }

        public static void trackClick(SyncStatus syncStatus, boolean z) {
            String str = trackStatusType.get(syncStatus);
            if (str != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("tip", "403.1.10.1.16305");
                hashMap.put("ref_tip", "403.1.2.1.9881");
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
                hashMap.put("status", z ? "auto" : "manual");
                TrackController.trackClick(hashMap);
            }
        }
    }
}
