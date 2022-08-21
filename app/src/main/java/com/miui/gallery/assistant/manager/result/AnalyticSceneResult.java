package com.miui.gallery.assistant.manager.result;

import com.miui.gallery.assistant.model.MediaScene;
import java.util.List;

/* loaded from: classes.dex */
public class AnalyticSceneResult extends AlgorithmResult {
    public List<MediaScene> mVideoSceneResult;

    public AnalyticSceneResult(int i, List<MediaScene> list) {
        super(i);
        this.mVideoSceneResult = list;
    }

    public AnalyticSceneResult(List<MediaScene> list) {
        super(0);
        this.mVideoSceneResult = list;
    }

    public AnalyticSceneResult(int i) {
        super(i);
    }

    public List<MediaScene> getResult() {
        return this.mVideoSceneResult;
    }
}
