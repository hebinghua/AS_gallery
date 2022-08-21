package com.miui.gallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MediaScannerReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("com.miui.gallery.action.MEDIA_SCANNER_SCAN_FILE".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("extra_file_path");
            boolean booleanExtra = intent.getBooleanExtra("user_generated_media", false);
            String stringExtra2 = intent.getStringExtra("referer");
            if (TextUtils.isEmpty(stringExtra)) {
                return;
            }
            DefaultLogger.d("MediaScannerReceiver", "onReceive, isUserGenerated: %b, referer: %s, path: %s", Boolean.valueOf(booleanExtra), stringExtra2, stringExtra);
            ScannerEngine.getInstance().scanPathAsync(stringExtra, 5);
        }
    }
}
