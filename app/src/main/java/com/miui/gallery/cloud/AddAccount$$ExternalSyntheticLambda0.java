package com.miui.gallery.cloud;

import android.database.Cursor;
import com.miui.gallery.util.SafeDBUtil;
import java.util.Set;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AddAccount$$ExternalSyntheticLambda0 implements SafeDBUtil.QueryHandler {
    public static final /* synthetic */ AddAccount$$ExternalSyntheticLambda0 INSTANCE = new AddAccount$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
    /* renamed from: handle */
    public final Object mo1808handle(Cursor cursor) {
        Set lambda$queryExistSecretFiles$0;
        lambda$queryExistSecretFiles$0 = AddAccount.lambda$queryExistSecretFiles$0(cursor);
        return lambda$queryExistSecretFiles$0;
    }
}
