package com.miui.gallery.startup;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.os.Process;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.startup.Initializer;
import androidx.tracing.Trace;
import ch.qos.logback.core.CoreConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageStartupHelper2;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.cleaner.slim.SlimScanner;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.ClearDataManager;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.MiCloudDownloadFileNetEventStatCallback;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.receiver.CloudPrivacyAgreementDeniedReceiver;
import com.miui.gallery.cloud.syncstate.SyncMonitor;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.dao.GalleryLiteEntityManager;
import com.miui.gallery.data.CitySearcher;
import com.miui.gallery.editor.photo.app.EditorInitializer;
import com.miui.gallery.job.GalleryJobScheduler;
import com.miui.gallery.migrate.StorageMigrationManager;
import com.miui.gallery.miplay.GalleryMiPlayManager;
import com.miui.gallery.monitor.LooperBlockDetector;
import com.miui.gallery.net.hardwareauth.DeviceCredentialManager;
import com.miui.gallery.permission.PermissionIntroductionUtils;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.MemoryPreferenceHelper;
import com.miui.gallery.preference.PreferenceHelper;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleManager;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleRecorder;
import com.miui.gallery.push.GalleryPushManager;
import com.miui.gallery.receiver.LocaleChangedReceiver;
import com.miui.gallery.receiver.TimeChangedReceiver;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.sdk.uploadstatus.ItemType;
import com.miui.gallery.sdk.uploadstatus.SyncProxy;
import com.miui.gallery.sdk.uploadstatus.UploadStatusItem;
import com.miui.gallery.sdk.uploadstatus.UploadStatusProxy;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.MamlUtil;
import com.miui.gallery.util.OldCacheCleaner;
import com.miui.gallery.util.PrivacyAgreementUtils;
import com.miui.gallery.util.ProcessUtils;
import com.miui.gallery.util.ReceiverUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deprecated.BaseDeprecatedPreference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.uil.ShortCutHelper;
import com.xiaomi.micloudsdk.request.utils.Request;
import com.xiaomi.micloudsdk.stat.MiCloudNetEventStatInjector;
import java.io.File;
import java.util.Collections;
import java.util.List;
import miui.cloud.util.DeviceFeatureUtils;
import miuix.net.ConnectivityHelper;

/* loaded from: classes2.dex */
public class GalleryAppInitializer implements Initializer<Void> {
    public static /* synthetic */ void $r8$lambda$RogpUxC80nRCmreEzKhnbVXomBQ(Context context, UploadStatusItem uploadStatusItem) {
        lambda$doInitInSubThreadAtOnce$1(context, uploadStatusItem);
    }

    public static /* synthetic */ void $r8$lambda$trmmkXCAcqqU5p0Jyrl8sH7WL0s(GalleryAppInitializer galleryAppInitializer, Context context) {
        galleryAppInitializer.lambda$initDataInSubThread$0(context);
    }

    @Override // androidx.startup.Initializer
    /* renamed from: create */
    public Void mo2646create(Context context) {
        char c;
        String currentProcessName = ProcessUtils.currentProcessName();
        DefaultLogger.d("GalleryAppInitializer", "initialize for process: %s", currentProcessName);
        int hashCode = currentProcessName.hashCode();
        if (hashCode == -1520738335) {
            if (currentProcessName.equals("com.miui.gallery")) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode != 1435407123) {
            if (hashCode == 1807619342 && currentProcessName.equals("com.miui.gallery:widgetProvider")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (currentProcessName.equals("com.miui.gallery:photo_editor")) {
                c = 0;
            }
            c = 65535;
        }
        if (c == 0) {
            new EditorInitializer().initialize(context);
        } else if (c == 1) {
            new WidgetInitializer().initialize(context);
        } else {
            new DefaultInitializer().initialize(context);
        }
        return null;
    }

    @Override // androidx.startup.Initializer
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }

    /* loaded from: classes2.dex */
    public class DefaultInitializer {
        public DefaultInitializer() {
            GalleryAppInitializer.this = r1;
        }

        public void initialize(Context context) {
            HomePageStartupHelper2.startPreloadHomePageDatasTask(context);
            Trace.beginSection("initDataInUIThread");
            GalleryAppInitializer.this.initDataInUIThread(context);
            Trace.endSection();
            GalleryAppInitializer.this.initDataInSubThread(context);
        }
    }

