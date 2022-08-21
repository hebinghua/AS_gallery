package com.miui.gallery.arch.platform;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.arch.events.ActivityHosted;
import com.miui.gallery.arch.events.ContextHosted;
import com.miui.gallery.arch.events.FragmentHosted;
import com.miui.gallery.arch.events.ViewEvent;
import com.miui.gallery.arch.viewmodel.BaseViewModel;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BaseUIFragment.kt */
/* loaded from: classes.dex */
public abstract class BaseUIFragment<VM extends BaseViewModel> extends BaseFragment implements UIComponent<VM> {
    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startObserveEvents();
    }

    @Override // com.miui.gallery.arch.platform.UIComponent
    public void onReceiveEvent(ViewEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event instanceof ContextHosted) {
            Context requireContext = requireContext();
            Intrinsics.checkNotNullExpressionValue(requireContext, "requireContext()");
            ((ContextHosted) event).invoke(requireContext);
        } else if (event instanceof ActivityHosted) {
            FragmentActivity requireActivity = requireActivity();
            Intrinsics.checkNotNullExpressionValue(requireActivity, "requireActivity()");
            ((ActivityHosted) event).invoke(requireActivity);
        } else if (!(event instanceof FragmentHosted)) {
        } else {
            ((FragmentHosted) event).invoke(this);
        }
    }
}
