package com.miui.gallery.magic.special.effects.video.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import com.miui.gallery.magic.util.MagicLog;
import com.nexstreaming.nexeditorsdk.nexEngine;
import com.xiaomi.milab.videosdk.FrameRetriever;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/* loaded from: classes2.dex */
public class MiVideoFrameLoader {
    public OnFrameCallback mCallback;
    public OnImageLoadedListener mListener;
    public final Map<String, WeakReference<ImageView>> mImageViewForKey = new HashMap();
    public LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(nexEngine.ExportHEVCHighTierLevel6) { // from class: com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader.1
        {
            MiVideoFrameLoader.this = this;
        }

        @Override // android.util.LruCache
        public int sizeOf(String str, Bitmap bitmap) {
            if (bitmap != null) {
                return bitmap.getAllocationByteCount();
            }
            return 0;
        }
    };
    public boolean isMiVideoLoaderRelease = false;
    public Runnable exit = new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader.3
        {
            MiVideoFrameLoader.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.d("ArMn", "---exit---");
            if (MiVideoFrameLoader.this.mFrameRetriever != null) {
                MiVideoFrameLoader.this.mFrameRetriever.release();
            }
            MiVideoFrameLoader.this.isMiVideoLoaderRelease = true;
        }
    };
    public Handler mMainHandler = new Handler(Looper.getMainLooper());
    public FrameRetriever mFrameRetriever = new FrameRetriever();
    public ExecutorService mThreadPoolExecutor = Executors.newFixedThreadPool(1);

    /* loaded from: classes2.dex */
    public interface OnFrameCallback {
        void onAvailableFrame(Bitmap bitmap);
    }

    /* loaded from: classes2.dex */
    public interface OnImageLoadedListener {
        void onImageDisplayed();
    }

    public static /* synthetic */ void $r8$lambda$qzgosdCm56Pr0Q878feZbdln8aw(MiVideoFrameLoader miVideoFrameLoader, Bitmap bitmap, String str) {
        miVideoFrameLoader.lambda$onIconReady$0(bitmap, str);
    }

    public final Bitmap getIconFromCache(String str, long j) {
        return this.mMemoryCache.get(String.format(Locale.US, "%s_%d", str, Long.valueOf(j)));
    }

    public boolean loadImage(ImageView imageView, final String str, final int i, final long j) {
        final String format = String.format(Locale.US, "%s_%d", str, Long.valueOf(j));
        Bitmap iconFromCache = getIconFromCache(str, j);
        if (iconFromCache != null) {
            imageView.setImageBitmap(iconFromCache);
            imageView.setTag(format);
            return true;
        }
        imageView.setImageBitmap(null);
        boolean containsKey = this.mImageViewForKey.containsKey(format);
        this.mImageViewForKey.put(format, new WeakReference<>(imageView));
        imageView.setTag(format);
        if (containsKey) {
            return false;
        }
        this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader.2
            {
                MiVideoFrameLoader.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (!str.equals(MiVideoFrameLoader.this.mFrameRetriever.getDataSource())) {
                    MiVideoFrameLoader.this.mFrameRetriever.release();
                    MiVideoFrameLoader.this.mFrameRetriever = new FrameRetriever();
                    MiVideoFrameLoader.this.mFrameRetriever.setDataSource(str);
                }
                MiVideoFrameLoader.this.mFrameRetriever.setFrameAtTime(j);
                int width = MiVideoFrameLoader.this.mFrameRetriever.getWidth();
                int height = MiVideoFrameLoader.this.mFrameRetriever.getHeight();
                MagicLog magicLog = MagicLog.INSTANCE;
                magicLog.showLog("MagicLogger MiVideoFrameLoader", "videoW " + width + " videoH " + height);
                if (width > 0) {
                    FrameRetriever frameRetriever = MiVideoFrameLoader.this.mFrameRetriever;
                    int i2 = i;
                    frameRetriever.setSize(i2, (height * i2) / width);
                    MiVideoFrameLoader.this.onIconReady(MiVideoFrameLoader.this.mFrameRetriever.getNextFrame(), format);
                }
            }
        });
        return false;
    }

    public void setFirstFrameCallback(OnFrameCallback onFrameCallback) {
        this.mCallback = onFrameCallback;
    }

    /* renamed from: displayImage */
    public final void lambda$onIconReady$0(Bitmap bitmap, String str) {
        this.mMemoryCache.put(str, bitmap);
        WeakReference<ImageView> remove = this.mImageViewForKey.remove(str);
        if (remove == null) {
            Log.w("ArMn", "display image remove key: " + str + " not found. current object status: " + this.isMiVideoLoaderRelease);
            return;
        }
        ImageView imageView = remove.get();
        if (imageView == null || !str.equals(imageView.getTag())) {
            return;
        }
        imageView.setImageBitmap(bitmap);
        OnFrameCallback onFrameCallback = this.mCallback;
        if (onFrameCallback != null) {
            onFrameCallback.onAvailableFrame(bitmap);
        }
        OnImageLoadedListener onImageLoadedListener = this.mListener;
        if (onImageLoadedListener == null) {
            return;
        }
        onImageLoadedListener.onImageDisplayed();
    }

    public void onIconReady(final Bitmap bitmap, final String str) {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.util.MiVideoFrameLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MiVideoFrameLoader.$r8$lambda$qzgosdCm56Pr0Q878feZbdln8aw(MiVideoFrameLoader.this, bitmap, str);
            }
        });
    }

    public void release() {
        this.mCallback = null;
        this.mImageViewForKey.clear();
        this.mMainHandler.removeCallbacks(null);
        this.mMemoryCache.evictAll();
        if (!this.mThreadPoolExecutor.isShutdown()) {
            ExecutorService executorService = this.mThreadPoolExecutor;
            if (executorService instanceof ThreadPoolExecutor) {
                ((ThreadPoolExecutor) executorService).getQueue().clear();
            }
            this.mThreadPoolExecutor.execute(this.exit);
            this.mThreadPoolExecutor.shutdown();
        }
        this.mListener = null;
    }
}
