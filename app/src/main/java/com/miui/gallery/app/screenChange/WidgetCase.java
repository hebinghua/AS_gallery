package com.miui.gallery.app.screenChange;

import android.content.res.Configuration;
import android.os.Bundle;

/* loaded from: classes.dex */
public abstract class WidgetCase {
    public static int CASE_EVENT_TYPE_HANDLE_INSTANCE_CHANGE = 1;
    public static int CASE_EVENT_TYPE_LARGE_SCREEN_CHANGE = 2;
    public static int CASE_EVENT_TYPE_ORIENTATION_CHANGE = 3;
    public static int CASE_EVENT_TYPE_SCREEN_LAYOUT_SIZE = 4;

    public abstract int getCaseType();

    public abstract void handleRestoreInstance(Bundle bundle, Configuration configuration);

    public abstract void handleWhenSaveInstance(Bundle bundle, Configuration configuration);

    public abstract boolean needHandle(ScreenConfig screenConfig, ScreenConfig screenConfig2);

    public abstract boolean needHandleInstance();

    public abstract void onScreenChange(ScreenSize screenSize, Configuration configuration);

    public final void dispatchScreenSizeChange(ScreenSize screenSize, Configuration configuration) {
        onScreenChange(screenSize, configuration);
    }
}
