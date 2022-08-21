package com.xiaomi.player;

import android.util.Log;
import com.xiaomi.player.videoAnalytic;

/* loaded from: classes3.dex */
public class VideoTagTask extends AlgorithmTask<Integer> {
    private static final int MAX_FRAME_CACHE = 2;
    private videoAnalytic.VideoTag.TagNode[] mTagNodes;

    public VideoTagTask(videoAnalytic videoanalytic, String str) {
        super(videoanalytic, str, "video_tag", 2);
        this.mTagNodes = null;
    }

    @Override // com.xiaomi.player.AlgorithmTask
    public void handleInput(Integer num) {
        String str = AlgorithmTask.TAG;
        Log.d(str, "video_tag handleInput(" + num + ")");
        long currentTimeMillis = System.currentTimeMillis();
        if (num.intValue() == 0) {
            this.mVideoAnalytic.videoTag.updateStartFrame(this.mFileId, num.intValue());
        } else {
            this.mVideoAnalytic.videoTag.updateNormalFrame(this.mFileId, num.intValue());
        }
        Log.d(str, "updateFrame time:" + (System.currentTimeMillis() - currentTimeMillis));
    }

    @Override // com.xiaomi.player.AlgorithmTask
    public void onComplete() {
        this.mTagNodes = this.mVideoAnalytic.videoTag.getTagList(this.mFileId);
        Log.d(AlgorithmTask.TAG, "video_tag onComplete()");
        super.onComplete();
    }

    public videoAnalytic.VideoTag.TagNode[] getTagNodes() {
        return this.mTagNodes;
    }
}
