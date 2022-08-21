package cn.kuaipan.android.http.client;

import android.os.SystemClock;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public class KscHttpRequestRetryHandler implements HttpRequestRetryHandler {
    public final int errorTimeOut;
    public final boolean requestSentRetryEnabled;
    public final int retryCount;

    public KscHttpRequestRetryHandler(int i, boolean z, int i2) {
        this.retryCount = i;
        this.requestSentRetryEnabled = z;
        this.errorTimeOut = i2;
    }

    public boolean retryRequest(IOException iOException, int i, HttpContext httpContext) {
        if (iOException != null) {
            if (httpContext == null) {
                throw new IllegalArgumentException("HTTP context may not be null");
            }
            if (i > this.retryCount) {
                return false;
            }
            if (iOException instanceof NoHttpResponseException) {
                return true;
            }
            if ((iOException instanceof InterruptedIOException) || (iOException instanceof UnknownHostException) || (iOException instanceof SSLHandshakeException)) {
                return false;
            }
            Boolean bool = (Boolean) httpContext.getAttribute("http.request_sent");
            Long l = (Long) httpContext.getAttribute("ksc.connect_start");
            RequestWrapper requestWrapper = (RequestWrapper) httpContext.getAttribute("http.request");
            URIRedirector uRIRedirector = (URIRedirector) httpContext.getAttribute("ksc.connect_redirector");
            boolean z = bool != null && bool.booleanValue();
            long longValue = l != null ? l.longValue() : 0L;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (z && ((!this.requestSentRetryEnabled || !requestWrapper.isRepeatable()) && (!(iOException instanceof SocketException) || elapsedRealtime - longValue > this.errorTimeOut))) {
                return false;
            }
            if (uRIRedirector == null) {
                return true;
            }
            return uRIRedirector.redirect(httpContext);
        }
        throw new IllegalArgumentException("Exception parameter may not be null");
    }
}
