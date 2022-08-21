package okhttp3;

/* loaded from: classes3.dex */
public interface WebSocket {
    void cancel();

    boolean close(int i, String str);

    boolean send(String str);
}
