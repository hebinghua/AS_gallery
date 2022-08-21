package com.miui.gallery.util.market;

import android.content.BroadcastReceiver;

/* loaded from: classes2.dex */
public abstract class MarketDownloadReceiver extends BroadcastReceiver {
    public boolean isInstallExists(int i) {
        return i == -1;
    }

    public boolean isInstallFinish(int i) {
        return i == 4 || i == -2 || i == -3 || i == -4 || i == -5 || i == -6;
    }

    public boolean isInstallSuccess(int i) {
        return i == 4;
    }
}
