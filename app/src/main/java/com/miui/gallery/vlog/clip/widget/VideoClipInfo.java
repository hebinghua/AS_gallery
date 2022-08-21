package com.miui.gallery.vlog.clip.widget;

import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;

/* loaded from: classes2.dex */
public class VideoClipInfo {
    public String mFilePath;
    public boolean mHasPreviousTransition;
    public boolean mHasTransition;
    public long mInPoint;
    public int mIndex;
    public VideoClipInfo mNext;
    public long mOriginalDuration;
    public long mOutPoint;
    public IVideoClip mOwner;
    public double mSpeed = 1.0d;
    public long mTransTrimIn;
    public long mTransTrimOut;
    public long mTrimIn;
    public long mTrimOut;
    public long mVideoDuration;

    public VideoClipInfo(IVideoClip iVideoClip, int i) {
        this.mOwner = iVideoClip;
        this.mIndex = i;
    }

    public IVideoClip getOwner() {
        return this.mOwner;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public long getTransTrimIn() {
        return this.mTransTrimIn;
    }

    public void setTransTrimIn(long j) {
        this.mTransTrimIn = j;
    }

    public long getTransTrimOut() {
        return this.mTransTrimOut;
    }

    public void setTransTrimOut(long j) {
        this.mTransTrimOut = j;
    }

    public long getTrimIn() {
        return this.mTrimIn;
    }

    public void setTrimIn(long j) {
        this.mTrimIn = j;
    }

    public void setTrimOut(long j) {
        this.mTrimOut = j;
    }

    public long getInPoint() {
        return this.mInPoint;
    }

    public void setInPoint(long j) {
        this.mInPoint = j;
    }

    public long getOutPoint() {
        return this.mOutPoint;
    }

    public void setOutPoint(long j) {
        this.mOutPoint = j;
    }

    public void setHasTransition(boolean z) {
        this.mHasTransition = z;
    }

    public boolean hasTransition() {
        return this.mHasTransition;
    }

    public void setSpeed(double d) {
        this.mSpeed = d;
    }

    public double getSpeed() {
        return this.mSpeed;
    }

    public long getDurationWithOutTransition() {
        return (long) ((this.mTrimOut - this.mTrimIn) / getSpeed());
    }

    public long getDurationWithTransition() {
        long durationWithOutTransition = getDurationWithOutTransition();
        if (this.mHasTransition) {
            durationWithOutTransition += 450000;
        }
        return this.mHasPreviousTransition ? durationWithOutTransition + 450000 : durationWithOutTransition;
    }

    public long getDurationForDrag() {
        return (long) ((this.mTransTrimOut - this.mTransTrimIn) / getSpeed());
    }

    public void setHasPreviousTransition(boolean z) {
        this.mHasPreviousTransition = z;
    }

    public boolean hasPreviousTransition() {
        return this.mHasPreviousTransition;
    }

    public void setVideoDuration(long j) {
        this.mVideoDuration = j;
    }

    public long getVideoDuration() {
        return this.mVideoDuration;
    }

    public void updateOriginalDuration() {
        this.mOriginalDuration = getDurationWithOutTransition();
    }

    public long getOriginalDuration() {
        return this.mOriginalDuration;
    }

    public void setNext(VideoClipInfo videoClipInfo) {
        this.mNext = videoClipInfo;
    }

    public boolean isTransitionEnable(int i) {
        VideoClipInfo videoClipInfo = this.mNext;
        if (videoClipInfo != null) {
            return this.mOwner.isTransitionValid(videoClipInfo.mOwner, i);
        }
        return false;
    }
}
