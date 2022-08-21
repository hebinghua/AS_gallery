package com.nexstreaming.nexeditorsdk;

import android.util.Log;
import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import com.xiaomi.miai.api.StatusCode;

/* loaded from: classes3.dex */
public final class nexVideoClipEdit implements Cloneable {
    @Deprecated
    public static int kAutoTrim_Divided = 1;
    @Deprecated
    public static int kAutoTrim_Interval = 2;
    public static final int kSpeedControl_MaxValue = 1600;
    public static final int kSpeedControl_MinValue = 3;
    private nexClip mClip;
    public int mFreezeDuration;
    private int mMasterSpeedControl;
    public int mTrimEndDuration;
    public int mTrimStartDuration;
    public boolean mUpdated;

    @Deprecated
    public void addTrim(int i, int i2, int i3) {
    }

    @Deprecated
    public int getTrimCount() {
        return 0;
    }

    @Deprecated
    public int removeTrim(int i) {
        return -1;
    }

    @Deprecated
    public int setAutoTrim(int i, int i2) {
        return 0;
    }

    public static nexVideoClipEdit clone(nexVideoClipEdit nexvideoclipedit) {
        try {
            return (nexVideoClipEdit) nexvideoclipedit.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private nexVideoClipEdit() {
        this.mMasterSpeedControl = 100;
        this.mFreezeDuration = 0;
    }

    private nexVideoClipEdit(nexClip nexclip) {
        this.mMasterSpeedControl = 100;
        this.mFreezeDuration = 0;
        this.mClip = nexclip;
    }

    public static nexVideoClipEdit getnexVideoClipEdit(nexClip nexclip) {
        if (nexclip.getClipType() != 4) {
            return null;
        }
        return new nexVideoClipEdit(nexclip);
    }

    public void setTrim(int i, int i2) {
        if (i2 <= i) {
            throw new InvalidRangeException(i, i2);
        }
        this.mTrimStartDuration = i;
        int totalTime = this.mClip.getTotalTime() - i2;
        this.mTrimEndDuration = totalTime;
        if (totalTime < 0 || this.mTrimStartDuration < 0) {
            throw new InvalidRangeException(this.mTrimStartDuration, this.mTrimEndDuration);
        }
        this.mUpdated = true;
        this.mClip.setProjectUpdateSignal(false);
    }

    public int getStartTrimTime() {
        return this.mTrimStartDuration;
    }

    public int getEndTrimTime() {
        return this.mClip.getTotalTime() - this.mTrimEndDuration;
    }

    public void setSpeedControl(int i) {
        if (this.mClip.getAudioOnOff()) {
            i = speedControlTab(i);
        }
        if (this.mMasterSpeedControl != i) {
            this.mUpdated = true;
            this.mClip.setProjectUpdateSignal(false);
            this.mMasterSpeedControl = i;
        }
    }

    public int getSpeedControl() {
        return this.mMasterSpeedControl;
    }

    public void clearTrim() {
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mUpdated = true;
        this.mClip.setProjectUpdateSignal(false);
    }

    public void setFreezeDuration(int i) {
        this.mFreezeDuration = i;
    }

    public int getDuration() {
        int totalTime;
        if (this.mTrimStartDuration != 0 || this.mTrimEndDuration != 0) {
            totalTime = (this.mClip.getTotalTime() - this.mTrimStartDuration) - this.mTrimEndDuration;
        } else {
            totalTime = this.mClip.getTotalTime();
        }
        int i = this.mMasterSpeedControl;
        if (i != 100) {
            if (i == 2) {
                totalTime *= 50;
            } else if (i == 3) {
                totalTime *= 32;
            } else if (i == 6) {
                totalTime *= 16;
            } else {
                totalTime = i == 13 ? totalTime * 8 : Math.round((totalTime * 100) / i);
            }
        }
        if (totalTime < 500) {
            Log.w("nexVideoClipEdit", "clip duration under 500! duration=" + totalTime + ", speed=" + this.mMasterSpeedControl + ", trim_start=" + this.mTrimStartDuration + ", trim_end=" + this.mTrimEndDuration);
        }
        return totalTime + this.mFreezeDuration;
    }

    private int speedControlTab(int i) {
        int[] iArr = {3, 6, 13, 25, 50, 75, 100, 125, 150, 175, 200, StatusCode.BAD_REQUEST, 800, kSpeedControl_MaxValue};
        for (int i2 = 0; i2 < 14; i2++) {
            if (iArr[i2] >= i) {
                return iArr[i2];
            }
        }
        return kSpeedControl_MaxValue;
    }

    public nexSaveDataFormat.nexVideoClipEditOf getSaveData() {
        nexSaveDataFormat.nexVideoClipEditOf nexvideoclipeditof = new nexSaveDataFormat.nexVideoClipEditOf();
        nexvideoclipeditof.mTrimStartDuration = this.mTrimStartDuration;
        nexvideoclipeditof.mTrimEndDuration = this.mTrimEndDuration;
        nexvideoclipeditof.mMasterSpeedControl = this.mMasterSpeedControl;
        return nexvideoclipeditof;
    }

    public nexVideoClipEdit(nexClip nexclip, nexSaveDataFormat.nexVideoClipEditOf nexvideoclipeditof) {
        this.mMasterSpeedControl = 100;
        this.mFreezeDuration = 0;
        this.mTrimStartDuration = nexvideoclipeditof.mTrimStartDuration;
        this.mTrimEndDuration = nexvideoclipeditof.mTrimEndDuration;
        this.mMasterSpeedControl = nexvideoclipeditof.mMasterSpeedControl;
        this.mClip = nexclip;
    }
}
