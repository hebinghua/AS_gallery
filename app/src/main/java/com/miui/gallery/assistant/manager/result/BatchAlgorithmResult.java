package com.miui.gallery.assistant.manager.result;

import android.util.SparseArray;
import com.miui.gallery.assistant.model.MediaFeature;

/* loaded from: classes.dex */
public class BatchAlgorithmResult extends ImageFeatureResult {
    public final SparseArray<ImageFeatureResult> mAlgorithmResults;

    public BatchAlgorithmResult(int i, long j) {
        super(i, j);
        this.mAlgorithmResults = new SparseArray<>();
    }

    public void add(int i, ImageFeatureResult imageFeatureResult) {
        this.mAlgorithmResults.put(i, imageFeatureResult);
    }

    @Override // com.miui.gallery.assistant.manager.result.ImageFeatureResult
    public void updateMediaFeature(MediaFeature mediaFeature) {
        if (mediaFeature != null) {
            for (int i = 0; i < this.mAlgorithmResults.size(); i++) {
                ImageFeatureResult valueAt = this.mAlgorithmResults.valueAt(i);
                if (valueAt != null) {
                    valueAt.updateMediaFeature(mediaFeature);
                }
            }
        }
    }
}
