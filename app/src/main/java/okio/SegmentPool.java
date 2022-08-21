package okio;

import ch.qos.logback.core.FileAppender;

/* loaded from: classes3.dex */
public final class SegmentPool {
    public static long byteCount;
    public static Segment next;

    public static Segment take() {
        synchronized (SegmentPool.class) {
            Segment segment = next;
            if (segment != null) {
                next = segment.next;
                segment.next = null;
                byteCount -= FileAppender.DEFAULT_BUFFER_SIZE;
                return segment;
            }
            return new Segment();
        }
    }

    public static void recycle(Segment segment) {
        if (segment.next != null || segment.prev != null) {
            throw new IllegalArgumentException();
        }
        if (segment.shared) {
            return;
        }
        synchronized (SegmentPool.class) {
            long j = byteCount;
            if (j + FileAppender.DEFAULT_BUFFER_SIZE > 65536) {
                return;
            }
            byteCount = j + FileAppender.DEFAULT_BUFFER_SIZE;
            segment.next = next;
            segment.limit = 0;
            segment.pos = 0;
            next = segment;
        }
    }
}
