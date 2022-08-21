package com.miui.mishare;

import miui.os.Build;

/* loaded from: classes3.dex */
public class MiShareDeviceModel {
    public static final boolean sSupportUwb = "odin".equals(Build.DEVICE);

    public static boolean supportUwb() {
        return sSupportUwb;
    }
}
