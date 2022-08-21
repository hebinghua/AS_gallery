package com.xiaomi.mediaprocess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes3.dex */
public class MediaEffect {
    private static String TAG = "MediaEffect";
    private static EffectNotifier noti;
    private static EffectNotifier notify;

    public static native long CreateEffectJni(int i, String str);

    public static native long CreateSeEffectJni(int i, String str, String str2, int i2, int i3, int i4);

    public static native void DestoryEffectJni(long j);

    public static native void SetCoverCallbackJni(long j, EffectCoverNotifier effectCoverNotifier);

    public static native void SetFilterCallbackJni(long j, EffectNotifier effectNotifier);

    public static native void SetInternalFilterJni(long j, long j2);

    public static native boolean SetParamsForAudioTrackJni(long j, String[] strArr);

    public static native boolean SetParamsForEffectJni(int i, long j, String[] strArr);

    private static String getCpuName() {
        String readLine;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            do {
                readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return null;
                }
            } while (!readLine.contains("Hardware"));
            return readLine.split(":")[1];
        } catch (IOException unused) {
            return null;
        }
    }

    public static long CreateEffect(EffectType effectType, String str, int i, int i2) {
        String cpuName = getCpuName();
        String str2 = TAG;
        Log.d(str2, "create effect, is_lower: " + cpuName);
        long CreateSeEffectJni = CreateSeEffectJni(effectType.ordinal(), str, cpuName, 0, i, i2);
        String str3 = TAG;
        Log.d(str3, "create effect, type id: " + effectType + ", effect id:" + CreateSeEffectJni + ", model path " + str);
        return CreateSeEffectJni;
    }

    public static long CreateEffect(EffectType effectType) {
        String cpuName = getCpuName();
        String str = TAG;
        Log.d(str, "create effect, is_lower: " + cpuName);
        long CreateEffectJni = CreateEffectJni(effectType.ordinal(), cpuName);
        String str2 = TAG;
        Log.d(str2, "create effect, type id: " + effectType + ", effect id:" + CreateEffectJni);
        return CreateEffectJni;
    }

    public static void DestoryEffect(long j) {
        String str = TAG;
        Log.d(str, "destory effect id: " + j);
        DestoryEffectJni(j);
    }

    public static boolean SetParamsForEffect(EffectType effectType, long j, Map<String, String> map) {
        String[] strArr;
        String str = TAG;
        Log.d(str, "set param for effect, effect type: " + effectType + ", effect id: " + j);
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
        return SetParamsForEffectJni(effectType.ordinal(), j, strArr);
    }

    public static boolean SetParamsForAudioTrack(long j, Map<String, String> map) {
        String[] strArr;
        String str = TAG;
        Log.d(str, "set param for audio track  id: " + j);
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
        return SetParamsForAudioTrackJni(j, strArr);
    }

    public static void SetInternalFilter(long j, long j2) {
        String str = TAG;
        Log.d(str, "SetInternalFilterJni externalEffectID: " + j + " internalEffectID: " + j2);
        SetInternalFilterJni(j, j2);
    }

    public static void SetCoverCallback(long j, EffectCoverNotifier effectCoverNotifier) {
        Log.d(TAG, "set EffectCoverCallback");
        SetCoverCallbackJni(j, effectCoverNotifier);
    }

    public static void SetFilterCallback(long j, EffectNotifier effectNotifier) {
        Log.d(TAG, "Register filter callback");
        notify = effectNotifier;
        EffectNotifier effectNotifier2 = new EffectNotifier() { // from class: com.xiaomi.mediaprocess.MediaEffect.1
            @Override // com.xiaomi.mediaprocess.EffectNotifier
            public void OnReceiveBitmap(Bitmap bitmap, long j2) {
                MediaEffect.notify.OnReceiveBitmap(bitmap, j2);
            }

            @Override // com.xiaomi.mediaprocess.EffectNotifier
            public void OnReceiveVideoFrame(byte[] bArr, int i, int i2, long j2) {
                try {
                    YuvImage yuvImage = new YuvImage(bArr, 17, i, i2, null);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    yuvImage.compressToJpeg(new Rect(0, 0, i, i2), 80, byteArrayOutputStream);
                    MediaEffect.notify.OnReceiveBitmap(BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size()), j2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.xiaomi.mediaprocess.EffectNotifier
            public void OnReceiveStatus(int i) {
                MediaEffect.notify.OnReceiveStatus(i);
            }

            @Override // com.xiaomi.mediaprocess.EffectNotifier
            public void OnReceiveFinish() {
                MediaEffect.notify.OnReceiveFinish();
            }

            @Override // com.xiaomi.mediaprocess.EffectNotifier
            public void OnReceiveFailed(int i) {
                MediaEffect.notify.OnReceiveFailed(i);
            }

            @Override // com.xiaomi.mediaprocess.EffectNotifier
            public void OnProcessProgress(double d) {
                MediaEffect.notify.OnProcessProgress(d);
            }
        };
        noti = effectNotifier2;
        SetFilterCallbackJni(j, effectNotifier2);
    }
}
