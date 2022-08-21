package com.xiaomi.mediaprocess;

import android.util.Log;
import java.util.Map;

/* loaded from: classes3.dex */
public class MediaEffectGraph {
    private static String TAG = "MediaEffectGraph";
    private long mGraphLine;

    private static native long AddAudioTrackJni(String str, long j, long j2, boolean z);

    private static native boolean AddEffectJni(long j, long j2);

    private static native void AddSourceAndEffectByTemplateJni(String[] strArr, String str);

    private static native boolean AddSourceMatrixJni(long j, float f, float f2, float f3, int i, int i2, boolean z);

    private static native boolean AddTransitionEffectJni(long j, long j2, long j3);

    private static native long AddVideoBackGroudJni(String str);

    private static native long AddVideoSourceJni(String str, long j, long j2);

    private static native void ChangeImageSourceSrcJni(long j, String str);

    private static native void ClearSourceMatrixJni(long j);

    private static native long ConstructMediaEffectGraphJni();

    private static native void DestructMediaEffectGraphJni();

    private static native boolean RemoveAudioTrackJni(long j);

    private static native boolean RemoveEffectJni(long j, long j2);

    private static native boolean RemoveTransitionEffectJni(long j, long j2, long j3);

    private static native boolean RemoveVideoBackGroudJni();

    private static native boolean RemoveVideoSourceJni(long j);

    private static native void SetAudioMuteJni(boolean z);

    private static native void SetCurrentSourceJni(long j);

    private static native boolean SetParamsForVideoSourceJni(long j, String[] strArr);

    private static native boolean SetVolumeForAudioSourceJni(long j, float f);

    private static native boolean SwapVideoSourceJni(long j, long j2);

    private static native void setUrlForAudioSourceJni(long j, long j2, long j3, String str);

    public MediaEffectGraph() {
        Log.d(TAG, " ");
        this.mGraphLine = 0L;
    }

    public long GetGraphLine() {
        return this.mGraphLine;
    }

    public void ConstructMediaEffectGraph() {
        this.mGraphLine = ConstructMediaEffectGraphJni();
        Log.d(TAG, "Construct: ");
    }

    public void DestructMediaEffectGraph() {
        Log.d(TAG, "Destruct");
        this.mGraphLine = 0L;
        DestructMediaEffectGraphJni();
    }

    public long AddVideoSource(String str, long j, long j2) {
        String str2 = TAG;
        Log.d(str2, "add video source: " + str + " seek " + j + " dura " + j2);
        if (this.mGraphLine == 0) {
            String str3 = TAG;
            Log.e(str3, "GraphLine is null, failed add video source: " + str);
            return 0L;
        }
        long AddVideoSourceJni = AddVideoSourceJni(str, j, j2);
        String str4 = TAG;
        Log.d(str4, "add video source: " + AddVideoSourceJni + "--" + str);
        return AddVideoSourceJni;
    }

