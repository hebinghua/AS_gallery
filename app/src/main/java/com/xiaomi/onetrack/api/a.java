package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.Configuration;

/* loaded from: classes3.dex */
public class a {
    public static int a(Configuration configuration) {
        if (configuration == null) {
            return 0;
        }
        boolean isGAIDEnable = configuration.isGAIDEnable();
        if (configuration.isIMSIEnable()) {
            isGAIDEnable |= true;
        }
        if (configuration.isIMEIEnable()) {
            boolean z = isGAIDEnable ? 1 : 0;
            char c = isGAIDEnable ? 1 : 0;
            isGAIDEnable = z | true;
        }
        if (configuration.isExceptionCatcherEnable()) {
            boolean z2 = isGAIDEnable ? 1 : 0;
            char c2 = isGAIDEnable ? 1 : 0;
            isGAIDEnable = z2 | true;
        }
        if (!configuration.isOverrideMiuiRegionSetting()) {
            int i = isGAIDEnable ? 1 : 0;
            int i2 = isGAIDEnable ? 1 : 0;
            int i3 = isGAIDEnable ? 1 : 0;
            return i;
        }
        return (isGAIDEnable ? 1 : 0) | 16;
    }
}
