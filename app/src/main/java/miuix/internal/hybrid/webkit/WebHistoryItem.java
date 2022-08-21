package miuix.internal.hybrid.webkit;

import android.graphics.Bitmap;
import miuix.hybrid.HybridHistoryItem;

/* loaded from: classes3.dex */
public class WebHistoryItem extends HybridHistoryItem {
    public android.webkit.WebHistoryItem mWebHistoryItem;

    public WebHistoryItem(android.webkit.WebHistoryItem webHistoryItem) {
        this.mWebHistoryItem = webHistoryItem;
    }

    @Override // miuix.hybrid.HybridHistoryItem
    public String getUrl() {
        return this.mWebHistoryItem.getUrl();
    }

    @Override // miuix.hybrid.HybridHistoryItem
    public String getOriginalUrl() {
        return this.mWebHistoryItem.getOriginalUrl();
    }

    @Override // miuix.hybrid.HybridHistoryItem
    public String getTitle() {
        return this.mWebHistoryItem.getTitle();
    }

    @Override // miuix.hybrid.HybridHistoryItem
    public Bitmap getFavicon() {
        return this.mWebHistoryItem.getFavicon();
    }
}
