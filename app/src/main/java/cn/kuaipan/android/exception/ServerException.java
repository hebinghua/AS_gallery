package cn.kuaipan.android.exception;

/* loaded from: classes.dex */
public class ServerException extends KscException {
    private static final long serialVersionUID = 6373467541984892922L;
    private final int statusCode;

    public static int validCode(int i) {
        if (i < 100 || i > 599) {
            return 0;
        }
        return i;
    }

    public ServerException(int i, String str) {
        super(validCode(i) + 503000, str);
        this.statusCode = i;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    @Override // cn.kuaipan.android.exception.KscException
    public String getSimpleMessage() {
        String str = getClass().getName() + "(ErrCode: " + getErrorCode() + "): StatusCode: " + this.statusCode;
        String str2 = this.detailMessage;
        if (str2 == null || str2.length() >= 100) {
            return str;
        }
        return str + ", " + this.detailMessage;
    }
}
