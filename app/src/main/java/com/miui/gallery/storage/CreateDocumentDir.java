package com.miui.gallery.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.util.Pair;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CreateDocumentDir.kt */
/* loaded from: classes2.dex */
public final class CreateDocumentDir extends ActivityResultContract<Pair<String, Uri>, Uri> {
    @Override // androidx.activity.result.contract.ActivityResultContract
    public Intent createIntent(Context context, Pair<String, Uri> input) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(input, "input");
        Intent putExtra = new Intent("android.intent.action.CREATE_DOCUMENT").addCategory("android.intent.category.OPENABLE").setType("vnd.android.document/directory").putExtra("android.intent.extra.TITLE", input.first).putExtra("android.provider.extra.INITIAL_URI", input.second);
        Intrinsics.checkNotNullExpressionValue(putExtra, "Intent(Intent.ACTION_CRE…_INITIAL_URI, initialUri)");
        return putExtra;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.activity.result.contract.ActivityResultContract
    /* renamed from: parseResult */
    public Uri mo1398parseResult(int i, Intent intent) {
        if (intent == null || i != -1) {
            return null;
        }
        return intent.getData();
    }
}
