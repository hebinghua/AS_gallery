package miuix.internal.hybrid.webkit;

import android.webkit.GeolocationPermissions;
import miuix.hybrid.GeolocationPermissions;
import miuix.hybrid.HybridChromeClient;
import miuix.hybrid.HybridView;
import miuix.internal.hybrid.provider.AbsWebChromeClient;

/* loaded from: classes3.dex */
public class WebChromeClient extends AbsWebChromeClient {
    public WebChromeClient(HybridChromeClient hybridChromeClient, HybridView hybridView) {
        super(hybridChromeClient, hybridView);
    }

    @Override // miuix.internal.hybrid.provider.AbsWebChromeClient
    public Object getWebChromeClient() {
        return new InternalWebChromeClient();
    }

    public boolean onJsAlert(HybridView hybridView, String str, String str2, miuix.hybrid.JsResult jsResult) {
        return this.mHybridChromeClient.onJsAlert(hybridView, str, str2, jsResult);
    }

    public boolean onJsConfirm(HybridView hybridView, String str, String str2, miuix.hybrid.JsResult jsResult) {
        return this.mHybridChromeClient.onJsConfirm(hybridView, str, str2, jsResult);
    }

    public void onProgressChanged(HybridView hybridView, int i) {
        this.mHybridChromeClient.onProgressChanged(hybridView, i);
    }

    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        this.mHybridChromeClient.onGeolocationPermissionsShowPrompt(str, callback);
    }

    public void onReceivedTitle(HybridView hybridView, String str) {
        this.mHybridChromeClient.onReceivedTitle(hybridView, str);
    }

    /* loaded from: classes3.dex */
    public class InternalWebChromeClient extends android.webkit.WebChromeClient {
        public InternalWebChromeClient() {
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsAlert(android.webkit.WebView webView, String str, String str2, android.webkit.JsResult jsResult) {
            JsResult jsResult2 = new JsResult(jsResult);
            WebChromeClient webChromeClient = WebChromeClient.this;
            return webChromeClient.onJsAlert(webChromeClient.mHybridView, str, str2, jsResult2);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsConfirm(android.webkit.WebView webView, String str, String str2, android.webkit.JsResult jsResult) {
            JsResult jsResult2 = new JsResult(jsResult);
            WebChromeClient webChromeClient = WebChromeClient.this;
            return webChromeClient.onJsConfirm(webChromeClient.mHybridView, str, str2, jsResult2);
        }

        @Override // android.webkit.WebChromeClient
        public void onProgressChanged(android.webkit.WebView webView, int i) {
            WebChromeClient webChromeClient = WebChromeClient.this;
            webChromeClient.onProgressChanged(webChromeClient.mHybridView, i);
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            WebChromeClient.this.onGeolocationPermissionsShowPrompt(str, new GeolocationPermissionsCallback(callback));
        }

        @Override // android.webkit.WebChromeClient
        public void onReceivedTitle(android.webkit.WebView webView, String str) {
            WebChromeClient webChromeClient = WebChromeClient.this;
            webChromeClient.onReceivedTitle(webChromeClient.mHybridView, str);
        }
    }
}
