package cn.kuaipan.android.http;

import android.util.Log;
import cn.kuaipan.android.utils.HttpUtils;
import com.xiaomi.stat.d;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;

/* loaded from: classes.dex */
public class KscHttpResponse {
    public final NetCacheManager mCache;
    public Throwable mError;
    public List<HttpMessage> mMessages;
    public HttpUriRequest mOrigRequest;
    public HttpResponse mResponse;

    public KscHttpResponse(NetCacheManager netCacheManager) {
        this.mCache = netCacheManager;
    }

    public void setOrigRequest(HttpUriRequest httpUriRequest) {
        this.mOrigRequest = httpUriRequest;
    }

    public void setMessage(List<HttpMessage> list) {
        this.mMessages = list;
    }

    public void handleResponse(KscHttpRequest kscHttpRequest, HttpResponse httpResponse, boolean z) {
        this.mOrigRequest = kscHttpRequest.getRequest();
        this.mResponse = httpResponse;
        IKscDecoder decoder = kscHttpRequest.getDecoder();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            if (z) {
                httpResponse.setEntity(KscHttpEntity.getRepeatableEntity(entity, decoder, this.mCache));
            } else if (decoder == null) {
            } else {
                httpResponse.setEntity(new KscHttpEntity(entity, decoder));
            }
        }
    }

    public void setError(Throwable th) {
        this.mError = th;
    }

    public Throwable getError() {
        return this.mError;
    }

    public HttpResponse getResponse() {
        return this.mResponse;
    }

    public InputStream getContent() throws IllegalStateException, IOException {
        HttpResponse httpResponse = this.mResponse;
        String str = null;
        HttpEntity entity = httpResponse == null ? null : httpResponse.getEntity();
        if (entity == null) {
            return null;
        }
        InputStream content = entity.getContent();
        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null) {
            str = contentEncoding.getValue();
        }
        return (str == null || !str.contains(d.aj)) ? content : new GZIPInputStream(content);
    }

    public int getStatusCode() {
        StatusLine statusLine;
        HttpResponse httpResponse = this.mResponse;
        if (httpResponse == null || (statusLine = httpResponse.getStatusLine()) == null) {
            return -1;
        }
        return statusLine.getStatusCode();
    }

    public void release() throws IOException {
        HttpResponse httpResponse = this.mResponse;
        if (httpResponse == null || httpResponse.getEntity() == null) {
            return;
        }
        try {
            try {
                this.mResponse.getEntity().consumeContent();
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                Log.w("KscHttpResponse", "Meet exception when release a KscHttpResponse.", e2);
            }
        } finally {
            this.mResponse = null;
        }
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();
        List<HttpMessage> list = this.mMessages;
        int i = 0;
        if (list != null) {
            Iterator<HttpMessage> it = list.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                HttpResponse httpResponse = (HttpMessage) it.next();
                if (httpResponse instanceof HttpRequest) {
                    sb.append("[Request " + i2 + "]\n");
                    sb.append(HttpUtils.toString((HttpRequest) httpResponse));
                    i2++;
                } else if (httpResponse instanceof HttpResponse) {
                    sb.append("[Response " + i + "]\n");
                    sb.append(HttpUtils.toString(httpResponse));
                    i++;
                }
            }
        }
        if (sb.length() <= 0) {
            sb.append("[Origin Request]\n");
            sb.append(HttpUtils.toString((HttpRequest) this.mOrigRequest));
        }
        sb.append("\n[Response " + i + "]\n");
        sb.append(HttpUtils.toString(this.mResponse));
        if (this.mError != null) {
            sb.append("\n[Error]\n");
            sb.append(Log.getStackTraceString(this.mError));
        }
        return sb.toString().replaceAll("password=.*&", "password=[secData]&");
    }
}
