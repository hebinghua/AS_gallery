package org.keyczar.enums;

/* loaded from: classes3.dex */
public enum KeyStatus {
    PRIMARY(0, "primary"),
    ACTIVE(1, "active"),
    INACTIVE(2, "inactive");
    
    private String name;
    private int value;

    KeyStatus(int i, String str) {
        this.value = i;
        this.name = str;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static KeyStatus getStatus(int i) {
        if (i != 0) {
            if (i == 1) {
                return ACTIVE;
            }
            if (i == 2) {
                return INACTIVE;
            }
            return null;
        }
        return PRIMARY;
    }

    public static KeyStatus getStatus(String str) {
        if (str != null) {
            KeyStatus keyStatus = PRIMARY;
            if (str.equalsIgnoreCase(keyStatus.getName())) {
                return keyStatus;
            }
            KeyStatus keyStatus2 = ACTIVE;
            if (str.equalsIgnoreCase(keyStatus2.getName())) {
                return keyStatus2;
            }
            KeyStatus keyStatus3 = INACTIVE;
            if (str.equalsIgnoreCase(keyStatus3.getName())) {
                return keyStatus3;
            }
        }
        return ACTIVE;
    }
}
