package cn.kuaipan.android.http;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import cn.kuaipan.android.KssConfig;
import cn.kuaipan.android.exception.ErrorHelper;
import cn.kuaipan.android.http.client.KscHttpClient;
import cn.kuaipan.android.http.client.URIRedirector;
import cn.kuaipan.android.utils.ContextUtils;
import cn.kuaipan.android.utils.HttpUtils;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;

/* loaded from: classes.dex */
public class KscHttpTransmitter {
    public final NetCacheManager mCacheManager;
    public final SparseArray<Pair<Long, ? extends HttpClient>> mClients;
    public final Context mContext;
    public final KscSpeedManager mDownloadSpeedManager;
    public URIRedirector mRedirector;
    public final KscSpeedManager mUploadSpeedManager;
    public final SparseArray<String> mUserAgents;

    public KscHttpTransmitter(Context context) {
        ContextUtils.init(context);
        this.mContext = context;
        this.mUploadSpeedManager = new KscSpeedManager(1800);
        this.mDownloadSpeedManager = new KscSpeedManager(1800);
        this.mCacheManager = NetCacheManager.getInstance(context, true);
        this.mClients = new SparseArray<>(4);
        this.mUserAgents = new SparseArray<>(4);
    }

    public KscHttpResponse execute(KscHttpRequest kscHttpRequest, int i, KssTransferStopper kssTransferStopper) throws InterruptedException {
        URIRedirector uRIRedirector;
        KssTransferStopper kssTransferStopper2 = kssTransferStopper == null ? KssConfig.DEFAULT_TRANSFER_STOPPER : kssTransferStopper;
        IKscTransferListener listener = kscHttpRequest.getListener();
        KscHttpResponse kscHttpResponse = new KscHttpResponse(this.mCacheManager);
        BasicHttpContext basicHttpContext = new BasicHttpContext();
        try {
            HttpUriRequest request = kscHttpRequest.getRequest();
            kscHttpResponse.setOrigRequest(request);
            HttpClient client = getClient(i);
            if ((i & 1) != 0 && (uRIRedirector = this.mRedirector) != null) {
                basicHttpContext.setAttribute("ksc.connect_redirector", uRIRedirector);
            }
            String requestHost = getRequestHost(kscHttpRequest.getRequest());
            KscSpeedMonitor moniter = this.mUploadSpeedManager.getMoniter(requestHost);
            KscSpeedMonitor moniter2 = this.mDownloadSpeedManager.getMoniter(requestHost);
            setMonitor(request, moniter, listener, kssTransferStopper2);
            long current = KscSpeedManager.current();
            HttpResponse execute = client.execute(request, basicHttpContext);
            long current2 = KscSpeedManager.current();
            Object attribute = basicHttpContext.getAttribute("ksc.message_list");
            boolean z = false;
            if (attribute instanceof List) {
                try {
                    List<HttpMessage> list = (List) attribute;
                    if (list != null && !list.isEmpty()) {
                        kscHttpResponse.setMessage(list);
                    }
                    moniter.recode(current, current2, HttpUtils.getRequestSize(getRequest(list)));
                    moniter2.recode(current, current2, HttpUtils.getResponseSize(getResponse(list)) + HttpUtils.getResponseSize(execute, false));
                } catch (Exception e) {
                    Log.w("KscHttpTransmitter", "Failed get requestList from context.", e);
                }
            }
            setMonitor(execute, moniter2, listener, kssTransferStopper2);
            if ((i & 4) == 0) {
                z = true;
            }
            kscHttpResponse.handleResponse(kscHttpRequest, execute, z);
        } catch (Throwable th) {
            ErrorHelper.handleInterruptException(th);
            Log.w("KscHttpTransmitter", "Meet exception when execute a KscHttpRequest.", th);
            kscHttpResponse.setError(th);
        }
        return kscHttpResponse;
    }

    public static void setMonitor(HttpMessage httpMessage, KscSpeedMonitor kscSpeedMonitor, IKscTransferListener iKscTransferListener, KssTransferStopper kssTransferStopper) {
        HttpResponse httpResponse;
        HttpEntity entity;
        if (httpMessage instanceof HttpEntityEnclosingRequestBase) {
            HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = (HttpEntityEnclosingRequestBase) httpMessage;
            HttpEntity entity2 = httpEntityEnclosingRequestBase.getEntity();
            if (entity2 == null) {
                return;
            }
            httpEntityEnclosingRequestBase.setEntity(new ProcessMonitorEntity(entity2, kscSpeedMonitor, iKscTransferListener, kssTransferStopper, true));
        } else if (!(httpMessage instanceof HttpResponse) || (entity = (httpResponse = (HttpResponse) httpMessage).getEntity()) == null) {
        } else {
            httpResponse.setEntity(new ProcessMonitorEntity(entity, kscSpeedMonitor, iKscTransferListener, kssTransferStopper, false));
        }
    }

    public static HttpResponse[] getResponse(List<HttpMessage> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<HttpMessage> it = list.iterator();
        while (it.hasNext()) {
            HttpResponse httpResponse = (HttpMessage) it.next();
            if (httpResponse instanceof HttpResponse) {
                arrayList.add(httpResponse);
            }
        }
        return (HttpResponse[]) arrayList.toArray(new HttpResponse[arrayList.size()]);
    }

    public final HttpRequest[] getRequest(List<HttpMessage> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<HttpMessage> it = list.iterator();
        while (it.hasNext()) {
            HttpRequest httpRequest = (HttpMessage) it.next();
            if (httpRequest instanceof HttpRequest) {
                arrayList.add(httpRequest);
            }
        }
        return (HttpRequest[]) arrayList.toArray(new HttpRequest[arrayList.size()]);
    }

    public void setUserAgent(int i, String str) {
        Object obj;
        this.mUserAgents.put(i, str);
        Pair<Long, ? extends HttpClient> pair = this.mClients.get(i);
        if (pair == null || (obj = pair.second) == null) {
            return;
        }
        HttpProtocolParams.setUserAgent(((HttpClient) obj).getParams(), str);
    }

    public final HttpClient getClient(int i) {
        KscHttpClient newInstance;
        int i2 = i & 7;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Pair<Long, ? extends HttpClient> pair = this.mClients.get(i2);
        if (pair == null || elapsedRealtime - ((Long) pair.first).longValue() > 180000) {
            String str = this.mUserAgents.get(i2);
            boolean z = false;
            if ((i2 & 4) != 0) {
                newInstance = KscHttpClient.newKssInstance(str);
            } else {
                boolean z2 = (i2 & 2) == 0;
                if ((i2 & 1) != 0) {
                    z = true;
                }
                newInstance = KscHttpClient.newInstance(str, z2, z);
            }
            this.mClients.put(i2, Pair.create(Long.valueOf(elapsedRealtime), newInstance));
            return newInstance;
        }
        return (HttpClient) pair.second;
    }

    public static String getRequestHost(HttpUriRequest httpUriRequest) {
        URI uri;
        if (httpUriRequest == null || (uri = httpUriRequest.getURI()) == null) {
            return null;
        }
        return uri.getHost();
    }
}
