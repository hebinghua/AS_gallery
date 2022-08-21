package miuix.internal.hybrid.provider;

import miuix.hybrid.HybridView;
import miuix.hybrid.HybridViewClient;

/* loaded from: classes3.dex */
public abstract class AbsWebViewClient {
    public HybridView mHybridView;
    public HybridViewClient mHybridViewClient;

    public abstract Object getWebViewClient();

    public AbsWebViewClient(HybridViewClient hybridViewClient, HybridView hybridView) {
        this.mHybridViewClient = hybridViewClient;
        this.mHybridView = hybridView;
    }
}
