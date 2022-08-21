package com.miui.gallery.storage.strategies.android30;

import android.content.UriPermission;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class SAFStorageStrategy$TreeDocumentFileProvider$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ SAFStorageStrategy$TreeDocumentFileProvider$$ExternalSyntheticLambda0 INSTANCE = new SAFStorageStrategy$TreeDocumentFileProvider$$ExternalSyntheticLambda0();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((UriPermission) obj).getUri();
    }
}
