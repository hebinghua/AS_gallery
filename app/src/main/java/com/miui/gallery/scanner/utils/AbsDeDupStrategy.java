package com.miui.gallery.scanner.utils;

import android.content.Context;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.model.SaveToCloud;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;

/* loaded from: classes2.dex */
public abstract class AbsDeDupStrategy {
    public abstract ScanResult doDeDup(Context context, SaveToCloud saveToCloud, SaveParams saveParams);

    public final ScanResult deDup(Context context, SaveToCloud saveToCloud, SaveParams saveParams) {
        System.currentTimeMillis();
        return doDeDup(context, saveToCloud, saveParams);
    }
}
