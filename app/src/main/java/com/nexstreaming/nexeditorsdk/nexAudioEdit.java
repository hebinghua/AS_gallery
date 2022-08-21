package com.nexstreaming.nexeditorsdk;

import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;

/* loaded from: classes3.dex */
public class nexAudioEdit implements Cloneable {
    private static final String TAG = "nexAudioEdit";
    public static final int kMusicEffect_LIVE_CONCERT = 1;
    public static final int kMusicEffect_MUSIC_ENHANCER = 3;
    public static final int kMusicEffect_NONE = 0;
    public static final int kMusicEffect_STEREO_CHORUS = 2;
    public static final int kVoiceFactor_CHIPMUNK = 1;
    public static final int kVoiceFactor_DEEP = 3;
    public static final int kVoiceFactor_MODULATION = 4;
    public static final int kVoiceFactor_NONE = 0;
    public static final int kVoiceFactor_ROBOT = 2;
    private int VCfactor;
    private int mBassStrength;
    private nexClip mClip;
    private int mCompressor;
    private String mEnhancedAudioFilter;
    private int mMusicEffect;
    private int mPanLeft;
    private int mPanRight;
    private int mPitch;
    private int mProcessorStrength;

    private nexAudioEdit() {
        this.VCfactor = 0;
        this.mPitch = 0;
        this.mCompressor = 0;
        this.mProcessorStrength = -1;
        this.mBassStrength = -1;
        this.mMusicEffect = 0;
        this.mPanLeft = -111;
        this.mPanRight = -111;
        this.mEnhancedAudioFilter = null;
    }

    private nexAudioEdit(nexClip nexclip) {
        this.VCfactor = 0;
        this.mPitch = 0;
        this.mCompressor = 0;
        this.mProcessorStrength = -1;
        this.mBassStrength = -1;
        this.mMusicEffect = 0;
        this.mPanLeft = -111;
        this.mPanRight = -111;
        this.mEnhancedAudioFilter = null;
        this.mClip = nexclip;
    }

    public static nexAudioEdit clone(nexClip nexclip, nexAudioEdit nexaudioedit) {
        nexAudioEdit nexaudioedit2 = null;
        try {
            nexAudioEdit nexaudioedit3 = (nexAudioEdit) nexaudioedit.clone();
            try {
                nexaudioedit3.mClip = nexclip;
                return nexaudioedit3;
            } catch (CloneNotSupportedException e) {
                e = e;
                nexaudioedit2 = nexaudioedit3;
                e.printStackTrace();
                return nexaudioedit2;
            }
        } catch (CloneNotSupportedException e2) {
            e = e2;
        }
    }

    public static nexAudioEdit getnexAudioEdit(nexClip nexclip) {
        if (nexclip.getClipType() == 4 || nexclip.getClipType() == 3) {
            return new nexAudioEdit(nexclip);
        }
        return null;
    }

    public void setPitch(int i) {
        if (!this.mClip.getAttachmentState()) {
            Log.d(TAG, "setPitch getInfo fail!");
        } else {
            this.mPitch = i;
        }
    }

    public int getPitch() {
        return this.mPitch;
    }

    public void setCompressor(int i) {
        if (!this.mClip.getAttachmentState()) {
            Log.d(TAG, "setCompressor getInfo fail!");
        } else {
            this.mCompressor = i;
        }
    }

    public int getCompressor() {
        return this.mCompressor;
    }

    public void setProcessorStrength(int i) {
        if (!this.mClip.getAttachmentState()) {
            Log.d(TAG, "setProcessorStrength getInfo fail!");
        } else {
            this.mProcessorStrength = i;
        }
    }

    public int getProcessorStrength() {
        return this.mProcessorStrength;
    }

    public void setBassStrength(int i) {
        if (!this.mClip.getAttachmentState()) {
            Log.d(TAG, "setBassStrength getInfo fail!");
        } else {
            this.mBassStrength = i;
        }
    }

    public int getBassStrength() {
        return this.mBassStrength;
    }

    public void setMusicEffect(int i) {
        if (!this.mClip.getAttachmentState()) {
            Log.d(TAG, "setMusicEffect getInfo fail!");
        } else {
            this.mMusicEffect = i;
        }
    }

    public int getMusicEffect() {
        return this.mMusicEffect;
    }

    public int getPanLeft() {
        return this.mPanLeft;
    }

    public void setPanLeft(int i) {
        if (!this.mClip.getAttachmentState()) {
            Log.d(TAG, "setPanleft getInfo fail!");
        } else {
            this.mPanLeft = i;
        }
    }

    public int getPanRight() {
        return this.mPanRight;
    }

    public void setPanRight(int i) {
        if (!this.mClip.getAttachmentState()) {
            Log.d(TAG, "setPanRight getInfo fail!");
        } else {
            this.mPanRight = i;
        }
    }

    public void setVoiceChangerFactor(int i) {
        this.VCfactor = i;
    }

    public int getVoiceChangerFactor() {
        return this.VCfactor;
    }

    public String getEnhancedAudioFilter() {
        return this.mEnhancedAudioFilter;
    }

    public void setEnhancedAudioFilter(String str) {
        this.mEnhancedAudioFilter = str;
    }

    public nexSaveDataFormat.nexAudioEditOf getSaveData() {
        nexSaveDataFormat.nexAudioEditOf nexaudioeditof = new nexSaveDataFormat.nexAudioEditOf();
        nexaudioeditof.VCfactor = this.VCfactor;
        nexaudioeditof.mPitch = this.mPitch;
        nexaudioeditof.mCompressor = this.mCompressor;
        nexaudioeditof.mProcessorStrength = this.mProcessorStrength;
        nexaudioeditof.mBassStrength = this.mBassStrength;
        nexaudioeditof.mMusicEffect = this.mMusicEffect;
        nexaudioeditof.mPanLeft = this.mPanLeft;
        nexaudioeditof.mPanRight = this.mPanRight;
        return nexaudioeditof;
    }

    public nexAudioEdit(nexClip nexclip, nexSaveDataFormat.nexAudioEditOf nexaudioeditof) {
        this.VCfactor = 0;
        this.mPitch = 0;
        this.mCompressor = 0;
        this.mProcessorStrength = -1;
        this.mBassStrength = -1;
        this.mMusicEffect = 0;
        this.mPanLeft = -111;
        this.mPanRight = -111;
        this.mEnhancedAudioFilter = null;
        this.VCfactor = nexaudioeditof.VCfactor;
        this.mPitch = nexaudioeditof.mPitch;
        this.mCompressor = nexaudioeditof.mCompressor;
        this.mProcessorStrength = nexaudioeditof.mProcessorStrength;
        this.mBassStrength = nexaudioeditof.mBassStrength;
        this.mMusicEffect = nexaudioeditof.mMusicEffect;
        this.mPanLeft = nexaudioeditof.mPanLeft;
        this.mPanRight = nexaudioeditof.mPanRight;
        this.mClip = nexclip;
    }
}
