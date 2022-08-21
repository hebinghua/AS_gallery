package com.xiaomi.milab.videosdk.message;

/* loaded from: classes3.dex */
public interface TranscodeCallback {
    void onTranscodeCancel();

    void onTranscodeFail();

    void onTranscodeProgress(int i);

    void onTranscodeSuccess();
}
