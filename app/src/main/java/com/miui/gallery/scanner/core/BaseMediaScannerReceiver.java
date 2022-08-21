package com.miui.gallery.scanner.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ReceiverUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class BaseMediaScannerReceiver extends BroadcastReceiver {
    public static int sRegistrantCount;

    public static /* synthetic */ Void $r8$lambda$r27uGpuPkjYaua_Ydcci1aS27_U(Uri uri, ThreadPool.JobContext jobContext) {
        return lambda$doOnReceive$0(uri, jobContext);
    }

    public static void register(Context context, BaseMediaScannerReceiver baseMediaScannerReceiver) {
        if (baseMediaScannerReceiver != null) {
            sRegistrantCount++;
            ReceiverUtils.registerReceiver(context, baseMediaScannerReceiver, 0, Action.FILE_ATTRIBUTE, baseMediaScannerReceiver.getActions());
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        doOnReceive(context, intent);
    }

    public boolean doOnReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra("com.miui.gallery.extra.trigger_scan", false)) {
            return true;
        }
        String action = intent.getAction();
        final Uri data = intent.getData();
        DefaultLogger.d("BaseMediaScannerReceiver", "Broadcast received, action: [%s], data uri: [%s]", action, data);
        action.hashCode();
        char c = 65535;
        switch (action.hashCode()) {
            case -1514214344:
                if (action.equals("android.intent.action.MEDIA_MOUNTED")) {
                    c = 0;
                    break;
                }
                break;
            case -963871873:
                if (action.equals("android.intent.action.MEDIA_UNMOUNTED")) {
                    c = 1;
                    break;
                }
                break;
            case 2045140818:
                if (action.equals("android.intent.action.MEDIA_BAD_REMOVAL")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                StorageUtils.clearCache();
                ScannerEngine.getInstance().scanPathAsync(data != null ? data.getPath() : null, 7);
                return true;
            case 1:
            case 2:
                ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.scanner.core.BaseMediaScannerReceiver$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public final Object mo1807run(ThreadPool.JobContext jobContext) {
                        return BaseMediaScannerReceiver.$r8$lambda$r27uGpuPkjYaua_Ydcci1aS27_U(data, jobContext);
                    }
                });
                return true;
            default:
                return false;
        }
    }

    public static /* synthetic */ Void lambda$doOnReceive$0(Uri uri, ThreadPool.JobContext jobContext) {
        if (!BaseMiscUtil.isValid(StorageUtils.getMountedVolumePaths(GalleryApp.sGetAndroidContext()))) {
            ScannerEngine.getInstance().cancelRunning();
            DefaultLogger.d("BaseMediaScannerReceiver", "All external storage are unmounted, cancel all running tasks.");
        } else {
            ScannerEngine.getInstance().scanPathAsync(uri != null ? uri.getPath() : null, 7);
        }
        return null;
    }

    public String[] getActions() {
        return new String[]{"android.intent.action.MEDIA_MOUNTED", "android.intent.action.MEDIA_UNMOUNTED", "android.intent.action.MEDIA_BAD_REMOVAL"};
    }
}
