package cn.kuaipan.android.exception;

/* loaded from: classes.dex */
public class SessionExpiredException extends KscException {
    public final long retryAfterInMillis;

    public SessionExpiredException() {
        super(220601);
        this.retryAfterInMillis = 5000L;
    }

    public SessionExpiredException(String str) {
        super(220601, str);
        this.retryAfterInMillis = 5000L;
    }
}
