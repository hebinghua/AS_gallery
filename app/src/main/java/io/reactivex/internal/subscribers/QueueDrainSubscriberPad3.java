package io.reactivex.internal.subscribers;

import java.util.concurrent.atomic.AtomicLong;

/* compiled from: QueueDrainSubscriber.java */
/* loaded from: classes3.dex */
public class QueueDrainSubscriberPad3 extends QueueDrainSubscriberPad2 {
    public final AtomicLong requested = new AtomicLong();
}
