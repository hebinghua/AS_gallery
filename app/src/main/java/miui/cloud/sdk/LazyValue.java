package miui.cloud.sdk;

/* loaded from: classes3.dex */
public abstract class LazyValue<Param, Value> {
    public volatile boolean mResolved = false;
    public volatile Value mValue;

    public abstract Value onInit(Param param);

    public final synchronized void init(Param param) {
        if (!this.mResolved) {
            this.mValue = onInit(param);
            this.mResolved = true;
        }
    }

    public final Value get(Param param) {
        if (!this.mResolved) {
            init(param);
        }
        return this.mValue;
    }
}
