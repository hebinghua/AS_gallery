package com.miui.gallery.util;

import android.content.ContentUris;
import android.net.Uri;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes2.dex */
public class CameraPreviewManager {
    public CameraPreviewParams mCameraPreviewParams;
    public final Object mManagerLock = new Object();
    public HashMap<String, List<CountDownLatch>> mLocks = new HashMap<>();

    public static /* synthetic */ void $r8$lambda$hzd8PB2MGf4eY1Sdg7uSDGo9hUo(CameraPreviewManager cameraPreviewManager, String str) {
        cameraPreviewManager.lambda$invalid$0(str);
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final CameraPreviewManager sInstance = new CameraPreviewManager();
    }

    public static CameraPreviewManager getInstance() {
        return SingletonHolder.sInstance;
    }

    public CameraPreviewParams requestCameraPreviewParams(CountDownLatch countDownLatch, String str) {
        synchronized (this.mManagerLock) {
            if (getCameraPreviewParams(str) != null) {
                return this.mCameraPreviewParams;
            }
            prepareCameraPreview(countDownLatch, str);
            return null;
        }
    }

    public final void prepareCameraPreview(CountDownLatch countDownLatch, String str) {
        List<CountDownLatch> list = this.mLocks.get(str);
        if (list == null) {
            list = new LinkedList<>();
            this.mLocks.put(str, list);
        }
        list.add(countDownLatch);
    }

    public CameraPreviewParams getCameraPreviewParams(String str) {
        synchronized (this.mManagerLock) {
            if (this.mCameraPreviewParams != null && Scheme.ofUri(str) == Scheme.CONTENT) {
                if (Objects.equals(this.mCameraPreviewParams.getUri(), str)) {
                    return this.mCameraPreviewParams;
                } else if (ContentUris.parseId(Uri.parse(str)) != ContentUris.parseId(Uri.parse(this.mCameraPreviewParams.getUri()))) {
                    return null;
                } else {
                    return this.mCameraPreviewParams;
                }
            }
            return null;
        }
    }

    public void onHandleCameraPreviewParams(CameraPreviewParams cameraPreviewParams) {
        synchronized (this.mManagerLock) {
            this.mCameraPreviewParams = cameraPreviewParams;
            List<CountDownLatch> remove = this.mLocks.remove(cameraPreviewParams.getUri());
            if (remove != null) {
                for (CountDownLatch countDownLatch : remove) {
                    countDownLatch.countDown();
                }
                remove.clear();
            }
        }
    }

    public void invalid(final String str) {
        ThreadManager.getWorkHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.util.CameraPreviewManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CameraPreviewManager.$r8$lambda$hzd8PB2MGf4eY1Sdg7uSDGo9hUo(CameraPreviewManager.this, str);
            }
        }, 3000L);
    }

    public /* synthetic */ void lambda$invalid$0(String str) {
        synchronized (this.mManagerLock) {
            if (this.mCameraPreviewParams == null) {
                return;
            }
            if (getCameraPreviewParams(str) != null) {
                DefaultLogger.d("CameraPreviewManager", "invalidate camera preview [%s].", str);
                this.mCameraPreviewParams.invalid();
            }
        }
    }
}
