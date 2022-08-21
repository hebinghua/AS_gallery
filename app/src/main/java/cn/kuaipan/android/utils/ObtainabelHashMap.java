package cn.kuaipan.android.utils;

import java.util.HashMap;

/* loaded from: classes.dex */
public class ObtainabelHashMap<K, V> extends HashMap<K, V> implements IObtainable {
    public static ObtainabelHashMap<?, ?> mPool = null;
    public static int mPoolSize = 0;
    public static Object mPoolSync = new Object();
    private static final long serialVersionUID = 5201260832948788096L;
    private ObtainabelHashMap<?, ?> next;

    public static <K, V> ObtainabelHashMap<K, V> obtain() {
        synchronized (mPoolSync) {
            ObtainabelHashMap<K, V> obtainabelHashMap = (ObtainabelHashMap<K, V>) mPool;
            if (obtainabelHashMap != null) {
                mPool = ((ObtainabelHashMap) obtainabelHashMap).next;
                ((ObtainabelHashMap) obtainabelHashMap).next = null;
                mPoolSize--;
                obtainabelHashMap.clear();
                return obtainabelHashMap;
            }
            return new ObtainabelHashMap<>();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.kuaipan.android.utils.IObtainable
    public void recycle() {
        synchronized (mPoolSync) {
            int i = mPoolSize;
            if (i < 500) {
                mPoolSize = i + 1;
                this.next = mPool;
                mPool = this;
            }
            for (Object obj : values()) {
                if (obj instanceof IObtainable) {
                    ((IObtainable) obj).recycle();
                }
            }
            clear();
        }
    }

    private ObtainabelHashMap() {
    }
}
