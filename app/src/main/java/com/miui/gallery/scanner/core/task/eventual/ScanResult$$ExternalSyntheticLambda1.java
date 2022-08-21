package com.miui.gallery.scanner.core.task.eventual;

import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class ScanResult$$ExternalSyntheticLambda1 implements Supplier {
    public static final /* synthetic */ ScanResult$$ExternalSyntheticLambda1 INSTANCE = new ScanResult$$ExternalSyntheticLambda1();

    @Override // java.util.function.Supplier
    public final Object get() {
        ScanResult.Result result;
        result = ScanResult.Result.RETRY;
        return result;
    }
}
