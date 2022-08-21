package miuix.internal.hybrid.webkit;

import android.webkit.WebResourceResponse;
import miuix.hybrid.HybridResourceResponse;

/* loaded from: classes3.dex */
public class WebResourceResponce extends WebResourceResponse {
    public WebResourceResponce(HybridResourceResponse hybridResourceResponse) {
        super(hybridResourceResponse.getMimeType(), hybridResourceResponse.getEncoding(), hybridResourceResponse.getData());
    }
}
