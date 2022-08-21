package micloud.compat.independent.sync;

import android.content.Context;
import android.content.Intent;

/* loaded from: classes3.dex */
public class GdprUtilsCompat_V24 extends GdprUtilsCompat_V23 {
    @Override // micloud.compat.independent.sync.GdprUtilsCompat_Base, micloud.compat.independent.sync.IGdprUtilsCompat
    public void notifyPrivacyDenied(Context context) {
        Intent intent = new Intent("com.xiaomi.action.PRIVACY_DENIED");
        intent.setPackage("com.miui.cloudservice");
        if (context.getPackageManager().resolveService(intent, 0) != null) {
            context.startService(intent);
        } else {
            context.sendBroadcast(intent);
        }
    }
}
