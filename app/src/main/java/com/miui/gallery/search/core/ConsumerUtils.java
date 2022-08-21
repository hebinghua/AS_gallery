package com.miui.gallery.search.core;

import android.os.Handler;

/* loaded from: classes2.dex */
public class ConsumerUtils {
    public static <A> void consumeAsync(Handler handler, final Consumer<A> consumer, final A a) {
        if (handler == null) {
            consumer.consume(a);
        } else {
            handler.post(new Runnable() { // from class: com.miui.gallery.search.core.ConsumerUtils.1
                @Override // java.lang.Runnable
                public void run() {
                    Consumer.this.consume(a);
                }
            });
        }
    }
}
