package com.miui.gallery.assistant.manager.request.param;

import android.text.TextUtils;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.sdk.download.DownloadType;

/* loaded from: classes.dex */
public class AnalyticSceneParam implements RequestParams<PathParam> {
    public boolean mIsDownloadImage;
    public boolean mIsDownloadVideo;
    public boolean mIsQuickCalculateVideo;
    public MediaFeatureItem mMediaFeatureItem;

    public AnalyticSceneParam(MediaFeatureItem mediaFeatureItem, boolean z, boolean z2, boolean z3) {
        this.mMediaFeatureItem = mediaFeatureItem;
        this.mIsDownloadImage = z;
        this.mIsDownloadVideo = z2;
        this.mIsQuickCalculateVideo = z3;
    }

    public MediaFeatureItem getMediaFeatureItem() {
        return this.mMediaFeatureItem;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.assistant.manager.request.param.RequestParams
    /* renamed from: getAlgorithmRequestInputs */
    public PathParam mo564getAlgorithmRequestInputs() {
        MediaFeatureItem mediaFeatureItem = this.mMediaFeatureItem;
        if (mediaFeatureItem != null) {
            String imagePath = mediaFeatureItem.getImagePath();
            if (TextUtils.isEmpty(imagePath)) {
                if (this.mIsDownloadImage && this.mMediaFeatureItem.isImage()) {
                    imagePath = BaseImageTask.getFilePath(this.mMediaFeatureItem, DownloadType.MICRO, true).get();
                } else if (this.mIsDownloadVideo && this.mMediaFeatureItem.isVideo()) {
                    imagePath = BaseImageTask.getFilePath(this.mMediaFeatureItem, DownloadType.ORIGIN, true).get();
                }
            }
            return new PathParam(imagePath, this.mMediaFeatureItem.isVideo());
        }
        return null;
    }

    public boolean isQuickCalculateVideo() {
        return this.mIsQuickCalculateVideo;
    }
}
