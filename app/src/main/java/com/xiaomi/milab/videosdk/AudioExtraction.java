package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class AudioExtraction {
    private static final String TAG = "AudioExtraction";
    private int mChannels;
    private int mFrequency;
    private String mInputFile;
    private String mOutputFile;
    private long mHandler = 0;
    private int mFrom = 0;
    private int mTo = -1;

    public native long _extracAudio(String str, int i, int i2, String str2, int i3, int i4);

    public native void _release(long j);

    public void setInputFile(String str) {
        this.mInputFile = str;
    }

    public void setOutputFile(String str, int i, int i2) {
        this.mOutputFile = str;
        this.mChannels = i;
        this.mFrequency = i2;
    }

    public void setRegion(int i, int i2) {
        this.mFrom = i;
        this.mTo = i2;
    }

    public void extract() {
        this.mHandler = _extracAudio(this.mInputFile, this.mFrom, this.mTo, this.mOutputFile, this.mChannels, this.mFrequency);
    }

    public void release() {
        long j = this.mHandler;
        if (j != 0) {
            _release(j);
        }
    }
}
