package androidx.legacy.content;

import android.content.BroadcastReceiver;
import android.os.PowerManager;
import android.util.SparseArray;

@Deprecated
/* loaded from: classes.dex */
public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
    public static final SparseArray<PowerManager.WakeLock> sActiveWakeLocks = new SparseArray<>();
    public static int mNextId = 1;
}
