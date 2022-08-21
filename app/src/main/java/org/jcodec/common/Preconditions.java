package org.jcodec.common;

/* loaded from: classes3.dex */
public final class Preconditions {
    public static void checkState(boolean z) {
        if (z) {
            return;
        }
        throw new IllegalStateException();
    }
}
