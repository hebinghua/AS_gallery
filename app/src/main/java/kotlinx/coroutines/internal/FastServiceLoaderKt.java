package kotlinx.coroutines.internal;

import kotlin.Result;
import kotlin.ResultKt;

/* compiled from: FastServiceLoader.kt */
/* loaded from: classes3.dex */
public final class FastServiceLoaderKt {
    public static final boolean ANDROID_DETECTED;

    static {
        Object m2569constructorimpl;
        try {
            Result.Companion companion = Result.Companion;
            m2569constructorimpl = Result.m2569constructorimpl(Class.forName("android.os.Build"));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            m2569constructorimpl = Result.m2569constructorimpl(ResultKt.createFailure(th));
        }
        ANDROID_DETECTED = Result.m2574isSuccessimpl(m2569constructorimpl);
    }

    public static final boolean getANDROID_DETECTED() {
        return ANDROID_DETECTED;
    }
}
