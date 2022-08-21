package com.miui.gallery.sdk.editor;

import miui.external.SdkHelper;

/* loaded from: classes2.dex */
public interface Constants {
    public static final String EXTRA_IS_LONG_SCREENSHOT;
    public static final String EXTRA_IS_SCREENSHOT;

    static {
        SdkHelper.isMiuiSystem();
        EXTRA_IS_SCREENSHOT = "IsScreenshot";
        SdkHelper.isMiuiSystem();
        EXTRA_IS_LONG_SCREENSHOT = "IsLongScreenshot";
    }
}
