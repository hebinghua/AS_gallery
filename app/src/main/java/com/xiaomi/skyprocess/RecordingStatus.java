package com.xiaomi.skyprocess;

/* loaded from: classes3.dex */
public enum RecordingStatus {
    RecordingStopped(0),
    RecordingPlaying(1),
    RecordingPaused(2);
    
    private int nCode;

    RecordingStatus(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static RecordingStatus int2enum(int i) {
        RecordingStatus[] values;
        RecordingStatus recordingStatus = RecordingStopped;
        for (RecordingStatus recordingStatus2 : values()) {
            if (recordingStatus2.ordinal() == i) {
                recordingStatus = recordingStatus2;
            }
        }
        return recordingStatus;
    }
}
