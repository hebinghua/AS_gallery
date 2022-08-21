package ch.qos.logback.core.util;

/* loaded from: classes.dex */
public interface InvocationGate {
    public static final long TIME_UNAVAILABLE = -1;

    boolean isTooSoon(long j);
}
