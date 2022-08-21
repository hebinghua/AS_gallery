package com.miui.gallery.scanner.core.bulkoperator;

import android.content.Context;
import android.net.Uri;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class CloudMediaBulkDeleter extends MediaBulkDeleter {
    public boolean mIsNormal;

    public CloudMediaBulkDeleter(Uri uri, String str) {
        super(uri, str);
        this.mIsNormal = true;
    }

    @Override // com.miui.gallery.scanner.core.bulkoperator.MediaBulkDeleter
    public void flush(Context context) {
        boolean isValid = BaseMiscUtil.isValid(StorageUtils.getMountedVolumePaths(context));
        boolean isPrimaryStorageReadable = StorageUtils.isPrimaryStorageReadable();
        if (isValid && isPrimaryStorageReadable) {
            super.flush(context);
            return;
        }
        this.mIsNormal = false;
        if (!isValid) {
            recordStorageUnmounted();
        }
        if (isPrimaryStorageReadable) {
            return;
        }
        recordMainSDCardUnwritable();
    }

    public final void recordStorageUnmounted() {
        DefaultLogger.e("CloudMediaBulkDeleter", "No external storage mounted, skip cleanup");
        SamplingStatHelper.recordCountEvent("media_scanner", "no_external_storage_mounted", new HashMap());
    }

    public final void recordMainSDCardUnwritable() {
        DefaultLogger.e("CloudMediaBulkDeleter", "Main SDCard is unreadable, skip cleanup");
        SamplingStatHelper.recordCountEvent("media_scanner", "main_sdcard_unwritable", new HashMap());
    }
}
