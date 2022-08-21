package com.miui.gallery.vlog.base.interfaces;

import android.content.Intent;
import com.miui.gallery.vlog.home.VlogPresenter;

/* loaded from: classes2.dex */
public interface IVlogActivity {
    VlogPresenter getVlogPresenter();

    void onBack();

    void onInitFailed();

    void onSave(boolean z, String str);

    void onSaved(boolean z, Intent intent);
}
