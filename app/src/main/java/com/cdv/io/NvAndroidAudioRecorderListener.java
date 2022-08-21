package com.cdv.io;

import com.cdv.io.NvAndroidAudioRecorder;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class NvAndroidAudioRecorderListener implements NvAndroidAudioRecorder.RecordDataCallback {
    private int m_id;

    private static native void audioRecordDataReady(int i, ByteBuffer byteBuffer, int i2);

    public NvAndroidAudioRecorderListener(int i) {
        this.m_id = -1;
        this.m_id = i;
    }

    @Override // com.cdv.io.NvAndroidAudioRecorder.RecordDataCallback
    public void onAudioRecordDataArrived(ByteBuffer byteBuffer, int i) {
        audioRecordDataReady(this.m_id, byteBuffer, i);
    }
}
