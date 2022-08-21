package com.miui.gallery.arch.interactor;

import com.miui.gallery.arch.function.Either;
import kotlin.coroutines.Continuation;

/* compiled from: UseCase.kt */
/* loaded from: classes.dex */
public abstract class UseCase<RESULT, PARAM> {

    /* compiled from: UseCase.kt */
    /* loaded from: classes.dex */
    public static final class None {
    }

    public abstract Object run(PARAM param, Continuation<? super Either<Object, ? extends RESULT>> continuation);

    public final Object invoke(PARAM param, Continuation<? super Either<Object, ? extends RESULT>> continuation) {
        return run(param, continuation);
    }
}
