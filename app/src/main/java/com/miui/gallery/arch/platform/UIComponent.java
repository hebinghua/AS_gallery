package com.miui.gallery.arch.platform;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import com.miui.gallery.arch.events.ViewEvent;
import com.miui.gallery.arch.viewmodel.BaseViewModel;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: UIComponent.kt */
/* loaded from: classes.dex */
public interface UIComponent<VM extends BaseViewModel> extends LifecycleOwner {
    /* renamed from: $r8$lambda$t-AypKPT2J6GbyyJtteiwrVwl9g */
    static /* synthetic */ void m555$r8$lambda$tAypKPT2J6GbyyJtteiwrVwl9g(UIComponent uIComponent, ViewEvent viewEvent) {
        m556startObserveEvents$lambda0(uIComponent, viewEvent);
    }

    /* renamed from: getViewModel */
    VM mo617getViewModel();

    default void onReceiveEvent(ViewEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
    }

    /* renamed from: startObserveEvents$lambda-0 */
    static void m556startObserveEvents$lambda0(UIComponent this$0, ViewEvent it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullExpressionValue(it, "it");
        this$0.onReceiveEvent(it);
    }

    default void startObserveEvents() {
        mo617getViewModel().getViewEvents().observe(this, new Observer() { // from class: com.miui.gallery.arch.platform.UIComponent$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                UIComponent.m555$r8$lambda$tAypKPT2J6GbyyJtteiwrVwl9g(UIComponent.this, (ViewEvent) obj);
            }
        });
    }
}
