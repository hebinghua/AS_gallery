package com.miui.gallery.vlog.sdk.manager;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import com.xiaomi.milab.videosdk.FrameRetriever;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class MiVideoFrameLoader implements VideoFrameLoader {
    public VideoFrameLoader.OnImageLoadedListener mListener;
    public final Map<String, List<WeakReference<ImageView>>> mImageViewForKey = new HashMap();
    public Set<String> mLoadingKeys = new HashSet();
    public Handler mMainHandler = new Handler(Looper.getMainLooper());
    public FrameRetriever mFrameRetriever = new FrameRetriever();
    public LruCache<String, Bitmap> mMemoryCache = new LruCache<>(50);
    public ExecutorService mThreadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory(), new LogDiscardPolicy());

    public static /* synthetic */ void $r8$lambda$6UyUHwy8LZWxZEByEYoVAfFeIJE(MiVideoFrameLoader miVideoFrameLoader, Bitmap bitmap, String str) {
        miVideoFrameLoader.lambda$onIconReady$0(bitmap, str);
    }

    /* loaded from: classes2.dex */
    public static class LogDiscardPolicy extends ThreadPoolExecutor.DiscardPolicy {
        public LogDiscardPolicy() {
        }

        @Override // java.util.concurrent.ThreadPoolExecutor.DiscardPolicy, java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            super.rejectedExecution(runnable, threadPoolExecutor);
            DefaultLogger.e("MiVideoFrameLoader", "rejectedExecution ");
        }
    }

    public final Bitmap getIconFromCache(String str, long j) {
        return this.mMemoryCache.get(String.format(Locale.US, "%s_%d", str, Long.valueOf(j)));
    }

    public final Bitmap getFrameFromCache(String str, long j, int i, int i2) {
        return this.mMemoryCache.get(getKey(str, j, i, i2));
    }

    public final String getKey(String str, long j, int i, int i2) {
        return String.format(Locale.US, "%s_%d_%d_%d", str, Long.valueOf(j), Integer.valueOf(i), Integer.valueOf(i2));
    }

    public final Bitmap getFirstFrame(String str, int i, int i2, boolean z, VideoFrameLoader.LoadListener loadListener) {
        Bitmap bitmap = this.mMemoryCache.get(getKey(str, 0L, i, i2));
        if (bitmap != null) {
            return bitmap;
        }
        loadFrame(str, 0L, i, i2, z, loadListener);
        return null;
    }

    public final boolean loadFrame(final String str, final long j, final int i, final int i2, final boolean z, final VideoFrameLoader.LoadListener loadListener) {
        final String key = getKey(str, j, i, i2);
        if (this.mLoadingKeys.contains(key)) {
            return true;
        }
        this.mLoadingKeys.add(key);
        this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.miui.gallery.vlog.sdk.manager.MiVideoFrameLoader.1
            {
                MiVideoFrameLoader.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                synchronized (MiVideoFrameLoader.this) {
                    if (MiVideoFrameLoader.this.mFrameRetriever != null && !MiVideoFrameLoader.this.mThreadPoolExecutor.isShutdown()) {
                        MiVideoFrameLoader.this.checkPath(str);
                        long currentTimeMillis = System.currentTimeMillis();
                        final Bitmap doExtractFrame = MiVideoFrameLoader.this.doExtractFrame(j, i, i2, z);
                        DefaultLogger.d("MiVideoFrameLoader", "loadFrameTime key=%s,time=%d", key, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                        MiVideoFrameLoader.this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.sdk.manager.MiVideoFrameLoader.1.1
                            {
                                AnonymousClass1.this = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                MiVideoFrameLoader.this.mMemoryCache.put(key, doExtractFrame);
                                MiVideoFrameLoader.this.mLoadingKeys.remove(key);
                                VideoFrameLoader.LoadListener loadListener2 = loadListener;
                                if (loadListener2 != null) {
                                    loadListener2.onLoadFinished(doExtractFrame);
                                }
                            }
                        });
                    }
                }
            }
        });
        return false;
    }

    public final Bitmap doExtractFrame(long j, int i, int i2, boolean z) {
        this.mFrameRetriever.setAccurate(z);
        this.mFrameRetriever.setFrameAtTime(j / 1000);
        this.mFrameRetriever.setSize(i, i2);
        return this.mFrameRetriever.getNextFrame();
    }

    public final void checkPath(String str) {
        if (!str.equals(this.mFrameRetriever.getDataSource())) {
            this.mFrameRetriever.release();
            FrameRetriever frameRetriever = new FrameRetriever();
            this.mFrameRetriever = frameRetriever;
            frameRetriever.setDataSource(str);
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader
    public boolean loadImage(ImageView imageView, String str, int i, long j) {
        return loadImage(imageView, str, i, j, null);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader
    public boolean loadImage(ImageView imageView, final String str, final int i, final long j, Bitmap bitmap) {
        List<WeakReference<ImageView>> list;
        boolean z = true;
        final String format = String.format(Locale.US, "%s_%d", str, Long.valueOf(j));
        Bitmap iconFromCache = getIconFromCache(str, j);
        if (iconFromCache != null) {
            imageView.setImageBitmap(iconFromCache);
            imageView.setTag(format);
            return true;
        }
        imageView.setImageBitmap(bitmap);
        List<WeakReference<ImageView>> list2 = this.mImageViewForKey.get(format);
        if (list2 == null) {
            list2 = new ArrayList<>();
            this.mImageViewForKey.put(format, list2);
            z = false;
        }
        list2.add(new WeakReference<>(imageView));
        Object tag = imageView.getTag();
        imageView.setTag(format);
        if (tag != null && (tag instanceof String) && !format.equals(tag) && (list = this.mImageViewForKey.get(tag)) != null) {
            Iterator<WeakReference<ImageView>> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                WeakReference<ImageView> next = it.next();
                if (next.get() == imageView) {
                    list.remove(next);
                    break;
                }
            }
            if (list.isEmpty()) {
                this.mImageViewForKey.remove(tag);
            }
        }
        if (z) {
            return false;
        }
        this.mThreadPoolExecutor.execute(new Runnable() { // from class: com.miui.gallery.vlog.sdk.manager.MiVideoFrameLoader.2
            {
                MiVideoFrameLoader.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (!MiVideoFrameLoader.this.mImageViewForKey.containsKey(format)) {
                    DefaultLogger.w("MiVideoFrameLoader", "ignore " + format);
                    return;
                }
                synchronized (MiVideoFrameLoader.this) {
                    if (MiVideoFrameLoader.this.mFrameRetriever != null && !MiVideoFrameLoader.this.mThreadPoolExecutor.isShutdown()) {
                        if (!str.equals(MiVideoFrameLoader.this.mFrameRetriever.getDataSource())) {
                            MiVideoFrameLoader.this.mFrameRetriever.release();
                            MiVideoFrameLoader.this.mFrameRetriever = new FrameRetriever();
                            MiVideoFrameLoader.this.mFrameRetriever.setDataSource(str);
                        }
                        MiVideoFrameLoader.this.mFrameRetriever.setAccurate(false);
                        MiVideoFrameLoader.this.mFrameRetriever.setFrameAtTime(j / 1000);
                        int width = MiVideoFrameLoader.this.mFrameRetriever.getWidth();
                        int height = MiVideoFrameLoader.this.mFrameRetriever.getHeight();
                        FrameRetriever frameRetriever = MiVideoFrameLoader.this.mFrameRetriever;
                        int i2 = i;
                        frameRetriever.setSize(i2, (height * i2) / width);
                        MiVideoFrameLoader.this.onIconReady(MiVideoFrameLoader.this.mFrameRetriever.getNextFrame(), format);
                    }
                }
            }
        });
        return false;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader
    public boolean loadFromCache(ImageView imageView, String str, int i, long j) {
        Bitmap iconFromCache = getIconFromCache(str, j);
        if (iconFromCache != null) {
            imageView.setImageBitmap(iconFromCache);
            return true;
        }
        return false;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader
    public void setListener(VideoFrameLoader.OnImageLoadedListener onImageLoadedListener) {
        this.mListener = onImageLoadedListener;
    }

    /* renamed from: displayImage */
    public final void lambda$onIconReady$0(Bitmap bitmap, String str) {
        this.mMemoryCache.put(str, bitmap);
        List<WeakReference<ImageView>> remove = this.mImageViewForKey.remove(str);
        if (remove != null) {
            for (WeakReference<ImageView> weakReference : remove) {
                ImageView imageView = weakReference.get();
                if (imageView != null && str.equals(imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                    VideoFrameLoader.OnImageLoadedListener onImageLoadedListener = this.mListener;
                    if (onImageLoadedListener != null) {
                        onImageLoadedListener.onImageDisplayed();
                    }
                }
            }
        }
    }

    public void onIconReady(final Bitmap bitmap, final String str) {
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.sdk.manager.MiVideoFrameLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MiVideoFrameLoader.$r8$lambda$6UyUHwy8LZWxZEByEYoVAfFeIJE(MiVideoFrameLoader.this, bitmap, str);
            }
        });
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader
    public void release() {
        this.mImageViewForKey.clear();
        this.mMainHandler.removeCallbacks(null);
        this.mMemoryCache.evictAll();
        this.mThreadPoolExecutor.shutdown();
        synchronized (this) {
            FrameRetriever frameRetriever = this.mFrameRetriever;
            if (frameRetriever != null) {
                frameRetriever.release();
                this.mFrameRetriever = null;
            }
        }
        this.mListener = null;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader
    public Bitmap extractFrame(String str, long j, int i, int i2, boolean z, VideoFrameLoader.LoadListener loadListener) {
        Bitmap frameFromCache = getFrameFromCache(str, j, i, i2);
        if (frameFromCache != null) {
            return frameFromCache;
        }
        if (j == 0) {
            return getFirstFrame(str, i, i2, z, loadListener);
        }
        loadFrame(str, j, i, i2, z, loadListener);
        return getFirstFrame(str, i, i2, z, loadListener);
    }
}
