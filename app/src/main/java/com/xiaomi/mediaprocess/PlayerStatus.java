package com.xiaomi.mediaprocess;

/* loaded from: classes3.dex */
public enum PlayerStatus {
    NONE_STATUS(0),
    SEEK_COMPLETE(1),
    SEEK_FAILED(2);
    
    private int nCode;

    PlayerStatus(int i) {
        this.nCode = i;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public int getCode() {
        return this.nCode;
    }

    public static PlayerStatus int2enum(int i) {
        PlayerStatus[] values;
        PlayerStatus playerStatus = NONE_STATUS;
        for (PlayerStatus playerStatus2 : values()) {
            if (playerStatus2.ordinal() == i) {
                playerStatus = playerStatus2;
            }
        }
        return playerStatus;
    }
}
