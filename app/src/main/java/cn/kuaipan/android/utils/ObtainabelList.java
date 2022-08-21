package cn.kuaipan.android.utils;

import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class ObtainabelList<E> extends LinkedList<E> implements IObtainable {
    public static ObtainabelList<?> mPool = null;
    public static int mPoolSize = 0;
    public static Object mPoolSync = new Object();
    private static final long serialVersionUID = 6483198895359712723L;
    private ObtainabelList<?> next;

    public static <E> ObtainabelList<E> obtain() {
        synchronized (mPoolSync) {
            ObtainabelList<E> obtainabelList = (ObtainabelList<E>) mPool;
            if (obtainabelList != null) {
                mPool = ((ObtainabelList) obtainabelList).next;
                ((ObtainabelList) obtainabelList).next = null;
                mPoolSize--;
                obtainabelList.clear();
                return obtainabelList;
            }
            return new ObtainabelList<>();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.kuaipan.android.utils.IObtainable
    public void recycle() {
        synchronized (mPoolSync) {
            int i = mPoolSize;
            if (i < 80) {
                mPoolSize = i + 1;
                this.next = mPool;
                mPool = this;
            }
            Iterator it = iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next instanceof IObtainable) {
                    ((IObtainable) next).recycle();
                }
            }
            clear();
        }
    }

    private ObtainabelList() {
    }
}
