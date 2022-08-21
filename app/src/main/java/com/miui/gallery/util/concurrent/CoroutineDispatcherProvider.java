package com.miui.gallery.util.concurrent;

import ch.qos.logback.core.CoreConstants;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineDispatcher;

/* compiled from: CoroutineDispatcherProvider.kt */
/* loaded from: classes2.dex */
public final class CoroutineDispatcherProvider {
    public final CoroutineDispatcher computation;

    /* renamed from: io  reason: collision with root package name */
    public final CoroutineDispatcher f1001io;
    public final CoroutineDispatcher main;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CoroutineDispatcherProvider)) {
            return false;
        }
        CoroutineDispatcherProvider coroutineDispatcherProvider = (CoroutineDispatcherProvider) obj;
        return Intrinsics.areEqual(this.f1001io, coroutineDispatcherProvider.f1001io) && Intrinsics.areEqual(this.computation, coroutineDispatcherProvider.computation) && Intrinsics.areEqual(this.main, coroutineDispatcherProvider.main);
    }

    public int hashCode() {
        return (((this.f1001io.hashCode() * 31) + this.computation.hashCode()) * 31) + this.main.hashCode();
    }

    public String toString() {
        return "CoroutineDispatcherProvider(io=" + this.f1001io + ", computation=" + this.computation + ", main=" + this.main + CoreConstants.RIGHT_PARENTHESIS_CHAR;
    }

    public CoroutineDispatcherProvider(CoroutineDispatcher io2, CoroutineDispatcher computation, CoroutineDispatcher main) {
        Intrinsics.checkNotNullParameter(io2, "io");
        Intrinsics.checkNotNullParameter(computation, "computation");
        Intrinsics.checkNotNullParameter(main, "main");
        this.f1001io = io2;
        this.computation = computation;
        this.main = main;
    }

    public final CoroutineDispatcher getIo() {
        return this.f1001io;
    }
}
