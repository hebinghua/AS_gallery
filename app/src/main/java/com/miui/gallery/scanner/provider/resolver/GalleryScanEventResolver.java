package com.miui.gallery.scanner.provider.resolver;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.scanner.provider.RequestMediaStoreScanRecordManager;

/* loaded from: classes2.dex */
public class GalleryScanEventResolver extends IScanMethodResolver {
    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public boolean handles(String str) {
        return TextUtils.equals("save_request_media_store_scan_record", str);
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public Bundle resolve(Context context, Bundle bundle) {
        String string = bundle.getString("param_path");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        RequestMediaStoreScanRecordManager.getInstance().record(string);
        return null;
    }
}
