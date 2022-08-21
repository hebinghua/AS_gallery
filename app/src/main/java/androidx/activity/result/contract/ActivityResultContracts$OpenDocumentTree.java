package androidx.activity.result.contract;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.activity.result.contract.ActivityResultContract;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ActivityResultContracts.kt */
/* loaded from: classes.dex */
public class ActivityResultContracts$OpenDocumentTree extends ActivityResultContract<Uri, Uri> {
    @Override // androidx.activity.result.contract.ActivityResultContract
    public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context context, Uri uri) {
        Intrinsics.checkNotNullParameter(context, "context");
        return null;
    }

    @Override // androidx.activity.result.contract.ActivityResultContract
    public Intent createIntent(Context context, Uri uri) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        if (Build.VERSION.SDK_INT >= 26 && uri != null) {
            intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        }
        return intent;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.activity.result.contract.ActivityResultContract
    /* renamed from: parseResult */
    public final Uri mo1398parseResult(int i, Intent intent) {
        if (!(i == -1)) {
            intent = null;
        }
        if (intent == null) {
            return null;
        }
        return intent.getData();
    }
}
