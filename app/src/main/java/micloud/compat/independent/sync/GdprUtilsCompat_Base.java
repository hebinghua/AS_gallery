package micloud.compat.independent.sync;

import android.content.Context;

/* loaded from: classes3.dex */
public class GdprUtilsCompat_Base implements IGdprUtilsCompat {
    @Override // micloud.compat.independent.sync.IGdprUtilsCompat
    public boolean isGdprPermissionGranted(Context context) {
        return true;
    }

    @Override // micloud.compat.independent.sync.IGdprUtilsCompat
    public void notifyPrivacyDenied(Context context) {
    }
}
