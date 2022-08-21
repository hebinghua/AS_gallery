package com.nexstreaming.kminternal.nexvideoeditor;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import ch.qos.logback.core.util.FileSize;

/* compiled from: NexCache.java */
/* loaded from: classes3.dex */
public class b {
    private static b a;
    private LruCache b;

    public static b a() {
        if (a == null) {
            a = new b(((int) (Runtime.getRuntime().maxMemory() / FileSize.KB_COEFFICIENT)) / 8);
        }
        return a;
    }

    private b(int i) {
        a(i);
    }

    private void a(int i) {
        this.b = new LruCache<Object, Bitmap>(i) { // from class: com.nexstreaming.kminternal.nexvideoeditor.b.1
            @Override // android.util.LruCache
            /* renamed from: a */
            public void entryRemoved(boolean z, Object obj, Bitmap bitmap, Bitmap bitmap2) {
                if (z) {
                    Log.d("NexCache", "the entry is being removed to make space / key:" + obj);
                } else {
                    Log.d("NexCache", "the removal was caused by a put(K, V) or remove(K) / key:" + obj);
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }

            @Override // android.util.LruCache
            /* renamed from: a */
            public int sizeOf(Object obj, Bitmap bitmap) {
                Log.d("NexCache", "returns the size of the entry: <key, value>:" + obj + "," + (bitmap.getByteCount() / 1024));
                return bitmap.getByteCount() / 1024;
            }
        };
        Log.d("NexCache", "cache create (size, isUsedToEntryCnt)=" + i);
    }

    public void a(Object obj, Bitmap bitmap) throws NullPointerException {
        if (a(obj) == null) {
            this.b.put(obj, bitmap);
        }
    }

    public Bitmap a(Object obj) throws NullPointerException {
        return (Bitmap) this.b.get(obj);
    }

    public void b(Object obj) {
        Log.d("NexCache", "remove id:" + obj.toString());
        if (this.b.remove(obj) == null) {
            Log.d("NexCache", obj.toString() + " isn't in the cache.");
            return;
        }
        Log.d("NexCache", obj.toString() + " removes in the cache.");
    }

    public void b() {
        Log.d("NexCache", "releaseCache()");
        this.b.evictAll();
    }
}
