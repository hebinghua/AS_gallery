package kotlinx.coroutines.internal;

import java.lang.reflect.Constructor;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

/* compiled from: ExceptionsConstructor.kt */
/* loaded from: classes3.dex */
public final class ExceptionsConstructorKt$createSafeConstructor$$inlined$safeCtor$2 extends Lambda implements Function1<Throwable, Throwable> {
    public final /* synthetic */ Constructor $constructor$inlined;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ExceptionsConstructorKt$createSafeConstructor$$inlined$safeCtor$2(Constructor constructor) {
        super(1);
        this.$constructor$inlined = constructor;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Throwable mo2577invoke(Throwable th) {
        Object m2569constructorimpl;
        Object newInstance;
        try {
            Result.Companion companion = Result.Companion;
            newInstance = this.$constructor$inlined.newInstance(th);
        } catch (Throwable th2) {
            Result.Companion companion2 = Result.Companion;
            m2569constructorimpl = Result.m2569constructorimpl(ResultKt.createFailure(th2));
        }
        if (newInstance == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Throwable");
        }
        m2569constructorimpl = Result.m2569constructorimpl((Throwable) newInstance);
        if (Result.m2573isFailureimpl(m2569constructorimpl)) {
            m2569constructorimpl = null;
        }
        return (Throwable) m2569constructorimpl;
    }
}
