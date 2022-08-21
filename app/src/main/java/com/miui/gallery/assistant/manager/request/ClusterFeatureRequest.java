package com.miui.gallery.assistant.manager.request;

import com.miui.gallery.assistant.algorithm.AlgorithmFactroy;
import com.miui.gallery.assistant.algorithm.ClusterAlgorithm;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.param.BgrParams;
import com.miui.gallery.assistant.manager.request.param.ImageFeatureBgrRequestParams;
import com.miui.gallery.assistant.manager.result.ClusterFeatureResult;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class ClusterFeatureRequest extends PixelImageFeatureRequest<ClusterFeatureResult> {
    public ClusterFeatureRequest(AlgorithmRequest.Priority priority, ImageFeatureBgrRequestParams imageFeatureBgrRequestParams) {
        super(priority, imageFeatureBgrRequestParams);
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public ClusterFeatureResult process(BgrParams bgrParams) {
        if (bgrParams == null) {
            DefaultLogger.d(this.TAG, "bgr params is null");
            return new ClusterFeatureResult(3, this.mImageId);
        } else if (!LibraryManager.getInstance().loadLibrary(1004003L)) {
            DefaultLogger.d(this.TAG, "Load library %s failed", "ClusterAlgorithm");
            return new ClusterFeatureResult(2, this.mImageId);
        } else {
            try {
                try {
                    Float[] extractFeature = ((ClusterAlgorithm) AlgorithmFactroy.getAlgorithmByFlag(4)).extractFeature(bgrParams.pix, bgrParams.width, bgrParams.height);
                    if (extractFeature == null) {
                        return new ClusterFeatureResult(1, this.mImageId);
                    }
                    DefaultLogger.d(this.TAG, "ClusterAlgorithm extractFeature success!");
                    return new ClusterFeatureResult(extractFeature, this.mImageId);
                } catch (Exception e) {
                    e.printStackTrace();
                    AlgorithmFactroy.releaseAlgorithmByFlag(4);
                    return new ClusterFeatureResult(7, this.mImageId);
                }
            } finally {
                AlgorithmFactroy.releaseAlgorithmByFlag(4);
            }
        }
    }
}
