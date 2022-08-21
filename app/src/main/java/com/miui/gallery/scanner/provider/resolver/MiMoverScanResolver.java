package com.miui.gallery.scanner.provider.resolver;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import com.miui.gallery.scanner.core.MediaScannerService;
import com.miui.gallery.scanner.provider.GalleryMediaScannerProviderContract;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MiMoverScanResolver extends IScanMethodResolver {
    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public boolean handles(String str) {
        return TextUtils.equals("mi_mover_request_scan", str);
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public Bundle resolve(Context context, Bundle bundle) {
        ArrayList<String> stringArrayList = bundle.getStringArrayList("param_path_list");
        if (!BaseMiscUtil.isValid(stringArrayList)) {
            return null;
        }
        onMiMoverRequestScan(context, stringArrayList);
        return null;
    }

    public final void onMiMoverRequestScan(Context context, List<String> list) {
        Boolean bool = (Boolean) ScanCache.getInstance().get("key_mi_mover_event_start");
        if (bool == null || !bool.booleanValue()) {
            sendMiMoverEvent(context, "mi_mover_event_start");
        }
        String str = "key_mi_mover_paths" + System.currentTimeMillis();
        ScanCache.getInstance().put(str, list);
        BackgroundServiceHelper.startForegroundServiceIfNeed(context, new Intent(context, MediaScannerService.class).putExtra("key_mode", getCurrentMode()).putExtra("key_mi_mover_request", true).putExtra("key_mi_mover_request_scan_list", str));
    }

    public final void sendMiMoverEvent(Context context, String str) {
        try {
            ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(GalleryMediaScannerProviderContract.AUTHORITY_URI);
            Bundle bundle = new Bundle();
            bundle.putString("param_mi_mover_event", str);
            if (acquireContentProviderClient != null) {
                acquireContentProviderClient.call("mi_mover_event", null, bundle);
            }
            if (acquireContentProviderClient == null) {
                return;
            }
            acquireContentProviderClient.close();
        } catch (RemoteException e) {
            DefaultLogger.d("MiMoverScanResolver", e.getMessage());
        }
    }
}
