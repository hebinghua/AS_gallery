package com.miui.gallery.vlog.sdk.interfaces;

import com.miui.gallery.vlog.sdk.models.BaseVideoClip;
import com.xiaomi.milab.videosdk.XmsVideoClip;

/* loaded from: classes2.dex */
public interface IVideoClip {
    void changeSpeed(double d);

    void changeTrimInPoint(long j, boolean z);

    void changeTrimOutPoint(long j, boolean z);

    void changeVariableSpeed(double d, double d2, boolean z);

    double getBaseInfoSpeed();

    long getClipDurationWithTransition();

    String getFilePath();

    int getHeight();

    long getInPoint();

    int getIndex();

    long getOriginDuration();

    BaseVideoClip.TagInfo getOriginTag();

    long getOutPoint();

    BaseVideoClip.TagInfo getReverseTag();

    double getSpeed();

    BaseVideoClip.TagInfo getTag();

    long getTimelineDuration();

    String getTransName();

    String getTransParam();

    long getTrimDuration();

    long getTrimIn();

    long getTrimInWithTrans();

    long getTrimOut();

    long getTrimOutWithTrans();

    int getWidth();

    boolean isChangeSpeed();

    boolean isCuted();

    boolean isDeleted();

    boolean isInReverse();

    boolean isTransitionValid(IVideoClip iVideoClip, int i);

    void printTimeInfo(String str);

    void rebuild(XmsVideoClip xmsVideoClip);

    boolean removeAllFx();

    void setBaseInfoSpeed(double d);

    void setDeleted(boolean z);

    void setHeight(int i);

    void setIsCuted(boolean z);

    void setOriginDuration(long j);

    void setOriginTag(BaseVideoClip.TagInfo tagInfo);

    void setPlayInReverse(boolean z);

    void setReverseTag(BaseVideoClip.TagInfo tagInfo);

    void setTrans(String str, String str2);

    void setVolumeGain(float f, float f2);

    void setWidth(int i);

    void updateBaseSpeed();

    void updateTagInfo();
}
