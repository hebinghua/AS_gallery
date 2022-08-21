package com.android.volley;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes.dex */
public class NetworkDispatcher extends Thread {
    public final Cache mCache;
    public final ResponseDelivery mDelivery;
    public final Network mNetwork;
    public final BlockingQueue<Request<?>> mQueue;
    public volatile boolean mQuit = false;

    public NetworkDispatcher(BlockingQueue<Request<?>> blockingQueue, Network network, Cache cache, ResponseDelivery responseDelivery) {
        this.mQueue = blockingQueue;
        this.mNetwork = network;
        this.mCache = cache;
        this.mDelivery = responseDelivery;
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    @TargetApi(14)
    public final void addTrafficStatsTag(Request<?> request) {
        if (Build.VERSION.SDK_INT >= 14) {
            TrafficStats.setThreadStatsTag(request.getTrafficStatsTag());
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Process.setThreadPriority(10);
        while (true) {
            try {
                processRequest();
            } catch (InterruptedException unused) {
                if (this.mQuit) {
                    Thread.currentThread().interrupt();
                    return;
                }
                VolleyLog.e("Ignoring spurious interrupt of NetworkDispatcher thread; use quit() to terminate it", new Object[0]);
            }
        }
    }

    private void processRequest() throws InterruptedException {
        processRequest(this.mQueue.take());
    }

    public void processRequest(Request<?> request) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            request.addMarker("network-queue-take");
            if (request.isCanceled()) {
                request.finish("network-discard-cancelled");
                request.notifyListenerResponseNotUsable();
                return;
            }
            addTrafficStatsTag(request);
            NetworkResponse performRequest = this.mNetwork.performRequest(request);
            request.addMarker("network-http-complete");
            if (performRequest.notModified && request.hasHadResponseDelivered()) {
                request.finish("not-modified");
                request.notifyListenerResponseNotUsable();
                return;
            }
            Response<?> parseNetworkResponse = request.parseNetworkResponse(performRequest);
            request.addMarker("network-parse-complete");
            if (request.shouldCache() && parseNetworkResponse.cacheEntry != null) {
                this.mCache.put(request.getCacheKey(), parseNetworkResponse.cacheEntry);
                request.addMarker("network-cache-written");
            }
            request.markDelivered();
            this.mDelivery.postResponse(request, parseNetworkResponse);
            request.notifyListenerResponseReceived(parseNetworkResponse);
        } catch (VolleyError e) {
            e.setNetworkTimeMs(SystemClock.elapsedRealtime() - elapsedRealtime);
            parseAndDeliverNetworkError(request, e);
            request.notifyListenerResponseNotUsable();
        } catch (Exception e2) {
            VolleyLog.e(e2, "Unhandled exception %s", e2.toString());
            VolleyError volleyError = new VolleyError(e2);
            volleyError.setNetworkTimeMs(SystemClock.elapsedRealtime() - elapsedRealtime);
            this.mDelivery.postError(request, volleyError);
            request.notifyListenerResponseNotUsable();
        }
    }

    public final void parseAndDeliverNetworkError(Request<?> request, VolleyError volleyError) {
        this.mDelivery.postError(request, request.parseNetworkError(volleyError));
    }
}
