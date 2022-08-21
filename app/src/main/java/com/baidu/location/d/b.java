package com.baidu.location.d;

import android.util.Log;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
class b implements Runnable {
    public final /* synthetic */ WeakReference a;
    public final /* synthetic */ a b;

    public b(a aVar, WeakReference weakReference) {
        this.b = aVar;
        this.a = weakReference;
    }

    @Override // java.lang.Runnable
    public void run() {
        int i;
        a aVar = (a) this.a.get();
        if (aVar != null) {
            i = aVar.h;
            if (i != 3) {
                return;
            }
            Log.d("baidu_location_service", "baidu location service force stopped ...");
            aVar.i = false;
            aVar.b();
        }
    }
}
