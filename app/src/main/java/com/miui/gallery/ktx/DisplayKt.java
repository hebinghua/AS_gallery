package com.miui.gallery.ktx;

import android.graphics.Point;
import android.view.Display;
import android.view.View;
import androidx.core.view.ViewCompat;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DisplayX.kt */
/* loaded from: classes2.dex */
public final class DisplayKt {
    public static final Point getDisplaySize(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Display display = ViewCompat.getDisplay(view);
        Point point = new Point();
        if (display != null) {
            display.getSize(point);
        }
        return point;
    }

    public static final int getDisplayWidth(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Display display = ViewCompat.getDisplay(view);
        Point point = new Point();
        if (display != null) {
            display.getSize(point);
        }
        return point.x;
    }

    public static final int getDisplayHeight(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Display display = ViewCompat.getDisplay(view);
        Point point = new Point();
        if (display != null) {
            display.getSize(point);
        }
        return point.y;
    }
}
