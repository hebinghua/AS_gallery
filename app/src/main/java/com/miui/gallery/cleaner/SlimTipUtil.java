package com.miui.gallery.cleaner;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cleaner.SlimTipUtil;
import com.miui.gallery.cleaner.slim.SlimScanner;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.SlimEntrancesStrategy;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.FileSizeFormatter;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import miui.cloud.util.SyncAutoSettingUtil;

/* loaded from: classes.dex */
public class SlimTipUtil {

    /* loaded from: classes.dex */
    public static class StorageLowDialogFragment extends AlertDialogFragment {
        public final String desc;
        public final String negativeBtnText;
        public final String positiveBtnText;
        public final String title;

        public static /* synthetic */ void $r8$lambda$POxGC4X8r1TU6Oz8veY2gJOJxYw(StorageLowDialogFragment storageLowDialogFragment, DialogInterface dialogInterface, int i) {
            storageLowDialogFragment.lambda$setButtons$0(dialogInterface, i);
        }

        public StorageLowDialogFragment() {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            String string = sGetAndroidContext.getResources().getString(R.string.device_storage_space_low_dialog_title);
            this.title = string;
            String format = String.format(sGetAndroidContext.getResources().getString(R.string.device_storage_space_low_dialog_des), FormatUtil.formatFileSize(sGetAndroidContext, GalleryPreferences.Sync.getSlimableSize()));
            this.desc = format;
            this.positiveBtnText = sGetAndroidContext.getResources().getString(R.string.home_menu_cleaner);
            this.negativeBtnText = sGetAndroidContext.getResources().getString(R.string.cancel);
            setTitle(string);
            setMessage(format);
            setButtons();
        }

        public void show(FragmentManager fragmentManager) {
            if (fragmentManager == null || fragmentManager.isDestroyed()) {
                return;
            }
            GalleryPreferences.Sync.setSlimDialogPoppedUpTimestamp(System.currentTimeMillis());
            setButtons();
            showAllowingStateLoss(fragmentManager, "StorageLowDialogFragment");
            TrackController.trackExpose("403.1.0.1.17364", AutoTracking.getRef());
        }

