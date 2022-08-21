package com.miui.gallery.cloud;

import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.HttpUtils;
import com.miui.gallery.util.StreamUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.xiaomi.stat.d;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/* loaded from: classes.dex */
public class NetworkUtils extends BaseNetworkUtils {
    public static boolean DBG = false;

    /* loaded from: classes.dex */
    public enum RequestType {
        POST,
        GET
    }

    /* renamed from: com.miui.gallery.cloud.NetworkUtils$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$cloud$NetworkUtils$RequestType;

        static {
            int[] iArr = new int[RequestType.values().length];
            $SwitchMap$com$miui$gallery$cloud$NetworkUtils$RequestType = iArr;
            try {
                iArr[RequestType.GET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$NetworkUtils$RequestType[RequestType.POST.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static String getHttpMethod(RequestType requestType) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$cloud$NetworkUtils$RequestType[requestType.ordinal()];
        return i != 1 ? i != 2 ? "" : "POST" : "GET";
    }

    public static HttpURLConnection getHttpConn(String str, RequestType requestType) throws IOException {
        HttpURLConnection httpURLConnection;
        URL url = new URL(HttpUtils.appendAppLifecycleParameter(str));
        if (url.getProtocol().equals("https")) {
            httpURLConnection = (HttpsURLConnection) url.openConnection();
        } else {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }
        httpURLConnection.setRequestMethod(getHttpMethod(requestType));
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(30000);
        return httpURLConnection;
    }

    public static HttpClient getHttpClient() {
        HttpClient httpClient = ApplicationHelper.getMiCloudProvider().getHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        HttpConnectionParams.setSoTimeout(params, 30000);
        ConnManagerParams.setTimeout(params, 30000L);
        return httpClient;
    }

    public static InputStream handleResult(HttpRequestBase httpRequestBase, HttpResponse httpResponse, String str, boolean z) throws IllegalStateException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            return decodeGZip(httpResponse);
        }
        SyncLogger.d("NetworkUtils", "Server error: " + statusCode + " " + httpResponse.getStatusLine());
        throw new IOException();
    }

    public static void closeHttpClient(HttpClient httpClient) {
        if (httpClient != null) {
            try {
                ClientConnectionManager connectionManager = httpClient.getConnectionManager();
                if (connectionManager == null) {
                    return;
                }
                connectionManager.shutdown();
            } catch (Throwable th) {
                SyncLogger.e("NetworkUtils", th);
            }
        }
    }

    public static InputStream httpPostRequest(HttpClient httpClient, String str, HttpEntity httpEntity, Header header) throws ClientProtocolException, IOException, GalleryMiCloudServerException {
        HttpPost httpPost = new HttpPost(str);
        if (httpEntity != null) {
            httpPost.addHeader(httpEntity.getContentType());
            httpPost.setEntity(httpEntity);
        }
        if (header != null) {
            httpPost.addHeader(header);
        }
        if (!httpPost.containsHeader("Accept-Encoding")) {
            httpPost.addHeader("Accept-Encoding", d.aj);
        }
        if (DBG) {
            SyncLogger.d("NetworkUtils", "http post url: %s", str);
            SyncLogger.d("NetworkUtils", "http post cookies: %s", header);
        }
        return handleResult(httpPost, excuteInternal(httpClient, httpPost), str, true);
    }

    public static String httpPostRequestForString(String str, HttpEntity httpEntity, Header header) throws ClientProtocolException, IOException, GalleryMiCloudServerException {
        HttpClient httpClient = getHttpClient();
        InputStream httpPostRequest = httpPostRequest(httpClient, str, httpEntity, header);
        if (httpPostRequest != null) {
            try {
                return StreamUtils.toString(httpPostRequest);
            } finally {
                httpPostRequest.close();
                closeHttpClient(httpClient);
            }
        }
        if (httpPostRequest != null) {
            httpPostRequest.close();
        }
        closeHttpClient(httpClient);
        return null;
    }

    public static InputStream httpGetRequest(HttpClient httpClient, String str, Header header) throws ClientProtocolException, IOException, GalleryMiCloudServerException {
        HttpGet httpGet = new HttpGet(str);
        return handleResult(httpGet, httpGetRequest(httpClient, httpGet, header), str, true);
    }

    public static HttpResponse httpGetRequest(HttpClient httpClient, HttpGet httpGet, Header header) throws ClientProtocolException, IOException {
        httpGet.addHeader(header);
        if (!httpGet.containsHeader("Accept-Encoding")) {
            httpGet.addHeader("Accept-Encoding", d.aj);
        }
        if (DBG) {
            SyncLogger.d("NetworkUtils", "http get url : " + httpGet.getURI());
            SyncLogger.d("NetworkUtils", "http get cookies : " + header);
        }
        return excuteInternal(httpClient, httpGet);
    }

    public static String httpGetRequestForString(String str, Header header) throws ClientProtocolException, IOException, GalleryMiCloudServerException {
        HttpClient httpClient = getHttpClient();
        InputStream httpGetRequest = httpGetRequest(httpClient, str, header);
        if (httpGetRequest != null) {
            try {
                return StreamUtils.toString(httpGetRequest);
            } finally {
                httpGetRequest.close();
                closeHttpClient(httpClient);
            }
        }
        if (httpGetRequest != null) {
            httpGetRequest.close();
        }
        closeHttpClient(httpClient);
        return null;
    }

    public static InputStream decodeGZip(HttpResponse httpResponse) throws IllegalStateException, IOException {
        Header firstHeader = httpResponse.getFirstHeader("Content-Encoding");
        if (firstHeader != null && firstHeader.getValue().equalsIgnoreCase(d.aj)) {
            return new GZIPInputStream(httpResponse.getEntity().getContent());
        }
        return httpResponse.getEntity().getContent();
    }

    public static HttpResponse excuteInternal(HttpClient httpClient, HttpRequestBase httpRequestBase) throws IOException {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            HttpResponse execute = httpClient.execute(httpRequestBase);
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            long j = 0;
            if (execute.getEntity() != null) {
                j = execute.getEntity().getContentLength();
            }
            StatHelper.addHttpEvent(httpRequestBase.getURI().toString(), currentTimeMillis2, j, execute.getStatusLine().getStatusCode());
            return execute;
        } catch (IOException e) {
            StatHelper.addHttpEvent(httpRequestBase.getURI().toString(), e.getClass().getSimpleName());
            throw e;
        }
    }
}
