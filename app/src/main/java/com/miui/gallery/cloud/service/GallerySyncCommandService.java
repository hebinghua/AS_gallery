package com.miui.gallery.cloud.service;

import android.text.TextUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.sync.SyncCommandServiceBase;

/* loaded from: classes.dex */
public class GallerySyncCommandService extends SyncCommandServiceBase {
    @Override // com.xiaomi.micloudsdk.sync.SyncCommandServiceBase
    public void handleCommand(String str) {
        DefaultLogger.d("GallerySyncCommandService", "command %s", str);
        if (TextUtils.equals("value_command_cancel_sync", str)) {
            SyncUtil.stopSync(this);
        }
    }
}
