package com.miui.imagecleanlib;

import android.os.Handler;
import android.os.Looper;
import com.miui.gallery.editor.photo.sdk.CleanScheduler;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StorageUtils;
import com.miui.imagecleanlib.ImageCleanThreadManager;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes3.dex */
public class ImageCleanManager {
    public static volatile ImageCleanManager sInstance;
    public static final Handler uiHandle = new Handler(Looper.getMainLooper());
    public ImageCleanThreadManager imageCleanThreadManager = new ImageCleanThreadManager();

    /* loaded from: classes3.dex */
    public interface CleanProgressListener {
        void onProgress(int i, int i2);
    }

    public static synchronized ImageCleanManager getInstance() {
        ImageCleanManager imageCleanManager;
        synchronized (ImageCleanManager.class) {
            if (sInstance == null) {
                synchronized (ImageCleanManager.class) {
                    if (sInstance == null) {
                        sInstance = new ImageCleanManager();
                    }
                }
            }
            imageCleanManager = sInstance;
        }
        return imageCleanManager;
    }

    public void doCleanList(List<ImageCleanTask> list, CleanImagesListener cleanImagesListener) {
        this.imageCleanThreadManager.enqueueTaskList(new ImageCleanThreadManager.ImageCleanTaskList(list, cleanImagesListener));
    }

    public void doCleanAsyncAndWait(final List<ImageCleanTask> list, final CleanProgressListener cleanProgressListener) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        doCleanList(list, new CleanImagesListener() { // from class: com.miui.imagecleanlib.ImageCleanManager.1
            @Override // com.miui.imagecleanlib.CleanImagesListener
            public void onSuccess(String[] strArr) {
                countDownLatch.countDown();
                CleanScheduler.schedule(StaticContext.sGetAndroidContext(), "ImageCleanManager#clean_securityShare_temp", StorageUtils.getShareTempDirectory());
            }

            @Override // com.miui.imagecleanlib.CleanImagesListener
            public void onProgress(final int i) {
                ImageCleanManager.uiHandle.post(new Runnable() { // from class: com.miui.imagecleanlib.ImageCleanManager.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                        CleanProgressListener cleanProgressListener2 = cleanProgressListener;
                        if (cleanProgressListener2 != null) {
                            cleanProgressListener2.onProgress(i, list.size());
                        }
                    }
                });
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
