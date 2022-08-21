package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import com.bumptech.glide.util.Util;
import java.util.Queue;

/* loaded from: classes.dex */
public abstract class BaseKeyPool<T extends Poolable> {
    public final Queue<T> keyPool = Util.createQueue(20);

    /* renamed from: create */
    public abstract T mo226create();

    public T get() {
        T poll = this.keyPool.poll();
        return poll == null ? mo226create() : poll;
    }

    public void offer(T t) {
        if (this.keyPool.size() < 20) {
            this.keyPool.offer(t);
        }
    }
}
