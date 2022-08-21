package dagger.hilt.internal;

import java.util.Objects;

/* loaded from: classes3.dex */
public final class Preconditions {
    public static <T> T checkNotNull(T reference) {
        Objects.requireNonNull(reference);
        return reference;
    }

    public static <T> T checkNotNull(T reference, String errorMessage) {
        Objects.requireNonNull(reference, errorMessage);
        return reference;
    }

    public static void checkState(boolean expression, String errorMessageTemplate, Object... args) {
        if (expression) {
            return;
        }
        throw new IllegalStateException(String.format(errorMessageTemplate, args));
    }
}
