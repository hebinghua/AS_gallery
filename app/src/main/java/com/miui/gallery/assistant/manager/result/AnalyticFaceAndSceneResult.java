package com.miui.gallery.assistant.manager.result;

import com.miui.gallery.assistant.model.FaceClusterInfo;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import java.util.List;

/* loaded from: classes.dex */
public class AnalyticFaceAndSceneResult extends AlgorithmResult {
    public List<FaceClusterInfo> mClusterInfo;
    public List<MediaFeatureItem> mFaceResult;
    public List<Integer> mSceneUpdate;

    public AnalyticFaceAndSceneResult(int i) {
        super(i);
    }

    public AnalyticFaceAndSceneResult(List<MediaFeatureItem> list, List<Integer> list2, List<FaceClusterInfo> list3) {
        super(0);
        this.mFaceResult = list;
        this.mClusterInfo = list3;
        this.mSceneUpdate = list2;
    }

    public List<MediaFeatureItem> getFaceResult() {
        return this.mFaceResult;
    }

    public List<Integer> getSceneUpdateItem() {
        return this.mSceneUpdate;
    }

    public List<FaceClusterInfo> getClusterInfo() {
        return this.mClusterInfo;
    }
}
