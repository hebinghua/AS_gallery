package com.miui.gallery.hybrid.hybridclient;

import android.content.Context;
import android.webkit.WebSettings;

/* loaded from: classes2.dex */
public class RecommendHybridClient extends GalleryHybridClient {
    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public boolean isSupportPullToRefresh() {
        return false;
    }

    public RecommendHybridClient(Context context, String str) {
        super(context, str);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public void onConfigWebSettings(WebSettings webSettings) {
        super.onConfigWebSettings(webSettings);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
    }
}
