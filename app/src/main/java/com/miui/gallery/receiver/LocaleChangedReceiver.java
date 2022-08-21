package com.miui.gallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.uil.ShortCutHelper;

/* loaded from: classes2.dex */
public class LocaleChangedReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        AlbumSortHelper.init();
        ShortCutHelper.updateRecommendShortcut();
        ShortCutHelper.updateSlimShortcut();
    }
}
