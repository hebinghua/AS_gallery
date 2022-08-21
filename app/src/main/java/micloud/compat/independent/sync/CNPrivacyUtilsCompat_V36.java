package micloud.compat.independent.sync;

import android.content.Context;
import android.content.Intent;

/* loaded from: classes3.dex */
public class CNPrivacyUtilsCompat_V36 extends CNPrivacyUtilsCompat_Base {
    @Override // micloud.compat.independent.sync.CNPrivacyUtilsCompat_Base, micloud.compat.independent.sync.ICNPrivacyUtilsCompat
    public void sendCNPrivacyDeniedBroadcast(Context context) {
        Intent intent = new Intent("com.xiaomi.action.CN_PRIVACY_DENIED");
        intent.setPackage("com.miui.cloudservice");
        context.sendBroadcast(intent);
    }
}
