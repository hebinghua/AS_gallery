package miuix.internal.hybrid.webkit;

import android.os.Build;
import miuix.hybrid.HybridSettings;

/* loaded from: classes3.dex */
public class WebSettings extends HybridSettings {
    public android.webkit.WebSettings mWebSettings;

    public WebSettings(android.webkit.WebSettings webSettings) {
        this.mWebSettings = webSettings;
    }

    @Override // miuix.hybrid.HybridSettings
    public void setJavaScriptEnabled(boolean z) {
        this.mWebSettings.setJavaScriptEnabled(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setUserAgentString(String str) {
        this.mWebSettings.setUserAgentString(str);
    }

    @Override // miuix.hybrid.HybridSettings
    public String getUserAgentString() {
        return this.mWebSettings.getUserAgentString();
    }

    @Override // miuix.hybrid.HybridSettings
    public void setUseWideViewPort(boolean z) {
        this.mWebSettings.setUseWideViewPort(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setSupportMultipleWindows(boolean z) {
        this.mWebSettings.setSupportMultipleWindows(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setLoadWithOverviewMode(boolean z) {
        this.mWebSettings.setLoadWithOverviewMode(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setDomStorageEnabled(boolean z) {
        this.mWebSettings.setDomStorageEnabled(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setDatabaseEnabled(boolean z) {
        this.mWebSettings.setDatabaseEnabled(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setAllowFileAccessFromFileURLs(boolean z) {
        this.mWebSettings.setAllowFileAccessFromFileURLs(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setAllowUniversalAccessFromFileURLs(boolean z) {
        this.mWebSettings.setAllowUniversalAccessFromFileURLs(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setCacheMode(int i) {
        this.mWebSettings.setCacheMode(i);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setJavaScriptCanOpenWindowsAutomatically(boolean z) {
        this.mWebSettings.setJavaScriptCanOpenWindowsAutomatically(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setTextZoom(int i) {
        this.mWebSettings.setTextZoom(i);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setGeolocationEnabled(boolean z) {
        this.mWebSettings.setGeolocationEnabled(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setAppCacheEnabled(boolean z) {
        this.mWebSettings.setAppCacheEnabled(z);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setAppCachePath(String str) {
        this.mWebSettings.setAppCachePath(str);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setGeolocationDatabasePath(String str) {
        this.mWebSettings.setGeolocationDatabasePath(str);
    }

    @Override // miuix.hybrid.HybridSettings
    public void setForceDark(int i) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.mWebSettings.setForceDark(i);
        }
    }
}
