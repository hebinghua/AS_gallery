package micloud.compat.independent.request;

import android.content.Context;
import android.content.Intent;

/* loaded from: classes3.dex */
public class RequestInjectorCompact_V18 extends RequestInjectorCompat_Base {
    @Override // micloud.compat.independent.request.RequestInjectorCompat_Base, micloud.compat.independent.request.IRequestInjectorCompat
    public void sendDataInTransferBroadcast(Context context, int i) {
        Intent intent = new Intent("com.xiaomi.action.DATA_IN_TRANSFER");
        intent.setPackage("com.miui.cloudservice");
        intent.putExtra("retryTime", i);
        context.sendBroadcast(intent);
    }
}
