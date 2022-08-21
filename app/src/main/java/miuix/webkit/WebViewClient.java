package miuix.webkit;

import android.graphics.Bitmap;
import android.webkit.WebView;
import miuix.internal.webkit.WebViewClientDelegate;
import miuix.internal.webkit.WebViewWrapper;

/* loaded from: classes3.dex */
public class WebViewClient extends android.webkit.WebViewClient {
    public WebViewClientDelegate mDelegate = new WebViewClientDelegate();

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        return this.mDelegate.shouldOverrideUrlLoading(new WebViewWrapper(webView), str);
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        this.mDelegate.onPageStarted(new WebViewWrapper(webView), str, bitmap);
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String str) {
        this.mDelegate.onPageFinished(new WebViewWrapper(webView), str);
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedLoginRequest(WebView webView, String str, String str2, String str3) {
        this.mDelegate.onReceivedLoginRequest(new WebViewWrapper(webView), str, str2, str3);
    }
}
