package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class AudioFetch {
    private static final String TAG = "AudioFetch";
    private long mHandler = 0;

    public native long _fetchAudio(String str, String str2);

    public native void _release(long j);

    public void extractAudio(String str, String str2) {
        this.mHandler = _fetchAudio(str, str2);
    }

    public void release() {
        long j = this.mHandler;
        if (j != 0) {
            _release(j);
        }
    }
}
