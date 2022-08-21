package com.miui.gallery.assistant.manager.request.param;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.assistant.ImageUtil;

/* loaded from: classes.dex */
public class ImageFeatureBgrRequestParams extends ImageFeatureRequestParams<BgrParams> {
    public ImageFeatureBgrRequestParams(MediaFeatureItem mediaFeatureItem) {
        super(mediaFeatureItem);
    }

    public ImageFeatureBgrRequestParams(MediaFeatureItem mediaFeatureItem, boolean z, boolean z2, DownloadType downloadType) {
        super(mediaFeatureItem, z, z2, downloadType);
    }

    @Override // com.miui.gallery.assistant.manager.request.param.RequestParams
    /* renamed from: getAlgorithmRequestInputs */
    public BgrParams mo564getAlgorithmRequestInputs() {
        MediaFeatureItem mediaFeatureItem = this.mMediaFeatureItem;
        if (mediaFeatureItem != null) {
            String imagePath = mediaFeatureItem.getImagePath();
            if (TextUtils.isEmpty(imagePath) && this.mShouldDownloadIfNotExist) {
                imagePath = BaseImageTask.getFilePath(this.mMediaFeatureItem, this.mDownloadType, true).get();
            }
            Bitmap scaledBitmap = ImageUtil.getScaledBitmap(imagePath);
            if (scaledBitmap == null) {
                return null;
            }
            return new BgrParams(ImageUtil.getPixelsBGR(scaledBitmap), scaledBitmap.getWidth(), scaledBitmap.getHeight());
        }
        return null;
    }
}
