package com.miui.gallery.cloud.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.DeleteDataUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class CloudPrivacyAgreementDeniedReceiver extends BroadcastReceiver {
    public static AtomicBoolean sRunning = new AtomicBoolean(false);

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        DefaultLogger.w("CloudPrivacyAgreementDeniedReceiver", "cloud privacy denied");
        onCloudPrivacyAgreementDenied(context);
    }

    public static void onCloudPrivacyAgreementDenied(Context context) {
        final Context applicationContext = context.getApplicationContext();
        if (sRunning.compareAndSet(false, true)) {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.cloud.receiver.CloudPrivacyAgreementDeniedReceiver.1
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run  reason: collision with other method in class */
                public Void mo1807run(ThreadPool.JobContext jobContext) {
                    try {
                        if (SyncStateUtil.hasSyncedData(applicationContext)) {
                            DefaultLogger.w("CloudPrivacyAgreementDeniedReceiver", "delete synced data");
                            DeleteDataUtil.delete(applicationContext, 1);
                            StatHelper.recordCountEvent("cloud_sync", "cloud_privacy_agreement_denied");
                        }
                        CloudPrivacyAgreementDeniedReceiver.sRunning.set(false);
                        return null;
                    } catch (Throwable th) {
                        CloudPrivacyAgreementDeniedReceiver.sRunning.set(false);
                        throw th;
                    }
                }
            });
        }
    }
}
