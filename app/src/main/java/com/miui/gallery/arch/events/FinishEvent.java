package com.miui.gallery.arch.events;

import androidx.fragment.app.FragmentActivity;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ViewEvents.kt */
/* loaded from: classes.dex */
public final class FinishEvent extends ViewEvent implements ActivityHosted {
    @Override // com.miui.gallery.arch.events.ActivityHosted
    public void invoke(FragmentActivity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        activity.finish();
    }
}
