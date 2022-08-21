package com.miui.gallery.scanner.core.task.convertor.internal.base;

import android.database.Cursor;
import com.miui.gallery.util.SafeDBUtil;
import java.util.ArrayList;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class MediaProviderScanner$$ExternalSyntheticLambda0 implements SafeDBUtil.QueryHandler {
    public static final /* synthetic */ MediaProviderScanner$$ExternalSyntheticLambda0 INSTANCE = new MediaProviderScanner$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
    /* renamed from: handle */
    public final Object mo1808handle(Cursor cursor) {
        ArrayList lambda$scanMediaProviderImages$0;
        lambda$scanMediaProviderImages$0 = MediaProviderScanner.lambda$scanMediaProviderImages$0(cursor);
        return lambda$scanMediaProviderImages$0;
    }
}
