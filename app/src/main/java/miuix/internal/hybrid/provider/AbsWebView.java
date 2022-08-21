package miuix.internal.hybrid.provider;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebBackForwardList;
import miuix.hybrid.HybridBackForwardList;
import miuix.hybrid.HybridSettings;
import miuix.hybrid.HybridView;

/* loaded from: classes3.dex */
public abstract class AbsWebView {
    public Context mContext;
    public HybridView mHybridView;

    public abstract void addJavascriptInterface(Object obj, String str);

    public abstract boolean canGoBack();

    public abstract boolean canGoForward();

    public abstract void clearCache(boolean z);

    public abstract HybridBackForwardList copyBackForwardList();

    public abstract void destroy();

    public abstract void draw(Canvas canvas);

    public abstract View getBaseWebView();

    public abstract int getContentHeight();

    public abstract Context getContext();

    public abstract View getRootView();

    public abstract float getScale();

    public abstract HybridSettings getSettings();

    public abstract String getTitle();

    public abstract String getUrl();

    public abstract void goBack();

    public abstract void loadUrl(String str);

    public abstract void reload();

    public abstract WebBackForwardList restoreState(Bundle bundle);

    public abstract WebBackForwardList saveState(Bundle bundle);

    public abstract void setVisibility(int i);

    public abstract void setWebChromeClient(AbsWebChromeClient absWebChromeClient);

    public abstract void setWebViewClient(AbsWebViewClient absWebViewClient);

    public AbsWebView(Context context, HybridView hybridView) {
        this.mContext = context;
        this.mHybridView = hybridView;
    }
}
