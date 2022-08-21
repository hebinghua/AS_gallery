package cn.kuaipan.android.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

/* loaded from: classes.dex */
public class KscException extends Exception implements IKscError {
    private static final long serialVersionUID = 7461260166746901326L;
    public final String detailMessage;
    private final int errCode;

    public KscException(int i) {
        this(i, null, null);
    }

    public KscException(int i, String str) {
        this(i, str, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public KscException(int r4, java.lang.String r5, java.lang.Throwable r6) {
        /*
            r3 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "ErrCode: "
            r0.append(r1)
            r0.append(r4)
            if (r5 != 0) goto L12
            java.lang.String r1 = ""
            goto L23
        L12:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "\n"
            r1.append(r2)
            r1.append(r5)
            java.lang.String r1 = r1.toString()
        L23:
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.Throwable r6 = getSerial(r6)
            r3.<init>(r0, r6)
            r3.errCode = r4
            r3.detailMessage = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuaipan.android.exception.KscException.<init>(int, java.lang.String, java.lang.Throwable):void");
    }

    public int getErrorCode() {
        return this.errCode;
    }

    public String getSimpleMessage() {
        String str = getClass().getName() + "(ErrCode: " + this.errCode + ")";
        String str2 = this.detailMessage;
        if (str2 == null || str2.length() >= 100) {
            return str;
        }
        return str + ": " + this.detailMessage;
    }

    public static KscException newException(Throwable th, String str) throws InterruptedException {
        Throwable transformIfStopByCallerException = ErrorHelper.transformIfStopByCallerException(th);
        if (transformIfStopByCallerException instanceof KscException) {
            return (KscException) transformIfStopByCallerException;
        }
        ErrorHelper.handleInterruptException(transformIfStopByCallerException);
        if (transformIfStopByCallerException instanceof ConnectException) {
            return new NetworkException(504111, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof SocketException) {
            return new NetworkException(504000, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof SocketTimeoutException) {
            return new NetworkException(504400, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof ConnectTimeoutException) {
            return new NetworkException(504110, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof ClientProtocolException) {
            return new NetworkException(504500, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof UnknownHostException) {
            return new NetworkException(504501, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof InvalidKeyException) {
            return new KscException(500009, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof FileNotFoundException) {
            return new KscException(403001, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof IOException) {
            return new KscException(403000, str, transformIfStopByCallerException);
        }
        if (transformIfStopByCallerException instanceof RuntimeException) {
            throw ((RuntimeException) transformIfStopByCallerException);
        }
        return new KscException(403999, str, transformIfStopByCallerException);
    }

    public static Throwable getSerial(Throwable th) {
        return (th != null && (th instanceof HttpHostConnectException)) ? new HttpHostConnectExceptionWrapper((HttpHostConnectException) th) : th;
    }
}
