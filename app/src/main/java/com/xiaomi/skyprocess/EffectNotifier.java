package com.xiaomi.skyprocess;

/* loaded from: classes3.dex */
public interface EffectNotifier {
    void OnReceiveCacheFinished();

    void OnReceiveFailed();

    void OnReceiveFinish();

    void OnReceiveFrameInfo(int i, int i2);

    void onReceiveProgressPercent(int i);
}
