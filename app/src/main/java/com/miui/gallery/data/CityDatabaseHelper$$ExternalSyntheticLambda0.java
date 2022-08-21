package com.miui.gallery.data;

import android.database.Cursor;
import com.miui.gallery.util.SafeDBUtil;
import java.util.concurrent.ConcurrentMap;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CityDatabaseHelper$$ExternalSyntheticLambda0 implements SafeDBUtil.QueryHandler {
    public static final /* synthetic */ CityDatabaseHelper$$ExternalSyntheticLambda0 INSTANCE = new CityDatabaseHelper$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
    /* renamed from: handle */
    public final Object mo1808handle(Cursor cursor) {
        ConcurrentMap lambda$queryCityBoundary$0;
        lambda$queryCityBoundary$0 = CityDatabaseHelper.lambda$queryCityBoundary$0(cursor);
        return lambda$queryCityBoundary$0;
    }
}
