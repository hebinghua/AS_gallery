package com.xiaomi.magicskyplayer.enums;

/* loaded from: classes3.dex */
public enum PlayerCacheType {
    PlayerCacheNo(0),
    PlayerCacheFile(1),
    PlayerCacheMemory(2),
    PlayerCacheAll(3);
    
    private int nCode;

    PlayerCacheType(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static PlayerCacheType int2enum(int i) {
        PlayerCacheType[] values;
        PlayerCacheType playerCacheType = PlayerCacheNo;
        for (PlayerCacheType playerCacheType2 : values()) {
            if (playerCacheType2.ordinal() == i) {
                playerCacheType = playerCacheType2;
            }
        }
        return playerCacheType;
    }
}
