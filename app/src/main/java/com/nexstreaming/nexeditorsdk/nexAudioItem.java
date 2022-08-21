package com.nexstreaming.nexeditorsdk;

import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;

/* loaded from: classes3.dex */
public final class nexAudioItem implements Cloneable {
    private static int sLastId = 1;
    public nexClip mClip;
    private int mId;
    private int mSpeedControl;
    public int mTrimEndDuration;
    public int mTrimStartDuration;

    public static nexAudioItem clone(nexAudioItem nexaudioitem) {
        nexAudioItem nexaudioitem2 = null;
        try {
            nexAudioItem nexaudioitem3 = (nexAudioItem) nexaudioitem.clone();
            try {
                nexaudioitem3.mClip = nexClip.clone(nexaudioitem.mClip);
                return nexaudioitem3;
            } catch (CloneNotSupportedException e) {
                e = e;
                nexaudioitem2 = nexaudioitem3;
                e.printStackTrace();
                return nexaudioitem2;
            }
        } catch (CloneNotSupportedException e2) {
            e = e2;
        }
    }

    public nexAudioItem(nexClip nexclip, int i, int i2) {
        this.mSpeedControl = 100;
        if (i2 <= i) {
            throw new InvalidRangeException(i, i2);
        }
        this.mClip = nexclip;
        nexclip.mStartTime = i;
        nexclip.mEndTime = i2;
        int i3 = sLastId;
        this.mId = i3;
        sLastId = i3 + 1;
    }

    public nexClip getClip() {
        return this.mClip;
    }

    public int getId() {
        return this.mId;
    }

    public void setProjectTime(int i, int i2) {
        if (i2 > i) {
            if (i < 0) {
                throw new InvalidRangeException(0, this.mClip.mEndTime, i);
            }
            nexClip nexclip = this.mClip;
            nexclip.mStartTime = i;
            nexclip.mEndTime = i2;
            nexclip.setProjectUpdateSignal(true);
            return;
        }
        throw new InvalidRangeException(i, i2);
    }

    public int getStartTime() {
        return this.mClip.mStartTime;
    }

    public int getEndTime() {
        return this.mClip.mEndTime;
    }

    public void setTrim(int i, int i2) {
        if (i2 <= i) {
            throw new InvalidRangeException(i, i2);
        }
        if (i > this.mClip.getTotalTime()) {
            throw new InvalidRangeException(0, this.mClip.getTotalTime(), i);
        }
        if (i2 > this.mClip.getTotalTime()) {
            throw new InvalidRangeException(0, this.mClip.getTotalTime(), i2);
        }
        this.mTrimStartDuration = i;
        this.mTrimEndDuration = this.mClip.getTotalTime() - i2;
        this.mClip.getAudioEnvelop().updateTrimTime(i, i2);
        this.mClip.setProjectUpdateSignal(true);
    }

    public void removeTrim() {
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mClip.getAudioEnvelop().updateTrimTime(0, this.mClip.getTotalTime());
        nexClip nexclip = this.mClip;
        nexclip.mEndTime = nexclip.mStartTime + nexclip.getTotalTime();
        this.mClip.setProjectUpdateSignal(true);
    }

    public int getStartTrimTime() {
        return this.mTrimStartDuration;
    }

    public int getEndTrimTime() {
        return this.mClip.getTotalTime() - this.mTrimEndDuration;
    }

    public void setSpeedControl(int i) {
        this.mSpeedControl = i;
    }

    public int getSpeedControl() {
        return this.mSpeedControl;
    }

    public nexSaveDataFormat.nexAudioItemOf getSaveData() {
        nexSaveDataFormat.nexAudioItemOf nexaudioitemof = new nexSaveDataFormat.nexAudioItemOf();
        nexaudioitemof.mId = this.mId;
        nexaudioitemof.mClip = this.mClip.getSaveData();
        nexaudioitemof.mTrimStartDuration = this.mTrimStartDuration;
        nexaudioitemof.mTrimEndDuration = this.mTrimEndDuration;
        nexaudioitemof.mSpeedControl = this.mSpeedControl;
        return nexaudioitemof;
    }

    public nexAudioItem(b bVar, nexSaveDataFormat.nexAudioItemOf nexaudioitemof) {
        this.mSpeedControl = 100;
        this.mId = nexaudioitemof.mId;
        this.mClip = new nexClip(bVar, nexaudioitemof.mClip);
        this.mTrimStartDuration = nexaudioitemof.mTrimStartDuration;
        this.mTrimEndDuration = nexaudioitemof.mTrimEndDuration;
        this.mSpeedControl = nexaudioitemof.mSpeedControl;
    }
}
