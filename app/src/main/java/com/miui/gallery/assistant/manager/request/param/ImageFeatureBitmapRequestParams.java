package com.miui.gallery.assistant.manager.request.param;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.assistant.ImageUtil;

/* loaded from: classes.dex */
public class ImageFeatureBitmapRequestParams extends ImageFeatureRequestParams<Bitmap> {
    public ImageFeatureBitmapRequestParams(MediaFeatureItem mediaFeatureItem, boolean z) {
        super(mediaFeatureItem, z, true, DownloadType.MICRO);
    }

    @Override // com.miui.gallery.assistant.manager.request.param.RequestParams
    /* renamed from: getAlgorithmRequestInputs */
    public Bitmap mo564getAlgorithmRequestInputs() {
        MediaFeatureItem mediaFeatureItem = this.mMediaFeatureItem;
        if (mediaFeatureItem != null) {
            String imagePath = mediaFeatureItem.getImagePath();
            if (TextUtils.isEmpty(imagePath) && this.mShouldDownloadIfNotExist) {
                imagePath = BaseImageTask.getFilePath(this.mMediaFeatureItem, this.mDownloadType, true).get();
            }
            return ImageUtil.getScaledBitmap(imagePath);
        }
        return null;
    }
}
