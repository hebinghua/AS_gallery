package micloud.compat.independent.request;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

/* loaded from: classes3.dex */
public class BindAccountServiceCompat_V18 extends BindAccountServiceCompat_Base {
    @Override // micloud.compat.independent.request.BindAccountServiceCompat_Base, micloud.compat.independent.request.IBindAccountServiceCompat
    public boolean bindAccountService(Context context, ServiceConnection serviceConnection) {
        return bindAccountService(context, "com.xiaomi.account.action.BIND_XIAOMI_ACCOUNT_SERVICE", serviceConnection) || bindAccountService(context, "android.intent.action.BIND_XIAOMI_ACCOUNT_SERVICE", serviceConnection);
    }

    public static boolean bindAccountService(Context context, String str, ServiceConnection serviceConnection) {
        Intent intent = new Intent(str);
        intent.setPackage("com.xiaomi.account");
        return context.bindService(intent, serviceConnection, 1);
    }
}
