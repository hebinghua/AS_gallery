package com.xiaomi.magicskyplayer.enums;

/* loaded from: classes3.dex */
public enum PlayerScalingMode {
    PlayerScalingModeNone(0),
    PlayerScalingModeAspectFit(1),
    PlayerScalingModeAspectFill(2),
    PlayerScalingModeFill(3);
    
    private int nCode;

    PlayerScalingMode(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static PlayerScalingMode int2enum(int i) {
        PlayerScalingMode[] values;
        PlayerScalingMode playerScalingMode = PlayerScalingModeNone;
        for (PlayerScalingMode playerScalingMode2 : values()) {
            if (playerScalingMode2.ordinal() == i) {
                playerScalingMode = playerScalingMode2;
            }
        }
        return playerScalingMode;
    }
}
