package micloud.compat.independent.sync;

import android.content.Context;

/* loaded from: classes3.dex */
public interface IGdprUtilsCompat {
    boolean isGdprPermissionGranted(Context context);

    void notifyPrivacyDenied(Context context);
}
