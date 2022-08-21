package com.miui.gallery.cleaner;

import android.database.Cursor;
import com.miui.gallery.util.SafeDBUtil;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SimilarScanner$$ExternalSyntheticLambda0 implements SafeDBUtil.QueryHandler {
    public static final /* synthetic */ SimilarScanner$$ExternalSyntheticLambda0 INSTANCE = new SimilarScanner$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
    /* renamed from: handle */
    public final Object mo1808handle(Cursor cursor) {
        List lambda$getExcludedAlbumIds$0;
        lambda$getExcludedAlbumIds$0 = SimilarScanner.lambda$getExcludedAlbumIds$0(cursor);
        return lambda$getExcludedAlbumIds$0;
    }
}
