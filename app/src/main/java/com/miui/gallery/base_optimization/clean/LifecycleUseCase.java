package com.miui.gallery.base_optimization.clean;

import androidx.lifecycle.Lifecycle;
import com.miui.gallery.base_optimization.clean.lifecycle.UseCaseLifecycleManager;
import com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor;
import com.miui.gallery.base_optimization.clean.thread.SubScribeThreadExecutor;
import io.reactivex.subscribers.DisposableSubscriber;

/* loaded from: classes.dex */
public abstract class LifecycleUseCase<T, Param> extends UseCase<T, Param> implements UseCaseLifecycleManager.onLifecycleEventCallback {
    public void onStart() {
    }

    public void onStop() {
    }

    public LifecycleUseCase(SubScribeThreadExecutor subScribeThreadExecutor, ObserveThreadExecutor observeThreadExecutor) {
        super(subScribeThreadExecutor, observeThreadExecutor);
    }

    public void executeWith(DisposableSubscriber<T> disposableSubscriber, Param param, Object obj) {
        execute(disposableSubscriber, param);
        UseCaseLifecycleManager.getInstance().registerLifecycle(obj, this);
    }

    public void onDestroy() {
        dispose();
    }

    /* renamed from: com.miui.gallery.base_optimization.clean.LifecycleUseCase$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$androidx$lifecycle$Lifecycle$Event;

        static {
            int[] iArr = new int[Lifecycle.Event.values().length];
            $SwitchMap$androidx$lifecycle$Lifecycle$Event = iArr;
            try {
                iArr[Lifecycle.Event.ON_STOP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_START.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_DESTROY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // com.miui.gallery.base_optimization.clean.lifecycle.UseCaseLifecycleManager.onLifecycleEventCallback
    public void onEvent(Lifecycle.Event event) {
        int i = AnonymousClass1.$SwitchMap$androidx$lifecycle$Lifecycle$Event[event.ordinal()];
        if (i == 1) {
            onStop();
        } else if (i == 2) {
            onStart();
        } else if (i != 3) {
        } else {
            onDestroy();
        }
    }
}
