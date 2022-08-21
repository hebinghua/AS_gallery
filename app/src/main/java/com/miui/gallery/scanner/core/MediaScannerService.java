package com.miui.gallery.scanner.core;

import android.app.Notification;
import android.content.Intent;
import com.miui.gallery.scanner.core.task.convertor.ScanRequestConverter;
import com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager;
import com.miui.gallery.scanner.core.task.manager.RawScanTaskManager;
import com.miui.gallery.scanner.core.task.raw.RawScanTask;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.service.ServiceBase;
import com.miui.gallery.threadpool.PriorityTaskManager;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.List;

/* loaded from: classes2.dex */
public class MediaScannerService extends ServiceBase {
    public DelayStopSelfManager mDelayStopSelfManager;
    public BaseScanTaskManager<RawScanTask> mTaskManager;

    public static /* synthetic */ void $r8$lambda$HVhmxIccV7E58sLSDfUCRRrPifI(MediaScannerService mediaScannerService) {
        mediaScannerService.lambda$onCreate$0();
    }

    @Override // com.miui.gallery.service.ServiceBase
    public int getNotificationId() {
        return 8;
    }

    @Override // com.miui.gallery.service.ServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mTaskManager = new RawScanTaskManager(2, new PriorityTaskManager.OnAllTasksExecutedListener() { // from class: com.miui.gallery.scanner.core.MediaScannerService$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.threadpool.PriorityTaskManager.OnAllTasksExecutedListener
            public final void onAllTasksExecuted() {
                MediaScannerService.$r8$lambda$HVhmxIccV7E58sLSDfUCRRrPifI(MediaScannerService.this);
            }
        });
        this.mDelayStopSelfManager = new DelayStopSelfManager();
    }

    public /* synthetic */ void lambda$onCreate$0() {
        this.mDelayStopSelfManager.delayStop(this);
        DefaultLogger.d(getTag(), "onAllTasksExecuted, will be stopped in %d ms.", (Object) 4000L);
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        if (intent == null || intent.getExtras() == null) {
            return 2;
        }
        this.mDelayStopSelfManager.cancelDelayStop();
        if (intent.getStringExtra("key_external_scan_request") != null) {
            long longExtra = intent.getLongExtra("key_record_id", -1L);
            String stringExtra = intent.getStringExtra("key_external_scan_request");
            String stringExtra2 = intent.getStringExtra("key_calling_package_name");
            String stringExtra3 = intent.getStringExtra("key_operator_package_name");
            int intExtra = intent.getIntExtra("key_parallel_process_state", 0);
            long longExtra2 = intent.getLongExtra("key_media_store_id", -1L);
            boolean booleanExtra = intent.getBooleanExtra("key_using_gaussian", false);
            this.mTaskManager.submit(ScanRequestConverter.convertToExternalScanTask(getBaseContext(), (ScanContracts$Mode) intent.getSerializableExtra("key_mode"), longExtra, longExtra2, stringExtra, booleanExtra, stringExtra2, stringExtra3, intExtra));
        }
        if (intent.getParcelableExtra("key_internal_scan_request") != null) {
            this.mTaskManager.submit(ScanRequestConverter.convertToInternalScanTask(getBaseContext(), (ScanContracts$Mode) intent.getSerializableExtra("key_mode"), (ScanRequest) intent.getParcelableExtra("key_internal_scan_request")));
        }
        if (!intent.getBooleanExtra("key_mi_mover_request", false)) {
            return 3;
        }
        String stringExtra4 = intent.getStringExtra("key_mi_mover_request_scan_list");
        this.mTaskManager.submit(ScanRequestConverter.convertToMiMoverRawTask(getBaseContext(), (ScanContracts$Mode) intent.getSerializableExtra("key_mode"), (List) ScanCache.getInstance().remove(stringExtra4)));
        return 3;
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public void onDestroy() {
        this.mTaskManager.shutdown();
        super.onDestroy();
    }

    @Override // android.app.Service
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mTaskManager.dump(printWriter);
    }

    /* loaded from: classes2.dex */
    public static class DelayStopSelfManager {
        public DelayStopSelfRunnable mDelayStopSelfRunnable;
        public final Object mLock;

        public DelayStopSelfManager() {
            this.mLock = new Object();
        }

        public void delayStop(MediaScannerService mediaScannerService) {
            synchronized (this.mLock) {
                this.mDelayStopSelfRunnable = new DelayStopSelfRunnable(mediaScannerService);
                ThreadManager.getMainHandler().postDelayed(this.mDelayStopSelfRunnable, 4000L);
            }
        }

        public void cancelDelayStop() {
            synchronized (this.mLock) {
                DelayStopSelfRunnable delayStopSelfRunnable = this.mDelayStopSelfRunnable;
                if (delayStopSelfRunnable != null) {
                    DefaultLogger.d(delayStopSelfRunnable.getTag(), "cancel stop self runnable since a new task comes.");
                    ThreadManager.getMainHandler().removeCallbacks(this.mDelayStopSelfRunnable);
                    this.mDelayStopSelfRunnable = null;
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class DelayStopSelfRunnable implements Runnable {
        public final WeakReference<MediaScannerService> mRef;
        public final String mTag;

        public DelayStopSelfRunnable(MediaScannerService mediaScannerService) {
            this.mRef = new WeakReference<>(mediaScannerService);
            this.mTag = mediaScannerService.getTag();
        }

        @Override // java.lang.Runnable
        public void run() {
            MediaScannerService mediaScannerService = this.mRef.get();
            if (mediaScannerService == null) {
                return;
            }
            mediaScannerService.stopSelfSafely();
        }

        public String getTag() {
            return this.mTag;
        }
    }
}
