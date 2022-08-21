package com.miui.gallery.arch.function;

import com.miui.gallery.arch.function.Either;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Either.kt */
/* loaded from: classes.dex */
public final class EitherKt {
    public static final <L, R> R getOrElse(Either<? extends L, ? extends R> either, R r) {
        Intrinsics.checkNotNullParameter(either, "<this>");
        if (either instanceof Either.Right) {
            return (R) ((Either.Right) either).getValue();
        }
        throw new NoWhenBranchMatchedException();
    }
}
