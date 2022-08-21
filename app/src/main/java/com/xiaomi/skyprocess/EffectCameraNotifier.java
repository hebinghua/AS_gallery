package com.xiaomi.skyprocess;

/* loaded from: classes3.dex */
public interface EffectCameraNotifier {
    void OnNeedStopRecording();

    void OnNotifyRender();

    void OnRecordFailed();

    void OnRecordFinish(String str);
}
