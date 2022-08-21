package com.miui.pickdrag.blur;

import java.lang.ref.WeakReference;
import kotlin.jvm.internal.Intrinsics;
import miuix.appcompat.app.AppCompatActivity;

/* compiled from: BlurController.kt */
/* loaded from: classes3.dex */
public abstract class BlurController {
    public WeakReference<AppCompatActivity> activityRef;

    public abstract void setBlurAlpha(float f);

    public abstract void setBlurEnabled(boolean z);

    public final void setActivity(AppCompatActivity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        this.activityRef = new WeakReference<>(activity);
    }

    public final AppCompatActivity getActivity() {
        AppCompatActivity appCompatActivity;
        WeakReference<AppCompatActivity> weakReference = this.activityRef;
        if (weakReference == null || (appCompatActivity = weakReference.get()) == null) {
            return null;
        }
        return appCompatActivity;
    }
}
