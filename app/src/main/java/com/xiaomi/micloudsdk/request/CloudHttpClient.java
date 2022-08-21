package com.xiaomi.micloudsdk.request;

import com.xiaomi.micloudsdk.request.utils.RequestContext;
import java.io.IOException;
import java.util.Locale;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes3.dex */
public class CloudHttpClient implements HttpClient {
    public HttpClient mProxy;

    public CloudHttpClient(HttpClient httpClient) {
        this.mProxy = httpClient;
    }

    public static CloudHttpClient newInstance() {
        return new CloudHttpClient(initClient());
    }

    public static DefaultHttpClient initClient() {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpProtocolParams.setUserAgent(defaultHttpClient.getParams(), RequestContext.getUserAgent());
        return defaultHttpClient;
    }

    public HttpParams getParams() {
        return this.mProxy.getParams();
    }

    public ClientConnectionManager getConnectionManager() {
        return this.mProxy.getConnectionManager();
    }

    public HttpResponse execute(HttpUriRequest httpUriRequest) throws IOException, ClientProtocolException {
        addHeader(httpUriRequest);
        return this.mProxy.execute(httpUriRequest);
    }

    public HttpResponse execute(HttpUriRequest httpUriRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
        addHeader(httpUriRequest);
        return this.mProxy.execute(httpUriRequest, httpContext);
    }

    public HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest) throws IOException, ClientProtocolException {
        addHeader(httpRequest);
        return this.mProxy.execute(httpHost, httpRequest);
    }

    public HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
        addHeader(httpRequest);
        return this.mProxy.execute(httpHost, httpRequest);
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        addHeader(httpUriRequest);
        return (T) this.mProxy.execute(httpUriRequest, responseHandler);
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        addHeader(httpUriRequest);
        return (T) this.mProxy.execute(httpUriRequest, responseHandler, httpContext);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        addHeader(httpRequest);
        return (T) this.mProxy.execute(httpHost, httpRequest, responseHandler);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        addHeader(httpRequest);
        return (T) this.mProxy.execute(httpHost, httpRequest, responseHandler, httpContext);
    }

    public final void addHeader(HttpRequest httpRequest) {
        httpRequest.addHeader("Accept-Language", Locale.getDefault().toString());
    }
}
