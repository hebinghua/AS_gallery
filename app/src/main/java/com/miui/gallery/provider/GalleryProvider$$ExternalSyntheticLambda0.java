package com.miui.gallery.provider;

import android.database.Cursor;
import com.miui.gallery.util.SafeDBUtil;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class GalleryProvider$$ExternalSyntheticLambda0 implements SafeDBUtil.QueryHandler {
    public static final /* synthetic */ GalleryProvider$$ExternalSyntheticLambda0 INSTANCE = new GalleryProvider$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
    /* renamed from: handle */
    public final Object mo1808handle(Cursor cursor) {
        List lambda$generateRecentMediaIdsSelection$0;
        lambda$generateRecentMediaIdsSelection$0 = GalleryProvider.lambda$generateRecentMediaIdsSelection$0(cursor);
        return lambda$generateRecentMediaIdsSelection$0;
    }
}
