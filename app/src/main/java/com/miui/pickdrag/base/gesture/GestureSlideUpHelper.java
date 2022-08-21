package com.miui.pickdrag.base.gesture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: GestureSlideUpHelper.kt */
/* loaded from: classes3.dex */
public final class GestureSlideUpHelper extends BroadcastReceiver {
    public static final Companion Companion = new Companion(null);
    public final GestureSlideUpCallback gestureSlideUpCallback;

    public GestureSlideUpHelper(GestureSlideUpCallback gestureSlideUpCallback) {
        this.gestureSlideUpCallback = gestureSlideUpCallback;
    }

    /* compiled from: GestureSlideUpHelper.kt */
    /* loaded from: classes3.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void register(Context applicationContext) {
        Intrinsics.checkNotNullParameter(applicationContext, "applicationContext");
        applicationContext.registerReceiver(this, new IntentFilter("com.miui.home.fullScreenGesture.actionUp"));
    }

    public final void unregister(Context applicationContext) {
        Intrinsics.checkNotNullParameter(applicationContext, "applicationContext");
        applicationContext.unregisterReceiver(this);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        GestureSlideUpCallback gestureSlideUpCallback;
        if (intent != null && TextUtils.equals("com.miui.home.fullScreenGesture.actionUp", intent.getAction())) {
            int intExtra = intent.getIntExtra("actionUpSpeedAndDirection", -1);
            if ((intExtra != 5 && intExtra != 10) || (gestureSlideUpCallback = this.gestureSlideUpCallback) == null) {
                return;
            }
            gestureSlideUpCallback.onGestureSlideUp();
        }
    }
}
