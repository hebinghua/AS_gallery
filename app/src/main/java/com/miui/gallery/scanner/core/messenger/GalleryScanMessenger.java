package com.miui.gallery.scanner.core.messenger;

import android.content.ContentProviderClient;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.miui.gallery.concurrent.PriorityThreadFactory;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.scanner.core.ScanRequest;
import com.miui.gallery.scanner.provider.GalleryMediaScannerProviderContract;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public class GalleryScanMessenger {
    public ExecutorService mExecutorService = Executors.newSingleThreadExecutor(new PriorityThreadFactory("gallery-scan-messenger", 5));

    /* renamed from: $r8$lambda$ePlp7XScwoVU-yvP1gW7devCWN8 */
    public static /* synthetic */ void m1273$r8$lambda$ePlp7XScwoVUyvP1gW7devCWN8(ScanRequest scanRequest) {
        lambda$submit$0(scanRequest);
    }

    public void submit(final ScanRequest scanRequest) {
        this.mExecutorService.submit(new Runnable() { // from class: com.miui.gallery.scanner.core.messenger.GalleryScanMessenger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GalleryScanMessenger.m1273$r8$lambda$ePlp7XScwoVUyvP1gW7devCWN8(ScanRequest.this);
            }
        });
    }

    public static /* synthetic */ void lambda$submit$0(ScanRequest scanRequest) {
        Context sGetAndroidContext = StaticContext.sGetAndroidContext();
        DefaultLogger.fd("GalleryScanMessenger", "request ID is:" + scanRequest.getSceneCode());
        if (!PermissionUtils.checkStoragePermission(sGetAndroidContext)) {
            DefaultLogger.w("GalleryScanMessenger", "check storage permission failed.");
            return;
        }
        try {
            ContentProviderClient acquireContentProviderClient = sGetAndroidContext.getContentResolver().acquireContentProviderClient(GalleryMediaScannerProviderContract.AUTHORITY_URI);
            Bundle bundle = new Bundle();
            bundle.putParcelable("scan_request", scanRequest);
            if (acquireContentProviderClient != null) {
                acquireContentProviderClient.call("internal_request_scan", null, bundle);
            }
            if (acquireContentProviderClient == null) {
                return;
            }
            acquireContentProviderClient.close();
        } catch (RemoteException e) {
            DefaultLogger.d("GalleryScanMessenger", e.getMessage());
        }
    }
}
