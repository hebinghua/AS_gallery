package com.miui.gallery.assistant.manager.request;

import com.miui.gallery.assistant.algorithm.AlgorithmFactroy;
import com.miui.gallery.assistant.algorithm.SceneFilterAlgorithm;
import com.miui.gallery.assistant.jni.filter.SceneResult;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.param.BgrParams;
import com.miui.gallery.assistant.manager.request.param.ImageFeatureBgrRequestParams;
import com.miui.gallery.assistant.manager.result.SceneFeatureResult;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class SceneFeatureRequest extends PixelImageFeatureRequest<SceneFeatureResult> {
    public SceneFeatureRequest(AlgorithmRequest.Priority priority, ImageFeatureBgrRequestParams imageFeatureBgrRequestParams) {
        super(priority, imageFeatureBgrRequestParams);
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public SceneFeatureResult process(BgrParams bgrParams) {
        if (bgrParams == null) {
            DefaultLogger.d(this.TAG, "bgr params is null");
            return new SceneFeatureResult(3, this.mImageId);
        }
        try {
            if (!LibraryManager.getInstance().loadLibrary(1003L)) {
                DefaultLogger.d(this.TAG, "Load library %s failed", "SceneFilterAlgorithm");
                return new SceneFeatureResult(2, this.mImageId);
            }
            try {
                SceneResult sceneResult = ((SceneFilterAlgorithm) AlgorithmFactroy.getAlgorithmByFlag(2)).getSceneResult(bgrParams.pix, bgrParams.width, bgrParams.height);
                if (sceneResult == null) {
                    return new SceneFeatureResult(1, this.mImageId);
                }
                DefaultLogger.d(this.TAG, "SceneFilterAlgorithm execute success,result: %s", sceneResult);
                return new SceneFeatureResult(sceneResult, this.mImageId);
            } catch (Exception e) {
                e.printStackTrace();
                AlgorithmFactroy.releaseAlgorithmByFlag(2);
                return new SceneFeatureResult(7, this.mImageId);
            }
        } finally {
            AlgorithmFactroy.releaseAlgorithmByFlag(2);
        }
    }
}
