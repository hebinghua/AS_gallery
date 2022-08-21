package com.miui.gallery.movie.xmsdk;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Size;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: classes2.dex */
public class XmsMultiThumbnailSequenceViewImpl extends XmsMultiThumbnailSequenceView implements RequestListener<Bitmap> {
    public Map<Integer, Bitmap> mBitmapCache;
    public RequestOptions mDisplayOptions;
    public Size mImageSize;

    public static /* synthetic */ void $r8$lambda$As0xE8yPcM0O3yUa88CQEQPRy_I(XmsMultiThumbnailSequenceViewImpl xmsMultiThumbnailSequenceViewImpl, Object obj, Bitmap bitmap) {
        xmsMultiThumbnailSequenceViewImpl.lambda$onResourceReady$0(obj, bitmap);
    }

    @Override // com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView
    public void nativeCancelIconTask(long j) {
    }

    @Override // com.bumptech.glide.request.RequestListener
    public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
        return false;
    }

    public XmsMultiThumbnailSequenceViewImpl(Context context) {
        super(context);
        this.mDisplayOptions = RequestOptions.skipMemoryCacheOf(false).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo958format(DecodeFormat.PREFER_RGB_565);
        Resources resources = getResources();
        int i = R$dimen.photo_movie_edit_edit_item_width;
        this.mImageSize = new Size((int) resources.getDimension(i), (int) getResources().getDimension(i));
        this.mBitmapCache = new WeakHashMap();
    }

    @Override // com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView
    public Bitmap nativeGetIconFromCache(String str, long j) {
        Bitmap remove = this.mBitmapCache.remove(Integer.valueOf(str.hashCode()));
        Object[] objArr = new Object[4];
        boolean z = false;
        objArr[0] = str;
        objArr[1] = Long.valueOf(j);
        if (remove != null) {
            z = true;
        }
        objArr[2] = Boolean.valueOf(z);
        objArr[3] = Integer.valueOf(this.mBitmapCache.size());
        DefaultLogger.d("XmsMultiThumbnailSequenceViewImpl", "nativeGetIconFromCache %s, %d, success :%b, cached: %d", objArr);
        return remove;
    }

    @Override // com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView
    public long nativeGetIcon(String str, long j) {
        DefaultLogger.d("XmsMultiThumbnailSequenceViewImpl", "nativeGetIcon %s, %d", str, Long.valueOf(j));
        int hashCode = str.hashCode();
        if (!this.mBitmapCache.containsKey(Integer.valueOf(hashCode))) {
            Glide.with(this).mo985asBitmap().mo963load(str).mo946apply((BaseRequestOptions<?>) this.mDisplayOptions).mo959listener(this).submit(this.mImageSize.getWidth(), this.mImageSize.getHeight());
        }
        return hashCode;
    }

    @Override // com.bumptech.glide.request.RequestListener
    public boolean onResourceReady(final Bitmap bitmap, final Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.movie.xmsdk.XmsMultiThumbnailSequenceViewImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                XmsMultiThumbnailSequenceViewImpl.$r8$lambda$As0xE8yPcM0O3yUa88CQEQPRy_I(XmsMultiThumbnailSequenceViewImpl.this, obj, bitmap);
            }
        });
        return true;
    }

    public /* synthetic */ void lambda$onResourceReady$0(Object obj, Bitmap bitmap) {
        if (obj instanceof String) {
            String str = (String) obj;
            this.mBitmapCache.put(Integer.valueOf(str.hashCode()), bitmap);
            DefaultLogger.d("XmsMultiThumbnailSequenceViewImpl", "onLoadingComplete: %s, cached: %d", str, Integer.valueOf(this.mBitmapCache.size()));
            notifyIconArrived();
        }
    }
}
