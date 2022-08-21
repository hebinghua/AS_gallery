package androidx.work;

import android.annotation.SuppressLint;

/* loaded from: classes.dex */
public interface Operation {
    @SuppressLint({"SyntheticAccessor"})
    public static final State.SUCCESS SUCCESS = new State.SUCCESS();
    @SuppressLint({"SyntheticAccessor"})
    public static final State.IN_PROGRESS IN_PROGRESS = new State.IN_PROGRESS();

    /* loaded from: classes.dex */
    public static abstract class State {

        /* loaded from: classes.dex */
        public static final class SUCCESS extends State {
            public String toString() {
                return "SUCCESS";
            }

            public SUCCESS() {
            }
        }

        /* loaded from: classes.dex */
        public static final class IN_PROGRESS extends State {
            public String toString() {
                return "IN_PROGRESS";
            }

            public IN_PROGRESS() {
            }
        }

        /* loaded from: classes.dex */
        public static final class FAILURE extends State {
            public final Throwable mThrowable;

            public FAILURE(Throwable exception) {
                this.mThrowable = exception;
            }

            public Throwable getThrowable() {
                return this.mThrowable;
            }

            public String toString() {
                return String.format("FAILURE (%s)", this.mThrowable.getMessage());
            }
        }
    }
}
