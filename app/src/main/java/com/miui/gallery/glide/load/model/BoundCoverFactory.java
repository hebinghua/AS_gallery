package com.miui.gallery.glide.load.model;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import com.miui.gallery.glide.Utils;
import com.miui.gallery.glide.load.data.BoundCover;
import com.miui.gallery.util.BaseFileMimeUtil;
import java.io.FileNotFoundException;
import java.io.IOException;

/* loaded from: classes2.dex */
public class BoundCoverFactory extends Factory<BoundCover> {
    public static final String[] sXmThumbnailSupportedMimeType = {"image/heif", "image/heic", "image/jpeg"};

    public static boolean isXmThumbnailSupported(String str, String str2) {
        for (String str3 : sXmThumbnailSupportedMimeType) {
            if (TextUtils.equals(str3, str)) {
                return !str2.contains("/Android/data/com.miui.gallery/cache/microthumbnailFile");
            }
        }
        return false;
    }

    public BoundCoverFactory(Context context) {
        super(context, BoundCoverFactory$$ExternalSyntheticLambda0.INSTANCE, BoundCover.class);
    }

    public static /* synthetic */ DataHolder lambda$new$0(GalleryModel galleryModel, int i, int i2, Options options) throws IOException {
        BoundCover boundCover;
        String path = galleryModel.getPath();
        Uri parseUri = Utils.parseUri(path);
        if (parseUri == null || parseUri.getPath() == null) {
            throw new FileNotFoundException("Invalid path: " + path);
        }
        int i3 = -1;
        String parseMimeType = Utils.parseMimeType(parseUri, options);
        if (BaseFileMimeUtil.isMp4MimeType(parseMimeType)) {
            boundCover = (BoundCover) ThumbFetcherManager.request(BoundCover.class, path, 2);
            i3 = 2;
        } else {
            boundCover = null;
        }
        if (boundCover == null && (isXmThumbnailSupported(parseMimeType, path) || TextUtils.isEmpty(parseMimeType))) {
            boundCover = (BoundCover) ThumbFetcherManager.request(BoundCover.class, path, 0);
            i3 = 0;
        }
        if (boundCover == null) {
            return null;
        }
        return new BoundCoverDataHolder(boundCover, i3);
    }
}
