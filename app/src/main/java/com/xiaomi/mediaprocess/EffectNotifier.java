package com.xiaomi.mediaprocess;

import android.graphics.Bitmap;

/* loaded from: classes3.dex */
public interface EffectNotifier {
    void OnProcessProgress(double d);

    void OnReceiveBitmap(Bitmap bitmap, long j);

    void OnReceiveFailed(int i);

    void OnReceiveFinish();

    void OnReceiveStatus(int i);

    void OnReceiveVideoFrame(byte[] bArr, int i, int i2, long j);
}
