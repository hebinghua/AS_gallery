package androidx.tracing;

/* loaded from: classes.dex */
public final class TraceApi18Impl {
    public static void beginSection(String str) {
        android.os.Trace.beginSection(str);
    }

    public static void endSection() {
        android.os.Trace.endSection();
    }
}
