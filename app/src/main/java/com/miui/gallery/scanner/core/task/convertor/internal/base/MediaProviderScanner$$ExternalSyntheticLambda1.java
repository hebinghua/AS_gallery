package com.miui.gallery.scanner.core.task.convertor.internal.base;

import android.database.Cursor;
import com.miui.gallery.util.SafeDBUtil;
import java.util.ArrayList;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class MediaProviderScanner$$ExternalSyntheticLambda1 implements SafeDBUtil.QueryHandler {
    public static final /* synthetic */ MediaProviderScanner$$ExternalSyntheticLambda1 INSTANCE = new MediaProviderScanner$$ExternalSyntheticLambda1();

    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
    /* renamed from: handle */
    public final Object mo1808handle(Cursor cursor) {
        ArrayList lambda$scanMediaProviderVideos$1;
        lambda$scanMediaProviderVideos$1 = MediaProviderScanner.lambda$scanMediaProviderVideos$1(cursor);
        return lambda$scanMediaProviderVideos$1;
    }
}
