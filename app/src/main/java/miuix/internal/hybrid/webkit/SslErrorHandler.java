package miuix.internal.hybrid.webkit;

/* loaded from: classes3.dex */
public class SslErrorHandler extends miuix.hybrid.SslErrorHandler {
    public android.webkit.SslErrorHandler mSslErrorHandler;

    public SslErrorHandler(android.webkit.SslErrorHandler sslErrorHandler) {
        this.mSslErrorHandler = sslErrorHandler;
    }

    @Override // miuix.hybrid.SslErrorHandler
    public void proceed() {
        this.mSslErrorHandler.proceed();
    }

    @Override // miuix.hybrid.SslErrorHandler
    public void cancel() {
        this.mSslErrorHandler.cancel();
    }
}
