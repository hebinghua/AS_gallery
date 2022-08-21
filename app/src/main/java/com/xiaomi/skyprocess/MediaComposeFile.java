package com.xiaomi.skyprocess;

import android.util.Log;
import java.io.FileDescriptor;

/* loaded from: classes3.dex */
public class MediaComposeFile {
    private static String TAG = "MediaComposeFile";
    private long mComposeFile;
    private MediaEffectGraph mMediaEffectGraph;

    private static native void BeginComposeFileJni();

    private static native void CancelComposeFileJni();

    private static native boolean ConstructMediaComposeFileJni(long j, int i, int i2, int i3, int i4);

    private static native void DestructMediaComposeFileJni();

    private static native void EnableComposeFile2GifJni();

    private static native void SetComposeFileFdJni(FileDescriptor fileDescriptor, String str);

    private static native void SetComposeFileNameJni(String str);

    private static native void SetComposeNotifyJni(EffectNotifier effectNotifier);

    private static native void SetParamentForGifJni(int i, int i2, float f, int i3, String str);

    private static native int getCurrentComposePercentJni();

    public MediaComposeFile(MediaEffectGraph mediaEffectGraph) {
        this.mMediaEffectGraph = mediaEffectGraph;
    }

    public void SetMediaEffectGraph(MediaEffectGraph mediaEffectGraph) {
        this.mMediaEffectGraph = mediaEffectGraph;
    }

    public boolean ConstructMediaComposeFile(int i, int i2, int i3, int i4) {
        MediaEffectGraph mediaEffectGraph = this.mMediaEffectGraph;
        if (mediaEffectGraph == null) {
            Log.e(TAG, "effect graph is null, failed!");
            return false;
        }
        ConstructMediaComposeFileJni(mediaEffectGraph.GetGraphLine(), i, i2, i3, i4);
        String str = TAG;
        Log.d(str, "construct compose file: " + this.mComposeFile);
        return true;
    }

    public void DestructMediaComposeFile() {
        String str = TAG;
        Log.d(str, "destruct mComposefile:" + this.mComposeFile);
        DestructMediaComposeFileJni();
    }

    public void SetComposeFileName(String str) {
        String str2 = TAG;
        Log.d(str2, "SetComposeFileName " + str);
        SetComposeFileNameJni(str);
    }

    public void SetComposeFileFd(FileDescriptor fileDescriptor, String str) {
        SetComposeFileFdJni(fileDescriptor, str);
    }

    public void BeginComposeFile() {
        String str = TAG;
        Log.d(str, "begin mComposefile:" + this.mComposeFile);
        BeginComposeFileJni();
    }

    public void CancelComposeFile() {
        String str = TAG;
        Log.d(str, "cancel mComposefile:" + this.mComposeFile);
        CancelComposeFileJni();
    }

    public int getCurrentComposePercent() {
        return getCurrentComposePercentJni();
    }

    public void SetComposeNotify(EffectNotifier effectNotifier) {
        String str = TAG;
        Log.d(str, "SetComposeNotify mComposefile:" + this.mComposeFile);
        SetComposeNotifyJni(effectNotifier);
    }

    public void SetParamentForGif(int i, int i2, float f, int i3, String str) {
        SetParamentForGifJni(i, i2, f, i3, str);
    }

    public void EnableComposeFile2Gif() {
        EnableComposeFile2GifJni();
    }
}
