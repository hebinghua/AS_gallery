package miuix.internal.webkit;

import miuix.internal.hybrid.webkit.WebView;

/* loaded from: classes3.dex */
public class WebViewWrapper extends WebView {
    public WebViewWrapper(android.webkit.WebView webView) {
        super(webView.getContext(), null);
        this.mWebView = webView;
    }
}
