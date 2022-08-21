package kotlin.coroutines.jvm.internal;

/* compiled from: boxing.kt */
/* loaded from: classes3.dex */
public final class Boxing {
    public static final Boolean boxBoolean(boolean z) {
        return Boolean.valueOf(z);
    }

    public static final Integer boxInt(int i) {
        return new Integer(i);
    }

    public static final Long boxLong(long j) {
        return new Long(j);
    }
}
