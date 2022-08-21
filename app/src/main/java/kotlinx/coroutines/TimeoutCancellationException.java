package kotlinx.coroutines;

import java.util.concurrent.CancellationException;

/* compiled from: Timeout.kt */
/* loaded from: classes3.dex */
public final class TimeoutCancellationException extends CancellationException implements CopyableThrowable<TimeoutCancellationException> {
    public final Job coroutine;

    public TimeoutCancellationException(String str, Job job) {
        super(str);
        this.coroutine = job;
    }

    public TimeoutCancellationException(String str) {
        this(str, null);
    }

    @Override // kotlinx.coroutines.CopyableThrowable
    public TimeoutCancellationException createCopy() {
        String message = getMessage();
        if (message == null) {
            message = "";
        }
        TimeoutCancellationException timeoutCancellationException = new TimeoutCancellationException(message, this.coroutine);
        timeoutCancellationException.initCause(this);
        return timeoutCancellationException;
    }
}
