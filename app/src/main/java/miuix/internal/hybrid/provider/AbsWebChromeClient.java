package miuix.internal.hybrid.provider;

import miuix.hybrid.HybridChromeClient;
import miuix.hybrid.HybridView;

/* loaded from: classes3.dex */
public abstract class AbsWebChromeClient {
    public HybridChromeClient mHybridChromeClient;
    public HybridView mHybridView;

    public abstract Object getWebChromeClient();

    public AbsWebChromeClient(HybridChromeClient hybridChromeClient, HybridView hybridView) {
        this.mHybridChromeClient = hybridChromeClient;
        this.mHybridView = hybridView;
    }
}
