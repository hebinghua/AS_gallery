package com.miui.gallery.assistant.manager.result;

import com.miui.gallery.assistant.jni.filter.SceneResult;
import com.miui.gallery.assistant.model.MediaFeature;

/* loaded from: classes.dex */
public class SceneFeatureResult extends ImageFeatureResult {
    public SceneResult mSceneResult;

    @Override // com.miui.gallery.assistant.manager.result.ImageFeatureResult
    public void updateMediaFeature(MediaFeature mediaFeature) {
    }

    public SceneFeatureResult(SceneResult sceneResult, long j) {
        super(0, j);
        this.mSceneResult = sceneResult;
    }

    public SceneFeatureResult(int i, long j) {
        super(i, j);
    }
}
