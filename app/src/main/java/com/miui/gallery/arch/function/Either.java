package com.miui.gallery.arch.function;

import ch.qos.logback.core.CoreConstants;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Either.kt */
/* loaded from: classes.dex */
public abstract class Either<L, R> {
    public /* synthetic */ Either(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public Either() {
    }

    /* compiled from: Either.kt */
    /* loaded from: classes.dex */
    public static final class Right<R> extends Either {
        public final boolean isRight;
        public final R value;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof Right) && Intrinsics.areEqual(this.value, ((Right) obj).value);
        }

        public int hashCode() {
            R r = this.value;
            if (r == null) {
                return 0;
            }
            return r.hashCode();
        }

        public String toString() {
            return "Right(value=" + this.value + CoreConstants.RIGHT_PARENTHESIS_CHAR;
        }

        public Right(R r) {
            super(null);
            this.value = r;
            this.isRight = true;
        }

        public final R getValue() {
            return this.value;
        }
    }
}
