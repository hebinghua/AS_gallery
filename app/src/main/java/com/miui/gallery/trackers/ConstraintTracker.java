package com.miui.gallery.trackers;

import android.content.Context;
import com.miui.gallery.arch.internal.TaskExecutor;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ConstraintTracker.kt */
/* loaded from: classes2.dex */
public abstract class ConstraintTracker<T> {
    public static final Companion Companion = new Companion(null);
    public final Context appContext;
    public T currentState;
    public final Set<ConstraintListener<T>> listeners;
    public final Object lock;
    public final TaskExecutor taskExecutor;

    public static /* synthetic */ void $r8$lambda$y3AlOb_RldwMR4tnZg9RHHgCq24(List list, ConstraintTracker constraintTracker) {
        m1416setState$lambda3$lambda2(list, constraintTracker);
    }

    /* renamed from: getInitialState */
    public abstract T mo1417getInitialState();

    public abstract void startTracking();

    public abstract void stopTracking();

    public ConstraintTracker(Context context, TaskExecutor taskExecutor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(taskExecutor, "taskExecutor");
        this.taskExecutor = taskExecutor;
        Context applicationContext = context.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "context.applicationContext");
        this.appContext = applicationContext;
        this.lock = new Object();
        this.listeners = new LinkedHashSet();
    }

    public final TaskExecutor getTaskExecutor() {
        return this.taskExecutor;
    }

    public final Context getAppContext() {
        return this.appContext;
    }

    public final T getCurrentState() {
        return this.currentState;
    }

    public final void setCurrentState(T t) {
        this.currentState = t;
    }

    public final void registerListener(ConstraintListener<T> listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        synchronized (this.lock) {
            if (this.listeners.add(listener)) {
                if (this.listeners.size() == 1) {
                    setCurrentState(mo1417getInitialState());
                    DefaultLogger.d("ConstraintTracker", ((Object) getClass().getSimpleName()) + ": initial state = " + getCurrentState());
                    startTracking();
                }
                listener.onConstraintChanged(getCurrentState());
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void unregisterListener(ConstraintListener<T> listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        synchronized (this.lock) {
            if (this.listeners.remove(listener) && this.listeners.isEmpty()) {
                stopTracking();
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void setState(T t) {
        synchronized (this.lock) {
            if (Intrinsics.areEqual(getCurrentState(), t)) {
                return;
            }
            setCurrentState(t);
            final ArrayList arrayList = new ArrayList(this.listeners);
            getTaskExecutor().postToMainThread(new Runnable() { // from class: com.miui.gallery.trackers.ConstraintTracker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ConstraintTracker.$r8$lambda$y3AlOb_RldwMR4tnZg9RHHgCq24(arrayList, this);
                }
            });
            Unit unit = Unit.INSTANCE;
        }
    }

    /* renamed from: setState$lambda-3$lambda-2 */
    public static final void m1416setState$lambda3$lambda2(List listenersList, ConstraintTracker this$0) {
        Intrinsics.checkNotNullParameter(listenersList, "$listenersList");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Iterator it = listenersList.iterator();
        while (it.hasNext()) {
            ((ConstraintListener) it.next()).onConstraintChanged(this$0.currentState);
        }
    }

    /* compiled from: ConstraintTracker.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
