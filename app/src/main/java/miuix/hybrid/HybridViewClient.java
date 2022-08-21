package miuix.hybrid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import java.io.IOException;
import java.util.UUID;
import miuix.internal.hybrid.HybridManager;
import miuix.internal.webkit.WebViewClientDelegate;

/* loaded from: classes3.dex */
public class HybridViewClient {
    private static final String ASSET_PATH = "hybrid/";
    private static final String VIRTUAL_PATH = "android_asset/hybrid/";
    private HybridManager mManager;
    private WebViewClientDelegate mWebViewClientDelegate = new WebViewClientDelegate();

    public void setHybridManager(HybridManager hybridManager) {
        this.mManager = hybridManager;
    }

    public void onPageStarted(HybridView hybridView, String str, Bitmap bitmap) {
        PageContext pageContext = new PageContext();
        pageContext.setId(UUID.randomUUID().toString());
        pageContext.setUrl(str);
        this.mManager.setPageContext(pageContext);
        this.mManager.onPageChange();
        hybridView.setWebProvider(str);
        hybridView.setLoadingError(false);
        this.mWebViewClientDelegate.onPageStarted(hybridView.getWebView(), str, bitmap);
    }

    public void onPageFinished(HybridView hybridView, String str) {
        if (this.mManager.getActivity().getActionBar() != null) {
            this.mManager.getActivity().getActionBar().setTitle(hybridView.getTitle());
        }
        this.mWebViewClientDelegate.onPageFinished(hybridView.getWebView(), str);
    }

    public HybridResourceResponse shouldInterceptRequest(HybridView hybridView, String str) {
        int indexOf;
        int i;
        if (str == null || !str.startsWith("http") || (indexOf = str.indexOf(VIRTUAL_PATH)) < 0 || (i = indexOf + 21) >= str.length()) {
            return null;
        }
        String substring = str.substring(i);
        try {
            AssetManager assets = this.mManager.getActivity().getAssets();
            return new HybridResourceResponse(null, null, assets.open(ASSET_PATH + substring));
        } catch (IOException unused) {
            return null;
        }
    }

    public boolean shouldOverrideUrlLoading(HybridView hybridView, String str) {
        return this.mWebViewClientDelegate.shouldOverrideUrlLoading(hybridView.getWebView(), str);
    }

    public void onReceivedSslError(HybridView hybridView, SslErrorHandler sslErrorHandler, SslError sslError) {
        sslErrorHandler.cancel();
    }

    public void onReceivedError(HybridView hybridView, int i, String str, String str2) {
        hybridView.setLoadingError(true);
        hybridView.showReloadView();
    }

    public void onReceivedLoginRequest(HybridView hybridView, String str, String str2, String str3) {
        this.mWebViewClientDelegate.onReceivedLoginRequest(hybridView.getWebView(), str, str2, str3);
    }
}
