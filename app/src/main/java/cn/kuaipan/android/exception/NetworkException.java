package cn.kuaipan.android.exception;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class NetworkException extends KscException {
    private static final long serialVersionUID = 3410936099313815279L;
    private final String origMessage;

    public NetworkException(int i, String str, Throwable th) {
        super(i, str, th);
        this.origMessage = th == null ? null : th.getMessage();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (TextUtils.isEmpty(this.origMessage)) {
            return super.getMessage();
        }
        return this.origMessage + "\n" + super.getMessage();
    }

    @Override // cn.kuaipan.android.exception.KscException
    public String getSimpleMessage() {
        Throwable cause;
        String str = getClass().getName() + "(ErrCode: " + getErrorCode() + ")";
        if (getCause() != null) {
            String str2 = str + " - [" + cause.getClass().getName();
            if (this.origMessage != null) {
                str2 = str2 + ": " + this.origMessage;
            }
            str = str2 + "]";
        }
        String str3 = this.detailMessage;
        if (str3 == null || str3.length() >= 100) {
            return str;
        }
        return str + ": " + this.detailMessage;
    }
}
