package com.xiaomi.magicskyplayer.enums;

/* loaded from: classes3.dex */
public enum PlayerSeekingMode {
    PlayerSeekingNormalMode(0),
    PlayerSeekingFastMode(1),
    PlayerSeekingPreciseMode(2);
    
    private int nCode;

    PlayerSeekingMode(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static PlayerSeekingMode int2enum(int i) {
        PlayerSeekingMode[] values;
        PlayerSeekingMode playerSeekingMode = PlayerSeekingNormalMode;
        for (PlayerSeekingMode playerSeekingMode2 : values()) {
            if (playerSeekingMode2.ordinal() == i) {
                playerSeekingMode = playerSeekingMode2;
            }
        }
        return playerSeekingMode;
    }
}
