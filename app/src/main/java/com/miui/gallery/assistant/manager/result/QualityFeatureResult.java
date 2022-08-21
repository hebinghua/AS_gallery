package com.miui.gallery.assistant.manager.result;

import com.miui.gallery.assistant.jni.score.QualityScore;
import com.miui.gallery.assistant.model.MediaFeature;

/* loaded from: classes.dex */
public class QualityFeatureResult extends ImageFeatureResult {
    public QualityScore mQualityScore;

    public QualityFeatureResult(QualityScore qualityScore, long j) {
        super(0, j);
        this.mQualityScore = qualityScore;
    }

    public QualityFeatureResult(int i, long j) {
        super(i, j);
    }

    @Override // com.miui.gallery.assistant.manager.result.ImageFeatureResult
    public void updateMediaFeature(MediaFeature mediaFeature) {
        if (mediaFeature != null) {
            mediaFeature.setQualityScore(getQualityScore());
        }
    }

    public QualityScore getQualityScore() {
        return this.mQualityScore;
    }
}
