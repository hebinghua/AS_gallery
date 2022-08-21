package com.miui.gallery.hybrid.pulltorefresh;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.WebView;
import com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase;

/* loaded from: classes2.dex */
public class PullToRefreshWebView extends PullToRefreshBase<WebView> {
    public PullToRefreshWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase
    public final PullToRefreshBase.Orientation getPullToRefreshScrollDirection() {
        return PullToRefreshBase.Orientation.VERTICAL;
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase
    public WebView createRefreshableView(Context context, AttributeSet attributeSet) {
        WebView webView = new WebView(context, attributeSet);
        webView.setBackgroundColor(0);
        return webView;
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase
    public boolean isReadyForPullStart() {
        return ((WebView) this.mRefreshableView).getScrollY() == 0 && !isRefreshing();
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase
    public boolean isReadyForPullEnd() {
        return ((float) ((WebView) this.mRefreshableView).getScrollY()) >= ((float) Math.floor((double) (((float) ((WebView) this.mRefreshableView).getContentHeight()) * ((WebView) this.mRefreshableView).getScale()))) - ((float) ((WebView) this.mRefreshableView).getHeight());
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase
    public void onPtrRestoreInstanceState(Bundle bundle) {
        super.onPtrRestoreInstanceState(bundle);
        ((WebView) this.mRefreshableView).restoreState(bundle);
    }

    @Override // com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase
    public void onPtrSaveInstanceState(Bundle bundle) {
        super.onPtrSaveInstanceState(bundle);
        ((WebView) this.mRefreshableView).saveState(bundle);
    }
}
