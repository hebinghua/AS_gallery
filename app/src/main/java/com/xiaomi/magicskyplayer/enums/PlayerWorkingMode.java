package com.xiaomi.magicskyplayer.enums;

/* loaded from: classes3.dex */
public enum PlayerWorkingMode {
    PlayerWorkingLipSyncMode(0),
    PlayerWorkingLowVideoDelayMode(1),
    PlayerWorkingVideoSmoothMode(2),
    PlayerWorkingDisableAudioDeviceMode(3);
    
    private int nCode;

    PlayerWorkingMode(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static PlayerWorkingMode int2enum(int i) {
        PlayerWorkingMode[] values;
        PlayerWorkingMode playerWorkingMode = PlayerWorkingLipSyncMode;
        for (PlayerWorkingMode playerWorkingMode2 : values()) {
            if (playerWorkingMode2.ordinal() == i) {
                playerWorkingMode = playerWorkingMode2;
            }
        }
        return playerWorkingMode;
    }
}
