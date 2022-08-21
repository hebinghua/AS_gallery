package cn.kuaipan.android.exception;

/* loaded from: classes.dex */
public class KscRuntimeException extends RuntimeException implements IKscError {
    private static final long serialVersionUID = 4693852528580738850L;
    private final String detailMessage;
    private final int errCode;

    public KscRuntimeException(int i) {
        this(i, null, null);
    }

    public KscRuntimeException(int i, String str) {
        this(i, str, null);
    }

    public KscRuntimeException(int i, Throwable th) {
        this(i, th == null ? null : th.toString(), th);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public KscRuntimeException(int r4, java.lang.String r5, java.lang.Throwable r6) {
        /*
            r3 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "ErrCode:"
            r0.append(r1)
            r0.append(r4)
            if (r5 != 0) goto L12
            java.lang.String r1 = ""
            goto L23
        L12:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = " details:"
            r1.append(r2)
            r1.append(r5)
            java.lang.String r1 = r1.toString()
        L23:
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.Throwable r6 = cn.kuaipan.android.exception.KscException.getSerial(r6)
            r3.<init>(r0, r6)
            r3.errCode = r4
            r3.detailMessage = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuaipan.android.exception.KscRuntimeException.<init>(int, java.lang.String, java.lang.Throwable):void");
    }

    public int getErrorCode() {
        return this.errCode;
    }
}
