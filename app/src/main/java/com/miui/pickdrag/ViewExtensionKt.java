package com.miui.pickdrag;

import android.view.MotionEvent;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ViewExtension.kt */
/* loaded from: classes3.dex */
public final class ViewExtensionKt {
    public static final boolean isMotionEventIn(View view, MotionEvent event) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Intrinsics.checkNotNullParameter(event, "event");
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i = iArr[0];
        int width = view.getWidth() + i;
        int i2 = iArr[1];
        return ((float) i) <= event.getRawX() && event.getRawX() <= ((float) width) && ((float) i2) <= event.getRawY() && event.getRawY() <= ((float) (view.getHeight() + i2));
    }
}
