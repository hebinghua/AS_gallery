package com.miui.gallery.vlog.sdk.models;

import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import java.util.HashMap;

/* loaded from: classes2.dex */
public abstract class BaseVideoClip implements IVideoClip {
    public HashMap<String, Object> mAttachment = new HashMap<>();
    public boolean mIsDeleted;

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public boolean removeAllFx() {
        return false;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setPlayInReverse(boolean z) {
    }

    public void setAttachment(String str, Object obj) {
        this.mAttachment.put(str, obj);
    }

    public Object getAttachment(String str) {
        return this.mAttachment.get(str);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public boolean isDeleted() {
        return this.mIsDeleted;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setDeleted(boolean z) {
        this.mIsDeleted = z;
    }

    /* loaded from: classes2.dex */
    public static class BaseInfo {
        public boolean mIsChangeSpeed;
        public boolean mIsCuted;
        public boolean mIsReverse;
        public long mOriginDuration;
        public String mPath;
        public double mSpeed = 1.0d;

        public long getOriginDuration() {
            return this.mOriginDuration;
        }

        public double getSpeed() {
            return this.mSpeed;
        }
    }

    /* loaded from: classes2.dex */
    public static class TagInfo {
        public long TrimDuration;
        public boolean mIsChangeSpeed;
        public boolean mIsReverse;
        public long mReverseTagTrimDuration;
        public double mSpeed;
        public long mTrimIn;
        public long mTrimOut;

        public void setReverseTagTrimDuration(long j) {
            this.mReverseTagTrimDuration = j;
        }

        public long getTrimDuration() {
            return this.TrimDuration;
        }

        public void setTrimDuration(long j) {
            this.TrimDuration = j;
        }

        public void setTrimIn(long j) {
            this.mTrimIn = j;
        }

        public void setTrimOut(long j) {
            this.mTrimOut = j;
        }

        public void setChangeSpeed(boolean z) {
            this.mIsChangeSpeed = z;
        }

        public void setSpeed(double d) {
            this.mSpeed = d;
        }

        public void setReverse(boolean z) {
            this.mIsReverse = z;
        }
    }
}
