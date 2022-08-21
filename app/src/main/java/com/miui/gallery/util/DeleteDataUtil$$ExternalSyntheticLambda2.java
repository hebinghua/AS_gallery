package com.miui.gallery.util;

import android.database.Cursor;
import com.miui.gallery.util.SafeDBUtil;
import java.util.Set;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class DeleteDataUtil$$ExternalSyntheticLambda2 implements SafeDBUtil.QueryHandler {
    public static final /* synthetic */ DeleteDataUtil$$ExternalSyntheticLambda2 INSTANCE = new DeleteDataUtil$$ExternalSyntheticLambda2();

    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
    /* renamed from: handle */
    public final Object mo1808handle(Cursor cursor) {
        Set lambda$queryExistMicroFiles$2;
        lambda$queryExistMicroFiles$2 = DeleteDataUtil.lambda$queryExistMicroFiles$2(cursor);
        return lambda$queryExistMicroFiles$2;
    }
}
