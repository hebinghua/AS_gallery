package com.milink.api.v1.type;

/* loaded from: classes.dex */
public enum DeviceType {
    Unknown,
    TV,
    Speaker,
    Miracast,
    Lelink,
    Bluetooth,
    MiPlay;

    public static DeviceType create(String str) {
        if (str.equalsIgnoreCase("airkan")) {
            return TV;
        }
        if (str.equalsIgnoreCase("airplay")) {
            return TV;
        }
        if (str.equalsIgnoreCase("airtunes")) {
            return Speaker;
        }
        if (str.equalsIgnoreCase("dlna.tv")) {
            return TV;
        }
        if (str.equalsIgnoreCase("dlna.speaker")) {
            return Speaker;
        }
        if (str.equalsIgnoreCase("miracast")) {
            return Miracast;
        }
        if (str.equalsIgnoreCase("lelink")) {
            return Lelink;
        }
        if (str.equalsIgnoreCase("bluetooth")) {
            return Bluetooth;
        }
        if (str.equalsIgnoreCase("miplay")) {
            return MiPlay;
        }
        return Unknown;
    }
}
