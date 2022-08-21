package com.miui.gallery.vlog.sdk.models;

import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.sdk.models.BaseVideoClip;
import com.xiaomi.milab.videosdk.XmsAudioFilter;
import com.xiaomi.milab.videosdk.XmsVideoClip;
import com.xiaomi.milab.videosdk.XmsVideoTrack;

/* loaded from: classes2.dex */
public class MiVlogVideoClip extends BaseVideoClip {
    public XmsAudioFilter mAudioFilter;
    public XmsVideoTrack mAudioTrack;
    public BaseVideoClip.BaseInfo mBaseInfo;
    public long mOriginDuration;
    public BaseVideoClip.TagInfo mOriginTag;
    public BaseVideoClip.TagInfo mReverseTag;
    public BaseVideoClip.TagInfo mTag;
    public String mTrans;
    public String mTransParam;
    public long mTrimDuration;
    public XmsVideoClip xmsVideoClip;

    public MiVlogVideoClip(XmsVideoTrack xmsVideoTrack, XmsVideoClip xmsVideoClip) {
        if (xmsVideoClip != null) {
            this.xmsVideoClip = xmsVideoClip;
            this.mAudioTrack = xmsVideoTrack;
            initBaseInfo();
            setTag();
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void rebuild(XmsVideoClip xmsVideoClip) {
        if (xmsVideoClip == null) {
            return;
        }
        this.xmsVideoClip = xmsVideoClip;
        this.mAudioFilter = null;
    }

    @Override // com.miui.gallery.vlog.sdk.models.BaseVideoClip, com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setPlayInReverse(boolean z) {
        super.setPlayInReverse(z);
        BaseVideoClip.BaseInfo baseInfo = this.mBaseInfo;
        if (baseInfo != null) {
            baseInfo.mIsReverse = z;
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public String getFilePath() {
        BaseVideoClip.BaseInfo baseInfo = this.mBaseInfo;
        if (baseInfo != null) {
            return baseInfo.mPath;
        }
        return this.xmsVideoClip.getSourcePath();
    }

    public final void initBaseInfo() {
        if (this.mBaseInfo == null) {
            this.mBaseInfo = new BaseVideoClip.BaseInfo();
        }
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip != null) {
            this.mBaseInfo.mPath = xmsVideoClip.getSourcePath();
            this.mBaseInfo.mIsChangeSpeed = isChangeSpeed();
            this.mBaseInfo.mIsReverse = isInReverse();
            this.mBaseInfo.mIsCuted = isCuted();
            this.mBaseInfo.mSpeed = getSpeed();
            this.mBaseInfo.mOriginDuration = getOriginDuration();
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void updateBaseSpeed() {
        this.mBaseInfo.mSpeed = getSpeed();
    }

    public BaseVideoClip.BaseInfo getBaseInfo() {
        return this.mBaseInfo;
    }

    public void updateBaseInfo(BaseVideoClip.BaseInfo baseInfo) {
        if (baseInfo != null) {
            BaseVideoClip.BaseInfo baseInfo2 = this.mBaseInfo;
            baseInfo2.mOriginDuration = baseInfo.mOriginDuration;
            baseInfo2.mSpeed = baseInfo.mSpeed;
            baseInfo2.mIsChangeSpeed = baseInfo.mIsChangeSpeed;
            baseInfo2.mPath = baseInfo.mPath;
            baseInfo2.mIsReverse = baseInfo.mIsReverse;
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void changeTrimOutPoint(long j, boolean z) {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return;
        }
        xmsVideoClip.setInAndOutTrans(xmsVideoClip.getTransIn(), j / 1000);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void changeTrimInPoint(long j, boolean z) {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return;
        }
        xmsVideoClip.setInAndOutTrans(j / 1000, xmsVideoClip.getTransOut());
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getTrimDuration() {
        long abs = Math.abs(getTrimOut() - getTrimIn());
        this.mTrimDuration = abs;
        return abs;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getOriginDuration() {
        long j = this.mOriginDuration;
        return j == 0 ? this.xmsVideoClip.getsourceDuration() * 1000 : j;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void printTimeInfo(String str) {
        DefaultLogger.d("VlogVideoClip", "printTimeInfo: %s.", str);
        DefaultLogger.d("VlogVideoClip", "printTimeInfo: [path = %s]", getFilePath());
        DefaultLogger.d("VlogVideoClip", "printTimeInfo: [index: %s, speed: %s, isChangeSpeed:%s,   isInReverse: %s, inPoint: %s, outPoint: %s, trimIn: %s, trimOut: %s, originDuration: %s, timelineDuration: %s, trimDuration: %s. ]", Integer.valueOf(getIndex()), Double.valueOf(getSpeed()), Boolean.valueOf(isChangeSpeed()), Boolean.valueOf(isInReverse()), Long.valueOf(getInPoint()), Long.valueOf(getOutPoint()), Long.valueOf(getTrimIn()), Long.valueOf(getTrimOut()), Long.valueOf(getOriginDuration()), Long.valueOf(getTimelineDuration()), Long.valueOf(getTrimDuration()));
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setOriginDuration(long j) {
        this.mOriginDuration = j;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getTimelineDuration() {
        return this.xmsVideoClip.getDuration();
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setTrans(String str, String str2) {
        this.mTrans = str;
        this.mTransParam = str2;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public String getTransName() {
        return this.mTrans;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public String getTransParam() {
        return this.mTransParam;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setIsCuted(boolean z) {
        this.mBaseInfo.mIsCuted = z;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setWidth(int i) {
        setAttachment("videoWidth", Integer.valueOf(i));
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setHeight(int i) {
        setAttachment("videoHeight", Integer.valueOf(i));
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public int getWidth() {
        Object attachment = getAttachment("videoWidth");
        if (attachment == null) {
            return -1;
        }
        return ((Integer) attachment).intValue();
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public int getHeight() {
        Object attachment = getAttachment("videoHeight");
        if (attachment == null) {
            return -1;
        }
        return ((Integer) attachment).intValue();
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public boolean isTransitionValid(IVideoClip iVideoClip, int i) {
        long j = i;
        return getClipDurationWithTransition() >= j && iVideoClip.getClipDurationWithTransition() >= j;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getClipDurationWithTransition() {
        return ((long) ((this.xmsVideoClip.getTransOut() - this.xmsVideoClip.getTransIn()) / getSpeed())) * 1000;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getInPoint() {
        return this.mAudioTrack.getClipStartPos(this.xmsVideoClip.getIndex()) * 1000;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getOutPoint() {
        return (this.mAudioTrack.getClipStartPos(this.xmsVideoClip.getIndex()) + this.xmsVideoClip.getDuration()) * 1000;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public boolean isChangeSpeed() {
        return Math.abs(this.xmsVideoClip.getSpeed() - 1.0d) > 0.01d;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void changeSpeed(double d) {
        this.xmsVideoClip.setSpeed(d);
        DefaultLogger.d("VlogVideoClip", " mBaseInfo.speed: %s, newSpeed: %s, isChangeSpeed(): %s", Double.valueOf(this.mBaseInfo.getSpeed()), Double.valueOf(d), Boolean.valueOf(isChangeSpeed()));
        BaseVideoClip.BaseInfo baseInfo = this.mBaseInfo;
        if (baseInfo != null) {
            baseInfo.mIsChangeSpeed = isChangeSpeed();
        }
        BaseVideoClip.TagInfo tagInfo = this.mTag;
        if (tagInfo != null) {
            tagInfo.setChangeSpeed(isChangeSpeed());
            this.mTag.setSpeed(d);
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void changeVariableSpeed(double d, double d2, boolean z) {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return;
        }
        xmsVideoClip.setInOutSpeed(d, d2);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setVolumeGain(float f, float f2) {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return;
        }
        if (this.mAudioFilter == null) {
            this.mAudioFilter = xmsVideoClip.appendAudioEffect("audio.volume", "");
        }
        this.mAudioFilter.setDoubleParam("volume.percent", f / 100.0f);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public boolean isInReverse() {
        return this.mBaseInfo.mIsReverse;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public boolean isCuted() {
        return this.mBaseInfo.mIsCuted;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public BaseVideoClip.TagInfo getTag() {
        return this.mTag;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public int getIndex() {
        return this.xmsVideoClip.getIndex();
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public double getSpeed() {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return 1.0d;
        }
        return xmsVideoClip.getSpeed();
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getTrimIn() {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return 0L;
        }
        return xmsVideoClip.getIn() * 1000;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getTrimOut() {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return 0L;
        }
        return xmsVideoClip.getOut() * 1000;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getTrimInWithTrans() {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return 0L;
        }
        return xmsVideoClip.getTransIn() * 1000;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public long getTrimOutWithTrans() {
        XmsVideoClip xmsVideoClip = this.xmsVideoClip;
        if (xmsVideoClip == null) {
            return 0L;
        }
        return xmsVideoClip.getTransOut() * 1000;
    }

    public void setTag() {
        if (this.mTag == null) {
            this.mTag = new BaseVideoClip.TagInfo();
        }
        this.mTag.mIsChangeSpeed = isChangeSpeed();
        this.mTag.mSpeed = getSpeed();
        this.mTag.mTrimIn = getTrimIn();
        this.mTag.mTrimOut = getTrimOut();
        this.mTag.TrimDuration = getTrimDuration();
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public BaseVideoClip.TagInfo getOriginTag() {
        return this.mOriginTag;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setOriginTag(BaseVideoClip.TagInfo tagInfo) {
        this.mOriginTag = tagInfo;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public BaseVideoClip.TagInfo getReverseTag() {
        return this.mReverseTag;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setReverseTag(BaseVideoClip.TagInfo tagInfo) {
        this.mReverseTag = tagInfo;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void updateTagInfo() {
        BaseVideoClip.TagInfo tagInfo = this.mTag;
        if (tagInfo == null) {
            return;
        }
        tagInfo.setTrimIn(getTrimInWithTrans());
        this.mTag.setTrimOut(getTrimOutWithTrans());
        this.mTag.setTrimDuration(getTrimDuration());
        this.mTag.setReverse(isInReverse());
        if (!isChangeSpeed()) {
            return;
        }
        this.mTag.setChangeSpeed(isChangeSpeed());
        this.mTag.setSpeed(getSpeed());
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public void setBaseInfoSpeed(double d) {
        BaseVideoClip.BaseInfo baseInfo = this.mBaseInfo;
        if (baseInfo != null) {
            baseInfo.mSpeed = d;
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IVideoClip
    public double getBaseInfoSpeed() {
        BaseVideoClip.BaseInfo baseInfo = this.mBaseInfo;
        if (baseInfo != null) {
            return baseInfo.mSpeed;
        }
        return 1.0d;
    }
}