    public final void initDataInUIThread(Context context) {
        Request.init(context);
        MiCloudNetEventStatInjector.getInstance().initDownloadFile(new MiCloudDownloadFileNetEventStatCallback());
        StorageSolutionProvider.init(context);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(GalleryMiPlayManager.getInstance());
    }

    public final void initDataInSubThread(final Context context) {
        new Thread(new Runnable() { // from class: com.miui.gallery.startup.GalleryAppInitializer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GalleryAppInitializer.$r8$lambda$trmmkXCAcqqU5p0Jyrl8sH7WL0s(GalleryAppInitializer.this, context);
            }
        }, "app-init-thread").start();
    }

    public /* synthetic */ void lambda$initDataInSubThread$0(Context context) {
        try {
            Trace.beginSection("doInitInSubThreadAtOnce");
            doInitInSubThreadAtOnce(context);
            Trace.endSection();
            Process.setThreadPriority(10);
            ThreadManager.sleepThread(500L);
            Trace.beginSection("doInitInSubThreadDelay");
            doInitInSubThreadDelay(context);
            Trace.endSection();
        } catch (Exception e) {
            DefaultLogger.e("GalleryAppInitializer", "initialize error:%s", e);
        }
    }

    public final void doInitInSubThreadAtOnce(final Context context) {
        BaseBuildUtil.isLargeScreenDevice();
        if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
            DefaultLogger.d("GalleryAppInitializer", "init MediaFeatureManager");
        }
        Trace.beginSection("initThumbConfig");
        Config$ThumbConfig.get();
        Trace.endSection();
        Trace.beginSection("isSetExemptMasterSyncAuto");
        if (!GalleryPreferences.Sync.isSetExemptMasterSyncAuto() && DeviceFeatureUtils.hasDeviceFeature("exempt_master_sync_auto")) {
            GalleryPreferences.Sync.setExemptMasterSyncAuto(true);
            Account account = AccountCache.getAccount();
            if (account != null && !ContentResolver.getMasterSyncAutomatically() && BaseGalleryPreferences.CTA.containCTACanConnectNetworkKey()) {
                ContentResolver.setSyncAutomatically(account, "com.miui.gallery.cloud.provider", false);
            }
        }
        Trace.endSection();
        AccountCache.getAccountInfo();
        if (BaseBuildUtil.isLowRamDevice() && PermissionIntroductionUtils.isAlreadyGetExternalStoragePermission(context)) {
            MemoryPreferenceHelper.putBoolean("is_need_check_write_external_storage_permission", false);
        }
        if (BaseBuildUtil.isLowRamDevice()) {
            ThreadManager.sleepThread(300L);
        }
        Trace.beginSection("loadPreferences");
        PreferenceHelper.getPreferences();
        BaseDeprecatedPreference.sGetDefaultPreferences();
        Trace.endSection();
        StorageMigrationManager.migrate(GalleryApp.sGetAndroidContext());
        DefaultLogger.d("GalleryAppInitializer", "Warm up Glide");
        Glide.with(context).mo988load(Integer.valueOf((int) R.drawable.icon_empty_photo)).mo965onlyRetrieveFromCache(true).mo978skipMemoryCache(true).mo950diskCacheStrategy(DiskCacheStrategy.RESOURCE).mo974priority(Priority.IMMEDIATE).preload();
        GalleryMiPlayManager.getInstance().init();
        StatHelper.init(context);
        TrackController.init(context);
        DefaultLogger.d("GalleryAppInitializer", "Warm up snapshot db");
        GalleryLiteEntityManager.getInstance().warmUp();
        CloudControlManager.getInstance().init(context);
        registerTimeChangedReceiver(context);
        registerLocaleChangedReceiver(context);
        ScannerEngine.getInstance().start();
        if (MediaFeatureManager.isImageFeatureCalculationEnable()) {
            MediaFeatureCacheManager.getInstance().init();
        }
        LibraryManager.getInstance().init(context);
        SyncMonitor.getInstance();
        SyncProxy.getInstance().init(context, new CloudUriAdapter());
        SyncProxy.getInstance().getUploadStatusProxy().addUploadStatusChangedListener(new UploadStatusProxy.UploadStatusChangedListener() { // from class: com.miui.gallery.startup.GalleryAppInitializer$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.sdk.uploadstatus.UploadStatusProxy.UploadStatusChangedListener
            public final void onUploadStatusChanged(UploadStatusItem uploadStatusItem) {
                GalleryAppInitializer.$r8$lambda$RogpUxC80nRCmreEzKhnbVXomBQ(context, uploadStatusItem);
            }
        });
        HomePageStartupHelper2.registerSnapshotUpdateListener(context);
        MamlUtil.initMamlAssert(context);
        CardManager.getInstance().initCovers();
        FileHandleRecorder.bindFileHandleRecordInvoker();
        if (new File(StorageUtils.getPathInPrimaryStorage("MIUI/Gallery/.block_monitor")).exists()) {
            openBlockMonitor();
        }
    }

    public static /* synthetic */ void lambda$doInitInSubThreadAtOnce$1(Context context, UploadStatusItem uploadStatusItem) {
        if (uploadStatusItem == null || uploadStatusItem.getItemType() != ItemType.OWNER) {
            return;
        }
        Uri userUri = uploadStatusItem.getUserUri();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sync_status", uploadStatusItem.mStatus.toString());
        context.getContentResolver().update(userUri, contentValues, "_id = ?", new String[]{userUri.getLastPathSegment()});
    }

    public final void doInitInSubThreadDelay(Context context) {
        GalleryJobScheduler.INSTANCE.scheduleAll(context);
        CloudUtils.checkAccount(null, true, null);
        ClearDataManager.getInstance().clearDataBaseIfNeeded(context, AccountCache.getAccount());
        CitySearcher.getInstance().preLoadData();
        if (PermissionUtils.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            OldCacheCleaner.clean();
        }
        if (!GalleryPreferences.Sync.getEverRefillLocalGroupId()) {
            GalleryDBHelper.refillLocalGroupId(GalleryDBHelper.getInstance().getWritableDatabase(), true, false);
            GalleryPreferences.Sync.setEverRefillLocalGroupId();
        }
        if (!GalleryPreferences.Sync.getEverSyncedSystemAlbum()) {
            SyncUtil.requestSync(context, new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(1L).build());
        }
        GalleryPushManager.getInstance().registerPush(context);
        if (BaseGalleryPreferences.CTA.canConnectNetwork()) {
            long currentTimeMillis = System.currentTimeMillis() - GalleryPreferences.CloudControl.getLastRequestSucceedTime();
            if (currentTimeMillis >= 259200000 && ConnectivityHelper.getInstance(context).isUnmeteredNetworkConnected()) {
                new CloudControlRequestHelper(context).execRequestSync();
            } else if (currentTimeMillis >= CoreConstants.MILLIS_IN_ONE_WEEK && ConnectivityHelper.getInstance(context).isNetworkConnected()) {
                new CloudControlRequestHelper(context).execRequestSync();
            }
        }
        if (!PrivacyAgreementUtils.isCloudServiceAgreementEnable(context)) {
            CloudPrivacyAgreementDeniedReceiver.onCloudPrivacyAgreementDenied(context);
        }
        VideoPlayerCompat.prepareVideoPlayerStatus();
        ScreenUtils.noteForceBlackState(context);
        FileHandleManager.checkUnhandledMedias(context);
        DeviceCredentialManager.checkSupportCloudCredential(context);
        PicToPdfHelper.isPicToPdfSupport();
        ExtraPhotoSDK.isDeviceSupportRefocus(context);
        ExtraPhotoSDK.isDeviceSupportMotionPhoto(context);
        SlimScanner.requestUpdateProfile();
        ScreenUtils.isUseScreenSceneMode();
        ShortCutHelper.addRecommendShortcut(context);
    }

    public final void registerTimeChangedReceiver(Context context) {
        ReceiverUtils.registerReceiver(context, new TimeChangedReceiver(), "android.intent.action.TIME_SET", "android.intent.action.DATE_CHANGED", "android.intent.action.TIMEZONE_CHANGED");
    }

    public final void registerLocaleChangedReceiver(Context context) {
        ReceiverUtils.registerReceiver(context, new LocaleChangedReceiver(), "android.intent.action.LOCALE_CHANGED");
    }

    public final void openBlockMonitor() {
        LooperBlockDetector.start(Looper.getMainLooper(), 150L);
    }
}
