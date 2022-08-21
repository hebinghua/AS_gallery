package com.miui.gallery.hybrid.hybridclient;

import android.content.Intent;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.miui.gallery.hybrid.GalleryHybridFragment;
import com.miui.gallery.hybrid.hybridclient.wrapper.WebChromeClientWrapper;
import com.miui.gallery.hybrid.hybridclient.wrapper.WebViewClientWrapper;
import com.miui.gallery.permission.core.Permission;
import java.util.List;
import miuix.webkit.WebViewClient;

/* loaded from: classes2.dex */
public interface HybridClient {

    /* loaded from: classes2.dex */
    public interface ActualPathCallback {
        void onGetActualPath(String str);
    }

    void bindWebViewFragment(GalleryHybridFragment galleryHybridFragment);

    void getActualPath(ActualPathCallback actualPathCallback);

    DownloadListener getDownloadListener(DownloadListener downloadListener);

    List<JsInterfacePair> getJavascriptInterfaces();

    Permission[] getRuntimePermissions();

    WebChromeClientWrapper getWebChromeClient(WebChromeClient webChromeClient);

    WebViewClientWrapper getWebViewClient(WebViewClient webViewClient);

    boolean isSupportPullToRefresh();

    void onActivityResult(int i, int i2, Intent intent);

    void onConfigWebSettings(WebSettings webSettings);

    void onPostConfigWebView(WebView webView);

    void onStartConfigWebView(WebView webView);

    /* loaded from: classes2.dex */
    public static class JsInterfacePair {
        public String name;
        public Object obj;

        public JsInterfacePair(String str, Object obj) {
            this.name = str;
            this.obj = obj;
        }
    }
}
