package androidx.sqlite.db;

/* loaded from: classes.dex */
public final class CursorSpec {
    public static final CursorSpec FORWARD_ONLY = new CursorSpec(true);
    public final boolean forwardOnly;
    public final int windowSizeBytes = 0;

    public CursorSpec(boolean z) {
        this.forwardOnly = z;
    }

    public boolean isForwardOnly() {
        return this.forwardOnly;
    }

    public int getWindowSizeBytes() {
        return this.windowSizeBytes;
    }
}
