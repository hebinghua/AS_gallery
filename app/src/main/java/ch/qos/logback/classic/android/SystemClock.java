package ch.qos.logback.classic.android;

/* loaded from: classes.dex */
final class SystemClock implements Clock {
    @Override // ch.qos.logback.classic.android.Clock
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
