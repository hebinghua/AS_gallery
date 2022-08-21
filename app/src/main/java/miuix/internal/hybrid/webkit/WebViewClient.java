package miuix.internal.hybrid.webkit;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.WebResourceResponse;
import miuix.hybrid.HybridResourceResponse;
import miuix.hybrid.HybridView;
import miuix.hybrid.HybridViewClient;
import miuix.internal.hybrid.provider.AbsWebViewClient;

/* loaded from: classes3.dex */
public class WebViewClient extends AbsWebViewClient {
    public WebViewClient(HybridViewClient hybridViewClient, HybridView hybridView) {
        super(hybridViewClient, hybridView);
    }

    @Override // miuix.internal.hybrid.provider.AbsWebViewClient
    public Object getWebViewClient() {
        return new InternalWebViewClient();
    }

    public void onPageStarted(HybridView hybridView, String str, Bitmap bitmap) {
        this.mHybridViewClient.onPageStarted(hybridView, str, bitmap);
    }

    public void onPageFinished(HybridView hybridView, String str) {
        this.mHybridViewClient.onPageFinished(hybridView, str);
    }

    public HybridResourceResponse shouldInterceptRequest(HybridView hybridView, String str) {
        return this.mHybridViewClient.shouldInterceptRequest(hybridView, str);
    }

    public boolean shouldOverrideUrlLoading(HybridView hybridView, String str) {
        return this.mHybridViewClient.shouldOverrideUrlLoading(hybridView, str);
    }

    public void onReceivedSslError(HybridView hybridView, miuix.hybrid.SslErrorHandler sslErrorHandler, SslError sslError) {
        this.mHybridViewClient.onReceivedSslError(hybridView, sslErrorHandler, sslError);
    }

    public void onReceivedError(HybridView hybridView, int i, String str, String str2) {
        this.mHybridViewClient.onReceivedError(hybridView, i, str, str2);
    }

    public void onReceivedLoginRequest(HybridView hybridView, String str, String str2, String str3) {
        this.mHybridViewClient.onReceivedLoginRequest(hybridView, str, str2, str3);
    }

    /* loaded from: classes3.dex */
    public class InternalWebViewClient extends android.webkit.WebViewClient {
        public InternalWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(android.webkit.WebView webView, String str, Bitmap bitmap) {
            WebViewClient webViewClient = WebViewClient.this;
            webViewClient.onPageStarted(webViewClient.mHybridView, str, bitmap);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(android.webkit.WebView webView, String str) {
            WebViewClient webViewClient = WebViewClient.this;
            webViewClient.onPageFinished(webViewClient.mHybridView, str);
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(android.webkit.WebView webView, String str) {
            WebViewClient webViewClient = WebViewClient.this;
            HybridResourceResponse shouldInterceptRequest = webViewClient.shouldInterceptRequest(webViewClient.mHybridView, str);
            if (shouldInterceptRequest == null) {
                return null;
            }
            return new WebResourceResponce(shouldInterceptRequest);
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(android.webkit.WebView webView, String str) {
            WebViewClient webViewClient = WebViewClient.this;
            return webViewClient.shouldOverrideUrlLoading(webViewClient.mHybridView, str);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedSslError(android.webkit.WebView webView, android.webkit.SslErrorHandler sslErrorHandler, SslError sslError) {
            SslErrorHandler sslErrorHandler2 = new SslErrorHandler(sslErrorHandler);
            WebViewClient webViewClient = WebViewClient.this;
            webViewClient.onReceivedSslError(webViewClient.mHybridView, sslErrorHandler2, sslError);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(android.webkit.WebView webView, int i, String str, String str2) {
            WebViewClient webViewClient = WebViewClient.this;
            webViewClient.onReceivedError(webViewClient.mHybridView, i, str, str2);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedLoginRequest(android.webkit.WebView webView, String str, String str2, String str3) {
            WebViewClient webViewClient = WebViewClient.this;
            webViewClient.onReceivedLoginRequest(webViewClient.mHybridView, str, str2, str3);
        }
    }
}
