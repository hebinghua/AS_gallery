package com.miui.gallery.app.screenChange;

import android.content.res.Configuration;
import android.os.Bundle;

/* loaded from: classes.dex */
public interface IScreenChange {

    /* loaded from: classes.dex */
    public interface OnLargeScreenChangeListener extends IScreenChange {
        void onCreatedWhileLargeDevice(ScreenSize screenSize);

        void onCreatedWhileNormalDevice(ScreenSize screenSize);

        void onScreenSizeToLargeOrNormal(ScreenSize screenSize);
    }

    /* loaded from: classes.dex */
    public interface OnOrientationChangeListener extends IScreenChange {
        void onOrientationChange(Configuration configuration);
    }

    /* loaded from: classes.dex */
    public interface OnRestoreInstanceListener extends IScreenChange {
        void onRestoreInstance(Bundle bundle, Configuration configuration);
    }

    /* loaded from: classes.dex */
    public interface OnScreenLayoutSizeChangeListener extends IScreenChange {
        void onScreenLayoutSizeChange(Configuration configuration);
    }
}
