package com.miui.gallery.util.photoview;

import android.content.ContentResolver;
import android.net.Uri;
import com.miui.gallery.Config$TileConfig;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ImageType;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class TileBitProviderFactory {
    public static final ImageType[] sSupportParallelTypes = {ImageType.HIGH_RESOLUTION_108M, ImageType.HIGH_RESOLUTION_64M};

    public static TileBitProvider create(ContentResolver contentResolver, Uri uri, String str, int i, int i2, byte[] bArr) {
        TileBitProvider tileBitProviderRegion;
        long currentTimeMillis = System.currentTimeMillis();
        if (BaseFileMimeUtil.isHeifMimeType(str)) {
            tileBitProviderRegion = new TileBitProviderForHeif(contentResolver, uri, bArr, str);
        } else if (supportParallel(ImageType.of(i, i2))) {
            tileBitProviderRegion = new ParallelTileBitProvider(contentResolver, uri, bArr, str, Config$TileConfig.REGION_DECODER_PARALLELISM);
        } else {
            tileBitProviderRegion = new TileBitProviderRegion(contentResolver, uri, bArr, str);
        }
        DefaultLogger.d("TileBitProviderFactory", "create tile provider costs: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return tileBitProviderRegion;
    }

    public static boolean supportParallel(ImageType imageType) {
        for (ImageType imageType2 : sSupportParallelTypes) {
            if (imageType == imageType2) {
                return true;
            }
        }
        return false;
    }
}