        public void setButtons() {
            setPositiveButton(this.positiveBtnText, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.cleaner.SlimTipUtil$StorageLowDialogFragment$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SlimTipUtil.StorageLowDialogFragment.$r8$lambda$POxGC4X8r1TU6Oz8veY2gJOJxYw(SlimTipUtil.StorageLowDialogFragment.this, dialogInterface, i);
                }
            });
            setNegativeButton(this.negativeBtnText, SlimTipUtil$StorageLowDialogFragment$$ExternalSyntheticLambda1.INSTANCE);
        }

        public /* synthetic */ void lambda$setButtons$0(DialogInterface dialogInterface, int i) {
            TrackController.trackClick("403.1.0.1.17399", AutoTracking.getRef());
            ActionURIHandler.handleUri(getActivity(), GalleryContract.Common.URI_CLEANER_PAGE);
            SamplingStatHelper.recordCountEvent("home", "home_page_menu_cleaner");
        }

        public static /* synthetic */ void lambda$setButtons$1(DialogInterface dialogInterface, int i) {
            TrackController.trackClick("403.1.0.1.17401", AutoTracking.getRef());
            GalleryPreferences.Sync.setSlimDialogShowCount(GalleryPreferences.Sync.getSlimDialogShowCount() + 1);
        }

        @Override // com.miui.gallery.ui.AlertDialogFragment
        public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            super.setOnDismissListener(onDismissListener);
        }
    }

    /* loaded from: classes.dex */
    public static class CloudInfoQuery implements ThreadPool.Job<SyncStateUtil.CloudSpaceInfo> {
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public SyncStateUtil.CloudSpaceInfo mo1807run(ThreadPool.JobContext jobContext) {
            if (SlimTipUtil.isCloudSpaceStorageCheckable(GalleryApp.sGetAndroidContext())) {
                GalleryPreferences.MiCloud.setCloudSpaceFullTipLastCheckTime(System.currentTimeMillis());
                return SyncStateUtil.getCloudSpaceInfo(GalleryApp.sGetAndroidContext());
            }
            return null;
        }
    }

    public static boolean isCloudSpaceStorageCheckable(Context context) {
        if (context == null) {
            DefaultLogger.d("SlimTipUtil", "activity is null");
            return false;
        } else if (FileSizeFormatter.localeFromContext(context) == null) {
            DefaultLogger.d("SlimTipUtil", "local is null");
            return false;
        } else if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("SlimTipUtil", "cta failed");
            return false;
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.d("SlimTipUtil", "no network");
            return false;
        } else {
            Account account = AccountCache.getAccount();
            if (account == null) {
                DefaultLogger.d("SlimTipUtil", "no account");
                return false;
            }
            if (!(SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider"))) {
                DefaultLogger.d("SlimTipUtil", "sync disable");
                return false;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - GalleryPreferences.MiCloud.getCloudSpaceAlmostFullTipLastPopped() <= 86400000 || currentTimeMillis - GalleryPreferences.MiCloud.getCloudSpaceCompletelyFullTipLastPopped() <= 86400000) {
                DefaultLogger.d("SlimTipUtil", "cloud space full dialog tip popped up in 1 day");
                return false;
            } else if (currentTimeMillis - GalleryPreferences.Sync.getSlimLastScanTimestamp() >= 43200000) {
                return true;
            } else {
                DefaultLogger.d("SlimTipUtil", "slim has been scanned in 12 hours");
                return false;
            }
        }
    }

    public static Boolean isDeviceStorageLow(Context context) {
        double usableSpace = (context.getCacheDir().getUsableSpace() * 100.0d) / context.getCacheDir().getTotalSpace();
        DefaultLogger.d("SlimTipUtil", "isDeviceStorageLow: %.3f%%", Double.valueOf(usableSpace));
        if (usableSpace <= 10.0d) {
            GalleryPreferences.Sync.setDeviceStorageLow(true);
            return Boolean.TRUE;
        }
        GalleryPreferences.Sync.setDeviceStorageLow(false);
        return Boolean.FALSE;
    }

    public static Boolean isDeviceStorageTooLow(Context context) {
        double usableSpace = (context.getCacheDir().getUsableSpace() * 100.0d) / context.getCacheDir().getTotalSpace();
        DefaultLogger.d("SlimTipUtil", "isDeviceStorageTooLow: %.3f%%", Double.valueOf(usableSpace));
        if (usableSpace <= 5.0d) {
            GalleryPreferences.Sync.setDeviceStorageTooLow(true);
            return Boolean.TRUE;
        }
        GalleryPreferences.Sync.setDeviceStorageTooLow(false);
        return Boolean.FALSE;
    }

    public static Boolean isSlimEntranceEnable(String str) {
        SlimEntrancesStrategy slimEntrancesStrategy = CloudControlStrategyHelper.getSlimEntrancesStrategy();
        return Boolean.valueOf(slimEntrancesStrategy.isEnable() && slimEntrancesStrategy.getEntrance(str).isEnable());
    }

    public static Boolean isNeedShowStorageLowTip(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        if (isSlimEntranceEnable("homepage-dialog").booleanValue() && isDeviceStorageTooLow(context).booleanValue()) {
            if (GalleryPreferences.Sync.getSlimDialogShowCount() >= 7) {
                DefaultLogger.d("SlimTipUtil", "slim dialog tip has been shown 7 more times");
                return Boolean.FALSE;
            } else if (currentTimeMillis - GalleryPreferences.Sync.getSlimDialogLastPoppedUpTimestamp() > 259200000 && currentTimeMillis - GalleryPreferences.Sync.getSlimTextLinkLastShowedTimestamp() > 86400000) {
                return Boolean.TRUE;
            }
        } else if (isSlimEntranceEnable("text-link").booleanValue() && isDeviceStorageLow(context).booleanValue() && !isDeviceStorageTooLow(context).booleanValue() && currentTimeMillis - GalleryPreferences.Sync.getSlimTextLinkLastShowedTimestamp() > 259200000 && currentTimeMillis - GalleryPreferences.Sync.getSlimDialogLastPoppedUpTimestamp() > 86400000) {
            return Boolean.TRUE;
        }
        DefaultLogger.d("SlimTipUtil", "won't show storage low dialog");
        return Boolean.FALSE;
    }

    public static void doScan() {
        DefaultLogger.d("SlimTipUtil", "slim scan weekly in background");
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (System.currentTimeMillis() - GalleryPreferences.Sync.getSlimNotificationShowedTimestamp() < CoreConstants.MILLIS_IN_ONE_WEEK) {
            DefaultLogger.d("SlimTipUtil", "slim notification has been shown in 7 days");
        } else if (!isDeviceStorageLow(sGetAndroidContext).booleanValue()) {
            DefaultLogger.d("SlimTipUtil", "device space not low");
        } else {
            SyncStateUtil.CloudSpaceInfo mo1807run = new CloudInfoQuery().mo1807run((ThreadPool.JobContext) null);
            if (mo1807run == null) {
                DefaultLogger.d("SlimTipUtil", "cloudSpaceInfo null");
                return;
            }
            long total = mo1807run.getTotal() - mo1807run.getUsed();
            GalleryPreferences.Sync.setSlimLastScanTimestamp(System.currentTimeMillis());
            ScanResult scan = ((SlimScanner) ScannerManager.getInstance().getScanner(0)).scan();
            if (scan == null) {
                DefaultLogger.d("SlimTipUtil", "scan result null");
                return;
            }
            long size = scan.getSize();
            DefaultLogger.d("SlimTipUtil", "slimSize: %,d, cloudUsableSpace: %,d", Long.valueOf(size), Long.valueOf(total));
            if (size <= 500000000 || size >= total) {
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW", GalleryContract.Common.URI_CLEANER_PAGE);
            intent.putExtra("extra_from_type", 1017);
            PendingIntent activity = PendingIntent.getActivity(sGetAndroidContext, 0, intent, 201326592);
            Notification.Builder builder = new Notification.Builder(sGetAndroidContext);
            builder.setTicker(sGetAndroidContext.getString(R.string.device_storage_space_low_notice_title));
            builder.setContentTitle(sGetAndroidContext.getString(R.string.device_storage_space_low_notice_title));
            builder.setContentText(String.format(sGetAndroidContext.getString(R.string.device_storage_space_low_notice_des), FormatUtil.formatFileSize(sGetAndroidContext, size)));
            builder.setContentIntent(activity);
            builder.setSmallIcon(R.drawable.album_share_invitation_stat);
            NotificationHelper.setDefaultChannel(sGetAndroidContext, builder);
            Notification build = builder.build();
            build.flags = 16;
            ((NotificationManager) sGetAndroidContext.getSystemService("notification")).notify(4, build);
            TrackController.trackExpose("403.70.1.1.16840", AutoTracking.getRef());
        }
    }
}
