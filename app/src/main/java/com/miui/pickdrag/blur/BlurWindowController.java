package com.miui.pickdrag.blur;

import miuix.appcompat.app.AppCompatActivity;

/* compiled from: BlurWindowController.kt */
/* loaded from: classes3.dex */
public final class BlurWindowController extends BlurController {
    public boolean isBlurOpen;

    @Override // com.miui.pickdrag.blur.BlurController
    public void setBlurEnabled(boolean z) {
        AppCompatActivity activity;
        if (this.isBlurOpen == z || (activity = getActivity()) == null) {
            return;
        }
        if (z) {
            activity.getWindow().setFlags(4, 4);
        } else {
            activity.getWindow().clearFlags(4);
        }
        this.isBlurOpen = z;
    }

    @Override // com.miui.pickdrag.blur.BlurController
    public void setBlurAlpha(float f) {
        AppCompatActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        this.isBlurOpen = MIUIWindowSettings.INSTANCE.setBlurRatio(activity.getWindow(), f);
    }
}
