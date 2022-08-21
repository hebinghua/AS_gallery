package androidx.lifecycle;

import android.view.View;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: View.kt */
/* loaded from: classes.dex */
public final class ViewKt {
    public static final LifecycleOwner findViewTreeLifecycleOwner(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        return ViewTreeLifecycleOwner.get(view);
    }
}
