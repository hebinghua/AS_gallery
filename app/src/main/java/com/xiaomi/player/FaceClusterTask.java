package com.xiaomi.player;

import android.util.Log;

/* loaded from: classes3.dex */
public class FaceClusterTask extends AlgorithmTask<Integer> {
    private static final int MAX_FRAME_CACHE = 2;

    public FaceClusterTask(videoAnalytic videoanalytic, String str) {
        super(videoanalytic, str, "faceCluster", 2);
    }

    @Override // com.xiaomi.player.AlgorithmTask
    public void handleInput(Integer num) {
        String str = AlgorithmTask.TAG;
        Log.d(str, "faceCluster handleInput(" + num + ")");
        long currentTimeMillis = System.currentTimeMillis();
        if (num.intValue() == 0) {
            this.mVideoAnalytic.faceCluster.updateStartFrame(this.mFileId, num.intValue());
        } else {
            this.mVideoAnalytic.faceCluster.updateNormalFrame(this.mFileId, num.intValue());
        }
        Log.d(str, "updateFrame time:" + (System.currentTimeMillis() - currentTimeMillis));
    }

    @Override // com.xiaomi.player.AlgorithmTask
    public void onComplete() {
        int inputCount = getInputCount();
        String str = AlgorithmTask.TAG;
        Log.d(str, "faceCluster onComplete inputCount:" + inputCount);
        if (inputCount > 0) {
            this.mVideoAnalytic.faceCluster.updateEndFrame(this.mFileId);
        }
        Log.d(str, "faceCluster onComplete()");
        super.onComplete();
    }
}
