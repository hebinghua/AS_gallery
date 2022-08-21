package com.miui.gallery.util.photoview;

import android.content.ContentResolver;
import android.graphics.Rect;
import android.net.Uri;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.HeifUtil;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class TileBitProviderForHeif extends TileBitProviderRegion {
    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public /* bridge */ /* synthetic */ int getImageHeight() {
        return super.getImageHeight();
    }

    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public /* bridge */ /* synthetic */ String getImageMimeType() {
        return super.getImageMimeType();
    }

    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public /* bridge */ /* synthetic */ int getImageWidth() {
        return super.getImageWidth();
    }

    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public /* bridge */ /* synthetic */ int getParallelism() {
        return super.getParallelism();
    }

    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public /* bridge */ /* synthetic */ int getRotation() {
        return super.getRotation();
    }

    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public /* bridge */ /* synthetic */ TileBit getTileBit(Rect rect, int i) {
        return super.getTileBit(rect, i);
    }

    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public /* bridge */ /* synthetic */ boolean isFlip() {
        return super.isFlip();
    }

    public TileBitProviderForHeif(ContentResolver contentResolver, Uri uri, byte[] bArr, String str) {
        super(contentResolver, uri, bArr, str);
    }

    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public ThreadPool customDecodePool() {
        return ThreadManager.getDecodePoolForHeif();
    }

    @Override // com.miui.gallery.util.photoview.TileBitProviderRegion, com.miui.gallery.util.photoview.TileBitProvider
    public void release() {
        super.release();
        HeifUtil.releaseMemoryHeap();
    }
}
