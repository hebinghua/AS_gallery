package okhttp3.internal.http2;

import java.util.concurrent.CountDownLatch;

/* loaded from: classes3.dex */
public final class Ping {
    public final CountDownLatch latch = new CountDownLatch(1);
    public long sent = -1;
    public long received = -1;

    public void send() {
        if (this.sent != -1) {
            throw new IllegalStateException();
        }
        this.sent = System.nanoTime();
    }

    public void receive() {
        if (this.received != -1 || this.sent == -1) {
            throw new IllegalStateException();
        }
        this.received = System.nanoTime();
        this.latch.countDown();
    }

    public void cancel() {
        if (this.received == -1) {
            long j = this.sent;
            if (j != -1) {
                this.received = j - 1;
                this.latch.countDown();
                return;
            }
        }
        throw new IllegalStateException();
    }
}
