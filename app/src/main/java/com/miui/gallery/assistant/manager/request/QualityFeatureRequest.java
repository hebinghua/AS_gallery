package com.miui.gallery.assistant.manager.request;

import com.miui.gallery.assistant.algorithm.AlgorithmFactroy;
import com.miui.gallery.assistant.algorithm.QualityScoreAlgorithm;
import com.miui.gallery.assistant.jni.score.QualityScore;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.param.BgrParams;
import com.miui.gallery.assistant.manager.request.param.ImageFeatureBgrRequestParams;
import com.miui.gallery.assistant.manager.result.QualityFeatureResult;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class QualityFeatureRequest extends PixelImageFeatureRequest<QualityFeatureResult> {
    public QualityFeatureRequest(AlgorithmRequest.Priority priority, ImageFeatureBgrRequestParams imageFeatureBgrRequestParams) {
        super(priority, imageFeatureBgrRequestParams);
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public QualityFeatureResult process(BgrParams bgrParams) {
        if (bgrParams == null) {
            DefaultLogger.d(this.TAG, "bgr params is null");
            return new QualityFeatureResult(3, this.mImageId);
        } else if (!LibraryManager.getInstance().loadLibrary(1002002L)) {
            DefaultLogger.d(this.TAG, "Load library %s failed", "QualityScoreAlgorithm");
            return new QualityFeatureResult(2, this.mImageId);
        } else {
            try {
                try {
                    DefaultLogger.v(this.TAG, "getQualityScore for image %d", Long.valueOf(this.mImageId));
                    QualityScore baseQualityScore = ((QualityScoreAlgorithm) AlgorithmFactroy.getAlgorithmByFlag(1)).getBaseQualityScore(bgrParams.pix, bgrParams.width, bgrParams.height);
                    if (baseQualityScore == null) {
                        return new QualityFeatureResult(1, this.mImageId);
                    }
                    DefaultLogger.v(this.TAG, "execute success for image %d: %s", Long.valueOf(this.mImageId), baseQualityScore);
                    return new QualityFeatureResult(baseQualityScore, this.mImageId);
                } catch (Exception e) {
                    e.printStackTrace();
                    AlgorithmFactroy.releaseAlgorithmByFlag(1);
                    return new QualityFeatureResult(7, this.mImageId);
                }
            } finally {
                AlgorithmFactroy.releaseAlgorithmByFlag(1);
            }
        }
    }
}
