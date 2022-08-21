package com.miui.gallery.scanner.core.model;

import android.database.Cursor;
import com.miui.gallery.util.SafeDBUtil;
import java.util.ArrayList;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class ExifCloudEntry$$ExternalSyntheticLambda0 implements SafeDBUtil.QueryHandler {
    public static final /* synthetic */ ExifCloudEntry$$ExternalSyntheticLambda0 INSTANCE = new ExifCloudEntry$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
    /* renamed from: handle */
    public final Object mo1808handle(Cursor cursor) {
        ArrayList lambda$static$0;
        lambda$static$0 = ExifCloudEntry.lambda$static$0(cursor);
        return lambda$static$0;
    }
}
