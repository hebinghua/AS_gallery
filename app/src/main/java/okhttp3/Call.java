package okhttp3;

/* loaded from: classes3.dex */
public interface Call extends Cloneable {
    void cancel();

    void enqueue(Callback callback);
}
