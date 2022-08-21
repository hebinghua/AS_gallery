package com.miui.gallery.arch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.miui.gallery.arch.events.ViewEvent;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BaseViewModel.kt */
/* loaded from: classes.dex */
public abstract class BaseViewModel extends ViewModel {
    public static final Companion Companion = new Companion(null);
    public final MutableLiveData<ViewEvent> _viewEvents = new MutableLiveData<>();

    public final LiveData<ViewEvent> getViewEvents() {
        return this._viewEvents;
    }

    public final <Event extends ViewEvent> void publish(Event event) {
        Intrinsics.checkNotNullParameter(event, "<this>");
        this._viewEvents.postValue(event);
    }

    /* compiled from: BaseViewModel.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
