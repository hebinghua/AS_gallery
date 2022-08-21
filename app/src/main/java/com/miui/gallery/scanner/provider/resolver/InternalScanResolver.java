package com.miui.gallery.scanner.provider.resolver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.scanner.core.MediaScannerService;
import com.miui.gallery.scanner.core.ScanRequest;
import com.miui.gallery.util.BackgroundServiceHelper;

/* loaded from: classes2.dex */
public class InternalScanResolver extends IScanMethodResolver {
    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public boolean handles(String str) {
        return TextUtils.equals("internal_request_scan", str);
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public Bundle resolve(Context context, Bundle bundle) {
        bundle.setClassLoader(ScanRequest.class.getClassLoader());
        ScanRequest scanRequest = (ScanRequest) bundle.getParcelable("scan_request");
        if (scanRequest == null) {
            return null;
        }
        BackgroundServiceHelper.startForegroundServiceIfNeed(context, new Intent(context, MediaScannerService.class).putExtra("key_mode", getCurrentMode()).putExtra("key_internal_scan_request", scanRequest));
        return null;
    }
}
