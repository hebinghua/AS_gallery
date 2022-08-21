package com.miui.gallery.vlog.rule;

import android.text.TextUtils;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.vlog.entity.VideoClip;
import java.io.File;

/* loaded from: classes2.dex */
public class VideoTagClip implements Cloneable {
    public MediaScene mClassificationScene;
    public int mHeight;
    public long mInPoint;
    public long mOutPoint;
    public String mPath;
    public MediaScene mShootingScene;
    public long mTakenTime;
    public long mTotalDuration;
    public int mType;
    public int mWidth;

    public static boolean sameVideoContent(VideoTagClip videoTagClip, VideoTagClip videoTagClip2) {
        return (videoTagClip == null || videoTagClip2 == null || videoTagClip.getPath() == null || videoTagClip2.getPath() == null || !videoTagClip.getPath().equals(videoTagClip2.getPath()) || videoTagClip.getInPoint() != videoTagClip2.getInPoint() || videoTagClip.getOutPoint() != videoTagClip2.getOutPoint()) ? false : true;
    }

    public Object clone() {
        try {
            return (VideoTagClip) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static VideoTagClip getCrossClip(MediaScene mediaScene, MediaScene mediaScene2, long j) {
        if (mediaScene == null || mediaScene2 == null || TextUtils.isEmpty(mediaScene.getPath()) || TextUtils.isEmpty(mediaScene2.getPath()) || !mediaScene.getPath().equals(mediaScene2.getPath())) {
            return null;
        }
        long milliSecond = Util.milliSecond(mediaScene.getEndTime());
        long milliSecond2 = Util.milliSecond(mediaScene.getStartTime());
        long milliSecond3 = Util.milliSecond(mediaScene2.getStartTime());
        long milliSecond4 = Util.milliSecond(mediaScene2.getEndTime());
        if (milliSecond2 == milliSecond || milliSecond3 == milliSecond4) {
            return null;
        }
        long min = Math.min(milliSecond, milliSecond4);
        long max = Math.max(milliSecond2, milliSecond3);
        if (min - max <= 0) {
            return null;
        }
        return new VideoTagClip(mediaScene, mediaScene2, j, max, min);
    }

    public static VideoTagClip getSingleClip(MediaScene mediaScene, long j) {
        if (mediaScene != null && !TextUtils.isEmpty(mediaScene.getPath()) && Util.milliSecond(mediaScene.getEndTime()) > Util.milliSecond(mediaScene.getStartTime())) {
            return new VideoTagClip(mediaScene, j);
        }
        return null;
    }

    public static VideoTagClip getArtificialVideoClip(String str, VideoInfo videoInfo, long j) {
        if (!TextUtils.isEmpty(str) && videoInfo != null && videoInfo.getDurationMilli() > 0) {
            return new VideoTagClip(str, j, videoInfo.getDurationMilli());
        }
        return null;
    }

    public VideoTagClip(String str, long j, long j2) {
        this.mType = 0;
        this.mPath = str;
        this.mTakenTime = getTakenTime(str);
        this.mTotalDuration = j;
        this.mInPoint = 0L;
        this.mInPoint = 0L;
        this.mOutPoint = j2;
    }

    public VideoTagClip(MediaScene mediaScene, long j) {
        this.mTotalDuration = j;
        this.mInPoint = Util.milliSecond(mediaScene.getStartTime());
        this.mOutPoint = Util.milliSecond(mediaScene.getEndTime());
        this.mPath = mediaScene.getPath();
        this.mTakenTime = getTakenTime(mediaScene.getPath());
        if (Shooting.isShootingScene(mediaScene)) {
            this.mShootingScene = mediaScene;
            this.mType = 2;
            return;
        }
        this.mClassificationScene = mediaScene;
        this.mType = 1;
    }

    public VideoTagClip(MediaScene mediaScene, MediaScene mediaScene2, long j, long j2, long j3) {
        this.mType = 3;
        this.mClassificationScene = mediaScene;
        this.mShootingScene = mediaScene2;
        this.mTotalDuration = j;
        this.mInPoint = j2;
        this.mOutPoint = j3;
        String path = mediaScene.getPath();
        this.mPath = path;
        this.mTakenTime = getTakenTime(path);
    }

    public int getClassificationTag() {
        MediaScene mediaScene = this.mClassificationScene;
        if (mediaScene == null) {
            return -1;
        }
        return mediaScene.getSceneTag();
    }

    public int getShootingTag() {
        MediaScene mediaScene = this.mShootingScene;
        if (mediaScene != null) {
            return mediaScene.getSceneTag();
        }
        return -1;
    }

    public int getClassification() {
        return Classification.getClassification(this.mClassificationScene);
    }

    public int getShooting() {
        return Shooting.getShooting(this.mShootingScene);
    }

    public final long getTakenTime(String str) {
        return new File(str).lastModified();
    }

    public long getDuration() {
        return this.mOutPoint - this.mInPoint;
    }

    public long getTotalDuration() {
        return this.mTotalDuration;
    }

    public long getInPoint() {
        return this.mInPoint;
    }

    public long getOutPoint() {
        return this.mOutPoint;
    }

    public String getPath() {
        return this.mPath;
    }

    public long getTakenTime() {
        return this.mTakenTime;
    }

    public MediaScene getClassificationScene() {
        return this.mClassificationScene;
    }

    public MediaScene getShootingScene() {
        return this.mShootingScene;
    }

    public void setInPoint(long j) {
        this.mInPoint = j;
    }

    public void setOutPoint(long j) {
        this.mOutPoint = j;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int i) {
        this.mWidth = i;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int i) {
        this.mHeight = i;
    }

    public static VideoClip convert(VideoTagClip videoTagClip) {
        return new VideoClip(videoTagClip.getPath(), videoTagClip.getTotalDuration() * 1000, videoTagClip.getInPoint() * 1000, videoTagClip.getOutPoint() * 1000, videoTagClip.getWidth(), videoTagClip.getHeight());
    }
}
