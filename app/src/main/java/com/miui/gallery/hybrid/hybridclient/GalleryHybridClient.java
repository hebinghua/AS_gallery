package com.miui.gallery.hybrid.hybridclient;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.android.internal.DarkModeCompat;
import com.miui.gallery.hybrid.GalleryHybridFragment;
import com.miui.gallery.hybrid.hybridclient.HybridClient;
import com.miui.gallery.hybrid.hybridclient.wrapper.DownloadListenerWrapper;
import com.miui.gallery.hybrid.hybridclient.wrapper.WebChromeClientWrapper;
import com.miui.gallery.hybrid.hybridclient.wrapper.WebViewClientWrapper;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import miuix.webkit.WebViewClient;

/* loaded from: classes2.dex */
public class GalleryHybridClient implements HybridClient {
    public Context mContext;
    public String mCurrentUrl;
    public String mUrl;
    public WebView mWebView;
    public GalleryHybridFragment mWebViewFragment;

    /* loaded from: classes2.dex */
    public interface ImageCountGotCallback {
        void onGetMaxImageCount(int i);
    }

    /* loaded from: classes2.dex */
    public static class ImageInfo {
        public String data;
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public List<HybridClient.JsInterfacePair> getJavascriptInterfaces() {
        return null;
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public Permission[] getRuntimePermissions() {
        return new Permission[0];
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public boolean isSupportPullToRefresh() {
        return true;
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public GalleryHybridClient(Context context, String str) {
        this.mContext = context;
        this.mUrl = str;
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public void bindWebViewFragment(GalleryHybridFragment galleryHybridFragment) {
        this.mWebViewFragment = galleryHybridFragment;
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public void getActualPath(HybridClient.ActualPathCallback actualPathCallback) {
        if (actualPathCallback != null) {
            actualPathCallback.onGetActualPath(this.mUrl);
        }
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public DownloadListener getDownloadListener(DownloadListener downloadListener) {
        return new GalleryDownloadListenerWrapper(downloadListener);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public WebViewClientWrapper getWebViewClient(WebViewClient webViewClient) {
        return new GalleryWebViewClientWrapper(webViewClient);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public WebChromeClientWrapper getWebChromeClient(WebChromeClient webChromeClient) {
        return new GalleryWebChromeClientWrapper(webChromeClient);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public void onConfigWebSettings(WebSettings webSettings) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(-1);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(100);
        if (MiscUtil.isNightMode(this.mContext)) {
            DarkModeCompat.setForceDark(webSettings, 2);
        } else {
            DarkModeCompat.setForceDark(webSettings, 1);
        }
        setGeoLocation(webSettings);
        setAppCache(webSettings);
        setDOMStorage(webSettings);
        setBrowserUA(webSettings);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public void onStartConfigWebView(WebView webView) {
        this.mWebView = webView;
    }

    @Override // com.miui.gallery.hybrid.hybridclient.HybridClient
    public void onPostConfigWebView(WebView webView) {
        webView.setOverScrollMode(2);
    }

    public final void setBrowserUA(WebSettings webSettings) {
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " MiuiGallery lg/" + Locale.getDefault().toString() + " XiaoMi/MiuiBrowser/4.3");
    }

    public final void setGeoLocation(WebSettings webSettings) {
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath(this.mContext.getDir("geodatabase", 0).getPath());
    }

    public final void setAppCache(WebSettings webSettings) {
        webSettings.setAppCacheEnabled(true);
        String path = this.mContext.getDir("cache", 0).getPath();
        ensureExistence(path);
        webSettings.setAppCachePath(path);
    }

    public final void ensureExistence(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void setDOMStorage(WebSettings webSettings) {
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
    }

    public void callJsMethod(final WebView webView, final String str, final ValueCallback<String> valueCallback) {
        if (webView != null) {
            webView.post(new Runnable() { // from class: com.miui.gallery.hybrid.hybridclient.GalleryHybridClient.1
                @Override // java.lang.Runnable
                public void run() {
                    webView.evaluateJavascript(str, valueCallback);
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public class GalleryDownloadListenerWrapper extends DownloadListenerWrapper {
        public GalleryDownloadListenerWrapper(DownloadListener downloadListener) {
            super(downloadListener);
        }

        @Override // com.miui.gallery.hybrid.hybridclient.wrapper.DownloadListenerWrapper, android.webkit.DownloadListener
        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
            if (getWrapped() == null) {
                GalleryHybridClient.this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                return;
            }
            super.onDownloadStart(str, str2, str3, str4, j);
        }
    }

    /* loaded from: classes2.dex */
    public class GalleryWebViewClientWrapper extends WebViewClientWrapper {
        public GalleryWebViewClientWrapper(WebViewClient webViewClient) {
            super(webViewClient);
        }

        @Override // com.miui.gallery.hybrid.hybridclient.wrapper.WebViewClientWrapper, miuix.webkit.WebViewClient, android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (super.shouldOverrideUrlLoading(webView, str)) {
                return true;
            }
            if (!HybridClientFactory.getSupportedIntentSchemes().contains(Uri.parse(str).getScheme())) {
                return false;
            }
            return tryToOverrideUrlLoading(webView, str);
        }

        @Override // com.miui.gallery.hybrid.hybridclient.wrapper.WebViewClientWrapper, miuix.webkit.WebViewClient, android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            GalleryHybridClient.this.mCurrentUrl = str;
            super.onPageStarted(webView, str, bitmap);
        }

        public final boolean tryToOverrideUrlLoading(WebView webView, String str) {
            try {
                Intent parseUri = Intent.parseUri(str, 1);
                parseUri.addCategory("android.intent.category.BROWSABLE");
                parseUri.setComponent(null);
                Intent selector = parseUri.getSelector();
                if (selector != null) {
                    selector.addCategory("android.intent.category.BROWSABLE");
                    selector.setComponent(null);
                }
                parseUri.putExtra("com.android.browser.application_id", webView.getContext().getPackageName());
                try {
                    webView.getContext().startActivity(parseUri);
                    return true;
                } catch (ActivityNotFoundException e) {
                    DefaultLogger.w("GalleryHybridClient", e);
                    return false;
                }
            } catch (URISyntaxException e2) {
                DefaultLogger.w("GalleryHybridClient", e2);
                return false;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class GalleryWebChromeClientWrapper extends WebChromeClientWrapper {
        public GalleryWebChromeClientWrapper(WebChromeClient webChromeClient) {
            super(webChromeClient);
        }
    }
}
