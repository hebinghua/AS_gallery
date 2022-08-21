package com.miui.gallery.assistant.manager.result;

import com.miui.gallery.assistant.model.MediaFeature;

/* loaded from: classes.dex */
public class ClusterFeatureResult extends ImageFeatureResult {
    public Float[] mClusterFeature;

    public ClusterFeatureResult(Float[] fArr, long j) {
        super(0, j);
        this.mClusterFeature = fArr;
    }

    public ClusterFeatureResult(int i, long j) {
        super(i, j);
    }

    @Override // com.miui.gallery.assistant.manager.result.ImageFeatureResult
    public void updateMediaFeature(MediaFeature mediaFeature) {
        if (mediaFeature != null) {
            mediaFeature.setClusterFeature(getClusterFeature());
        }
    }

    public Float[] getClusterFeature() {
        return this.mClusterFeature;
    }
}
