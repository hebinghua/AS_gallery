package miuix.hybrid;

import java.util.Map;

/* loaded from: classes3.dex */
public interface HybridFeature {

    /* loaded from: classes3.dex */
    public enum Mode {
        SYNC,
        ASYNC,
        CALLBACK
    }

    Mode getInvocationMode(Request request);

    Response invoke(Request request);

    void setParams(Map<String, String> map);
}
