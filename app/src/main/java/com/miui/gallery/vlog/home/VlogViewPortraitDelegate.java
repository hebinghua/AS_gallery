package com.miui.gallery.vlog.home;

import android.app.Activity;
import com.miui.gallery.vlog.R$layout;

/* loaded from: classes2.dex */
public class VlogViewPortraitDelegate implements IVlogViewDelegate {
    public Activity mActivity;

    @Override // com.miui.gallery.vlog.home.IVlogViewDelegate
    public void release() {
    }

    @Override // com.miui.gallery.vlog.home.IVlogViewDelegate
    public void showEffectMenuAnimation(String str) {
    }

    public VlogViewPortraitDelegate(Activity activity) {
        this.mActivity = activity;
    }

    @Override // com.miui.gallery.vlog.home.IVlogViewDelegate
    public void setContentView() {
        this.mActivity.setContentView(R$layout.vlog_activity_layout);
    }
}
