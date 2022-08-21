package androidx.constraintlayout.widget;

import android.util.SparseIntArray;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;

/* loaded from: classes.dex */
public class SharedValues {
    public SparseIntArray mValues = new SparseIntArray();
    public HashMap<Integer, HashSet<WeakReference<Object>>> mValuesListeners = new HashMap<>();
}
