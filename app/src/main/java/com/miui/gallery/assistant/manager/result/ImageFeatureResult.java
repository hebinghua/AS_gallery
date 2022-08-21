package com.miui.gallery.assistant.manager.result;

import com.miui.gallery.assistant.model.MediaFeature;

/* loaded from: classes.dex */
public abstract class ImageFeatureResult extends AlgorithmResult {
    public final long mImageId;

    public abstract void updateMediaFeature(MediaFeature mediaFeature);

    public ImageFeatureResult(int i, long j) {
        super(i);
        this.mImageId = j;
    }
}