    public boolean RemoveVideoSource(long j) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null , failed remove video source!");
            return false;
        }
        String str = TAG;
        Log.d(str, "remove video source: " + j);
        return RemoveVideoSourceJni(j);
    }

    public boolean SwapVideoSource(long j, long j2) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null , failed Swap video source!");
            return false;
        }
        String str = TAG;
        Log.d(str, "Swap video source: " + j + ", " + j2);
        return SwapVideoSourceJni(j, j2);
    }

    public boolean SetParamsForVideoSource(long j, Map<String, String> map) {
        String[] strArr;
        String str = TAG;
        Log.d(str, "set param for source, source id: " + j);
        int i = 0;
        if (map != null && map.size() != 0) {
            strArr = new String[map.size() * 2];
            for (String str2 : map.keySet()) {
                String str3 = TAG;
                Log.d(str3, "Param Map: <" + str2 + ", " + map.get(str2) + ">");
                int i2 = i * 2;
                strArr[i2] = str2.toLowerCase();
                strArr[i2 + 1] = map.get(str2);
                i++;
            }
        } else {
            Log.d(TAG, "Param Map: <null, null>");
            strArr = new String[0];
        }
        return SetParamsForVideoSourceJni(j, strArr);
    }

    public void ChangeImageSourceSrc(long j, String str) {
        String str2 = TAG;
        Log.d(str2, "ChangeImageSourceSrc: " + j + ", image path: " + str);
        ChangeImageSourceSrcJni(j, str);
    }

    public void setUrlForAudioSource(long j, long j2, long j3, String str) {
        String str2 = TAG;
        Log.d(str2, "setUrlForAudioSource: " + j + " st " + j2 + " seek " + j3 + ",url: " + str);
        setUrlForAudioSourceJni(j, j2, j3, str);
    }

    public long AddAudioTrack(String str, long j, long j2, boolean z) {
        String str2 = TAG;
        Log.d(str2, "add video source: " + str + " st " + j + " seek " + j2 + " play loop: " + z);
        if (this.mGraphLine == 0) {
            String str3 = TAG;
            Log.e(str3, "GraphLine is null, failed add audio source: " + str);
            return 0L;
        }
        long AddAudioTrackJni = AddAudioTrackJni(str, j, j2, z);
        String str4 = TAG;
        Log.d(str4, "add audio source: " + AddAudioTrackJni + "--" + str);
        return AddAudioTrackJni;
    }

    public void SetVolumeForAudioSource(long j, float f) {
        SetVolumeForAudioSourceJni(j, f);
    }

    public boolean RemoveAudioTrack(long j) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null , failed remove video source!");
            return false;
        }
        String str = TAG;
        Log.d(str, "remove video source: " + j);
        return RemoveAudioTrackJni(j);
    }

    public boolean AddEffect(long j, long j2) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null, add effect failed!");
            return false;
        }
        String str = TAG;
        Log.d(str, "add effect: " + j + ", " + j2);
        return AddEffectJni(j, j2);
    }

    public boolean RemoveEffect(long j, long j2) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null, remove effect failed!");
            return false;
        }
        String str = TAG;
        Log.d(str, "remove effect: " + j + ", " + j2);
        return RemoveEffectJni(j, j2);
    }

    public boolean AddEffect(long j, long j2, long j3) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null, add effect failed! ");
            return false;
        }
        String str = TAG;
        Log.d(str, "add effect: " + j + ", " + j2 + ", " + j3);
        return AddTransitionEffectJni(j, j2, j3);
    }

    public boolean RemoveEffect(long j, long j2, long j3) {
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null, remove effect failed!");
            return false;
        }
        String str = TAG;
        Log.d(str, "remove effect: " + j + ", " + j2 + ", " + j3);
        return RemoveTransitionEffectJni(j, j2, j3);
    }

    public void SetCurrentSource(long j) {
        String str = TAG;
        Log.d(str, "set current source id: " + j);
        SetCurrentSourceJni(j);
    }

    public void SetAudioMute(boolean z) {
        String str = TAG;
        Log.d(str, "SetAudioMute: " + z);
        SetAudioMuteJni(z);
    }

    public long AddVideoBackGroud(String str) {
        String str2 = TAG;
        Log.d(str2, "add video backgroud: " + str);
        if (this.mGraphLine == 0) {
            String str3 = TAG;
            Log.e(str3, "GraphLine is null, failed add video backgroud: " + str);
            return 0L;
        }
        return AddVideoBackGroudJni(str);
    }

    public boolean RemoveVideoBackGroud() {
        Log.d(TAG, "remove video backgroud");
        if (this.mGraphLine == 0) {
            Log.e(TAG, "GraphLine is null , failed remove video backgroud!");
            return false;
        }
        return RemoveVideoBackGroudJni();
    }

    public void AddSourceAndEffectByTemplate(String[] strArr, String str) {
        AddSourceAndEffectByTemplateJni(strArr, str);
    }

    public void AddSourceMatrix(long j, float f, float f2, float f3, int i, int i2, boolean z) {
        String str = TAG;
        Log.d(str, "add source matrix source:" + j + " scale:" + f + " rotate:" + f3 + " pos(" + i + "," + i2 + ") mirror:" + z);
        AddSourceMatrixJni(j, f, f2, f3, i, i2, z);
    }

    public void ClearSourceMatrix(long j) {
        String str = TAG;
        Log.d(str, "clear source matrix source:" + j);
        ClearSourceMatrixJni(j);
    }
}
