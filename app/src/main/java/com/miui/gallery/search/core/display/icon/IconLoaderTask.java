package com.miui.gallery.search.core.display.icon;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.search.core.Consumer;
import com.miui.gallery.search.core.context.PriorityTaskExecutor;
import com.miui.gallery.util.face.FaceRegionRectF;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class IconLoaderTask extends PriorityTaskExecutor.PriorityTask {
    public final boolean mCacheToDisk;
    public final Consumer<IconLoaderResult> mConsumer;
    public final Context mContext;
    public final Handler mHandler;
    public final boolean mHighAccuracy;
    public final Uri mIconUri;
    public final AtomicBoolean mIsCancelled;
    public final boolean mUseDiskCache;
    public static final String[] PROJECTION = {"file_path", "download_uri", "decode_region_orientation", "decode_region_x", "decode_region_y", "decode_region_w", "decode_region_h"};
    public static final FaceRegionRectF DEFAULT_RECT = new FaceRegionRectF(0.0f, 0.0f, 1.0f, 1.0f, 0);

    /* renamed from: $r8$lambda$9--nEZRBHwb7bGxsHWOjdg0Ziac */
    public static /* synthetic */ void m1320$r8$lambda$9nEZRBHwb7bGxsHWOjdg0Ziac(IconLoaderTask iconLoaderTask, IconLoaderResult iconLoaderResult) {
        iconLoaderTask.lambda$consumeResult$0(iconLoaderResult);
    }

    /* loaded from: classes2.dex */
    public static class IconLoaderResult {
        public Uri downloadUri;
        public FaceRegionRectF facePositionRect;
        public Uri iconUri;
        public boolean isFromUnreliableCache;
        public String localFilePath;

        public IconLoaderResult(Uri uri, String str, Uri uri2) {
            this.facePositionRect = IconLoaderTask.DEFAULT_RECT;
            this.isFromUnreliableCache = false;
            this.iconUri = uri;
            this.localFilePath = str;
            this.downloadUri = uri2;
        }

        public IconLoaderResult(Uri uri, String str, Uri uri2, FaceRegionRectF faceRegionRectF) {
            this(uri, str, uri2);
            if (faceRegionRectF != null) {
                this.facePositionRect = faceRegionRectF;
            }
        }

        public IconLoaderResult(Uri uri, String str, Uri uri2, FaceRegionRectF faceRegionRectF, boolean z) {
            this(uri, str, uri2);
            if (faceRegionRectF != null) {
                this.facePositionRect = faceRegionRectF;
            }
            this.isFromUnreliableCache = z;
        }

        public boolean isValid() {
            return (this.localFilePath == null && this.downloadUri == null) ? false : true;
        }

        public boolean isFromUnreliableCache() {
            return this.isFromUnreliableCache;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof IconLoaderResult)) {
                return false;
            }
            IconLoaderResult iconLoaderResult = (IconLoaderResult) obj;
            return Objects.equals(this.iconUri, iconLoaderResult.iconUri) && Objects.equals(this.localFilePath, iconLoaderResult.localFilePath) && Objects.equals(this.downloadUri, iconLoaderResult.downloadUri) && Objects.equals(this.facePositionRect, iconLoaderResult.facePositionRect);
        }

        public int hashCode() {
            return Objects.hash(this.iconUri, this.localFilePath, this.downloadUri, this.facePositionRect);
        }

        public String toString() {
            return "IconLoaderResult{iconUri=" + this.iconUri + ", localFilePath='" + this.localFilePath + CoreConstants.SINGLE_QUOTE_CHAR + ", downloadUri=" + this.downloadUri + ", facePositionRect=" + this.facePositionRect + ", isFromUnreliableCache=" + this.isFromUnreliableCache + '}';
        }
    }

    public IconLoaderTask(Context context, Uri uri, Consumer<IconLoaderResult> consumer, Handler handler, boolean z, boolean z2, boolean z3) {
        this.mContext = context;
        this.mIconUri = uri;
        this.mConsumer = consumer;
        this.mHandler = handler;
        this.mUseDiskCache = z;
        this.mCacheToDisk = z2;
        this.mHighAccuracy = z3;
        this.mPriority = z ? 1 : 2;
        this.mIsCancelled = new AtomicBoolean(false);
    }

    public void setCancelled() {
        this.mIsCancelled.set(true);
    }

    public boolean isCancelled() {
        return this.mIsCancelled.get();
    }

    public boolean isUseDiskCache() {
        return this.mUseDiskCache;
    }

    /* JADX WARN: Removed duplicated region for block: B:75:0x00c6  */
    @Override // com.miui.gallery.concurrent.ThreadPool.Job
    /* renamed from: run */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Void mo1807run(com.miui.gallery.concurrent.ThreadPool.JobContext r14) {
        /*
            Method dump skipped, instructions count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.search.core.display.icon.IconLoaderTask.mo1807run(com.miui.gallery.concurrent.ThreadPool$JobContext):java.lang.Void");
    }

    public final void consumeResult(final IconLoaderResult iconLoaderResult) {
        Handler handler = this.mHandler;
        if (handler == null) {
            this.mConsumer.consume(iconLoaderResult);
        } else {
            handler.post(new Runnable() { // from class: com.miui.gallery.search.core.display.icon.IconLoaderTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    IconLoaderTask.m1320$r8$lambda$9nEZRBHwb7bGxsHWOjdg0Ziac(IconLoaderTask.this, iconLoaderResult);
                }
            });
        }
    }

    public /* synthetic */ void lambda$consumeResult$0(IconLoaderResult iconLoaderResult) {
        this.mConsumer.consume(iconLoaderResult);
    }
}
