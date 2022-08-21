package com.miui.gallery.scanner.provider.resolver;

import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.scanner.core.ScanContracts$Mode;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: IScanMethodResolver.kt */
/* loaded from: classes2.dex */
public abstract class IScanMethodResolver {
    public static final Companion Companion = new Companion(null);
    public final Lazy currentMode$delegate = LazyKt__LazyJVMKt.lazy(IScanMethodResolver$currentMode$2.INSTANCE);

    public abstract boolean handles(String str);

    public abstract Bundle resolve(Context context, Bundle bundle);

    public final ScanContracts$Mode getCurrentMode() {
        return (ScanContracts$Mode) this.currentMode$delegate.mo119getValue();
    }

    /* compiled from: IScanMethodResolver.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
