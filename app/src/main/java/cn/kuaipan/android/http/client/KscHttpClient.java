package cn.kuaipan.android.http.client;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.util.FileSize;
import cn.kuaipan.android.utils.ContextUtils;
import com.xiaomi.stat.d.i;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class KscHttpClient extends DefaultHttpClient {
    public static String sUserAgent;
    public volatile LoggingConfiguration curlConfiguration;
    public final boolean mForKssTransmission;
    public final boolean mKeepAlive;
    public RuntimeException mLeakedException;
    public final boolean mRequestSentRetryEnabled;

    /* loaded from: classes.dex */
    public static class LoggingConfiguration {
    }

    public static KscHttpClient newInstance(String str, boolean z, boolean z2) {
        HttpParams httpParams = getHttpParams(str);
        return new KscHttpClient(getClientConnectionManager(httpParams), httpParams, z, z2);
    }

    public static KscHttpClient newKssInstance(String str) {
        HttpParams httpParams = getHttpParams(str);
        return new KscHttpClient(getClientConnectionManager(httpParams), httpParams);
    }

    public static HttpParams getHttpParams(String str) {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, i.b);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 30000);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        ConnManagerParams.setTimeout(basicHttpParams, 30000L);
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 30);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRoute() { // from class: cn.kuaipan.android.http.client.KscHttpClient.1
            public int getMaxForRoute(HttpRoute httpRoute) {
                return 32;
            }
        });
        HttpClientParams.setRedirecting(basicHttpParams, true);
        HttpProtocolParams.setUseExpectContinue(basicHttpParams, false);
        HttpProtocolParams.setContentCharset(basicHttpParams, Keyczar.DEFAULT_ENCODING);
        HttpProtocolParams.setHttpElementCharset(basicHttpParams, Keyczar.DEFAULT_ENCODING);
        if (TextUtils.isEmpty(str)) {
            str = getDefaultUserAgent();
        }
        HttpProtocolParams.setUserAgent(basicHttpParams, str);
        return basicHttpParams;
    }

    public static ClientConnectionManager getClientConnectionManager(HttpParams httpParams) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        return new ThreadSafeClientConnManager(httpParams, schemeRegistry);
    }

    public static String getDefaultUserAgent() {
        if (sUserAgent == null) {
            sUserAgent = String.format("KscClient/1.0 (Linux; U; Android %s)", ContextUtils.getFrameworkVersion());
        }
        return sUserAgent;
    }

    public KscHttpClient(ClientConnectionManager clientConnectionManager, HttpParams httpParams, boolean z, boolean z2) {
        super(clientConnectionManager, httpParams);
        this.mLeakedException = new IllegalStateException("AndroidHttpClient created and never closed");
        this.mForKssTransmission = false;
        this.mKeepAlive = z;
        this.mRequestSentRetryEnabled = z2;
    }

    public KscHttpClient(ClientConnectionManager clientConnectionManager, HttpParams httpParams) {
        super(clientConnectionManager, httpParams);
        this.mLeakedException = new IllegalStateException("AndroidHttpClient created and never closed");
        this.mForKssTransmission = true;
        this.mKeepAlive = true;
        this.mRequestSentRetryEnabled = true;
    }

    public BasicHttpProcessor createHttpProcessor() {
        BasicHttpProcessor createHttpProcessor;
        if (this.mForKssTransmission) {
            createHttpProcessor = new BasicHttpProcessor();
            createHttpProcessor.addInterceptor(new RequestDefaultHeaders());
            createHttpProcessor.addInterceptor(new RequestContent());
            createHttpProcessor.addInterceptor(new RequestTargetHost());
            createHttpProcessor.addInterceptor(new RequestConnControl());
            createHttpProcessor.addInterceptor(new RequestUserAgent());
        } else {
            createHttpProcessor = super.createHttpProcessor();
        }
        createHttpProcessor.addRequestInterceptor(new TimeMarker());
        createHttpProcessor.addRequestInterceptor(new CurlLogger());
        return createHttpProcessor;
    }

    public HttpRequestRetryHandler createHttpRequestRetryHandler() {
        return new KscHttpRequestRetryHandler(3, this.mRequestSentRetryEnabled, 10000);
    }

    public HttpRoutePlanner createHttpRoutePlanner() {
        return new KscHttpRoutePlanner(getConnectionManager().getSchemeRegistry());
    }

    public RedirectHandler createRedirectHandler() {
        return new KscRedirectHandler();
    }

    public ConnectionReuseStrategy createConnectionReuseStrategy() {
        if (this.mKeepAlive) {
            return new DefaultConnectionReuseStrategy();
        }
        return new NoConnectionReuseStrategy();
    }

    public void finalize() throws Throwable {
        super/*java.lang.Object*/.finalize();
    }

    /* loaded from: classes.dex */
    public class CurlLogger implements HttpRequestInterceptor {
        public CurlLogger() {
        }

        public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
            LoggingConfiguration unused = KscHttpClient.this.curlConfiguration;
            if (httpRequest instanceof HttpUriRequest) {
                Log.i("CurlLogger", KscHttpClient.toCurl((HttpUriRequest) httpRequest, false));
            }
        }
    }

    /* loaded from: classes.dex */
    public class TimeMarker implements HttpRequestInterceptor {
        public TimeMarker() {
        }

        public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
            httpContext.setAttribute("ksc.connect_start", Long.valueOf(SystemClock.elapsedRealtime()));
            List list = (List) httpContext.getAttribute("ksc.message_list");
            if (list == null) {
                list = new LinkedList();
                httpContext.setAttribute("ksc.message_list", list);
            }
            list.add(httpRequest);
        }
    }

    public static String toCurl(HttpUriRequest httpUriRequest, boolean z) throws IOException {
        Header[] allHeaders;
        HttpEntity entity;
        StringBuilder sb = new StringBuilder();
        sb.append("curl ");
        for (Header header : httpUriRequest.getAllHeaders()) {
            if (z || (!header.getName().equals("Authorization") && !header.getName().equals("Cookie"))) {
                sb.append("--header \"");
                sb.append(header.toString().trim());
                sb.append("\" ");
            }
        }
        URI uri = httpUriRequest.getURI();
        if (httpUriRequest instanceof RequestWrapper) {
            HttpUriRequest original = ((RequestWrapper) httpUriRequest).getOriginal();
            if (original instanceof HttpUriRequest) {
                uri = original.getURI();
            }
        }
        sb.append("\"");
        sb.append(uri);
        sb.append("\"");
        if ((httpUriRequest instanceof HttpEntityEnclosingRequest) && (entity = ((HttpEntityEnclosingRequest) httpUriRequest).getEntity()) != null && entity.isRepeatable()) {
            if (entity.getContentLength() < FileSize.KB_COEFFICIENT) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                entity.writeTo(byteArrayOutputStream);
                String byteArrayOutputStream2 = byteArrayOutputStream.toString();
                sb.append(" --data-ascii \"");
                sb.append(byteArrayOutputStream2);
                sb.append("\"");
            } else {
                sb.append(" [TOO MUCH DATA TO INCLUDE]");
            }
        }
        return sb.toString();
    }
}
