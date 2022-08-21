package com.miui.gallery.provider.cloudmanager.handleFile;

import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.miui.gallery.provider.cloudmanager.handleFile.FileTaskExecutor;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.service.ServiceBase;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class FileHandleService extends ServiceBase {
    public volatile ServiceHandler mServiceHandler;
    public volatile Looper mServiceLooper;
    public FileTaskExecutor mTaskExecutor = null;
    public boolean mNeedNotifyUri = false;

    @Override // com.miui.gallery.service.ServiceBase
    public int getNotificationId() {
        return 9;
    }

    /* loaded from: classes2.dex */
    public final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Intent intent = (Intent) message.obj;
            String action = intent.getAction();
            long[] longArrayExtra = intent.getLongArrayExtra("ids");
            String stringExtra = intent.getStringExtra("extra_invoker_tag");
            boolean booleanExtra = intent.getBooleanExtra("remark", false);
            if (!BaseMiscUtil.isValid(longArrayExtra)) {
                return;
            }
            FileHandleService.this.handleRecords(longArrayExtra, booleanExtra, stringExtra);
            if ("check_unhandled_media_ids".equals(action)) {
                HashMap hashMap = new HashMap();
                hashMap.put(MiStat.Param.COUNT, String.valueOf(longArrayExtra.length));
                SamplingStatHelper.recordCountEvent("file_handle", "restore_unhandled_file_operations", hashMap);
            }
            FileHandleService.this.stopSelfIfComplete();
        }
    }

    @Override // com.miui.gallery.service.ServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("FileHandleService");
        handlerThread.start();
        this.mServiceLooper = handlerThread.getLooper();
        this.mServiceHandler = new ServiceHandler(this.mServiceLooper);
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        Message obtainMessage = this.mServiceHandler.obtainMessage();
        obtainMessage.arg1 = i2;
        obtainMessage.obj = intent;
        this.mServiceHandler.sendMessage(obtainMessage);
        return 3;
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.mServiceLooper.quit();
        FileTaskExecutor fileTaskExecutor = this.mTaskExecutor;
        if (fileTaskExecutor != null) {
            this.mTaskExecutor = null;
            fileTaskExecutor.shutdown();
        }
    }

    public final void handleRecords(long[] jArr, final boolean z, String str) {
        if (this.mTaskExecutor == null) {
            this.mTaskExecutor = new FileTaskExecutor(this, new FileTaskExecutor.FileHandleListener() { // from class: com.miui.gallery.provider.cloudmanager.handleFile.FileHandleService.1
                @Override // com.miui.gallery.provider.cloudmanager.handleFile.FileTaskExecutor.FileHandleListener
                public void onRecordsHandled(long[] jArr2, long[] jArr3) {
                    if (jArr2 == null || jArr3 == null || jArr2.length != jArr3.length) {
                        DefaultLogger.e("galleryAction_FileHandle_FileHandleService", "Invalid results ids:%s, results:%s", jArr2, jArr3);
                        return;
                    }
                    if (z) {
                        RemarkManager.doneRemarkMediaIds(jArr2);
                    }
                    DefaultLogger.d("galleryAction_FileHandle_FileHandleService", "On receive results [%s]", StringUtils.join(",", jArr3));
                    FileHandleService fileHandleService = FileHandleService.this;
                    fileHandleService.mNeedNotifyUri = FileHandleManager.checkAndNotifyUri(fileHandleService, jArr3);
                }

                @Override // com.miui.gallery.provider.cloudmanager.handleFile.FileTaskExecutor.FileHandleListener
                public void onAllTaskExecuted() {
                    FileHandleService.this.stopSelfIfComplete();
                }

                @Override // com.miui.gallery.provider.cloudmanager.handleFile.FileTaskExecutor.FileHandleListener
                public void onCancel() {
                    FileHandleService.this.stopSelfIfComplete();
                }
            }, str);
        }
        this.mTaskExecutor.submit(jArr);
    }

    public final void stopSelfIfComplete() {
        FileTaskExecutor fileTaskExecutor = this.mTaskExecutor;
        if (fileTaskExecutor == null || fileTaskExecutor.isCompleted()) {
            if (this.mNeedNotifyUri) {
                FileHandleManager.requestSync(this);
            }
            stopSelfSafely();
        }
    }
}
