package com.miui.gallery.scanner.core.task.convertor;

import android.content.Context;
import com.miui.gallery.scanner.core.ScanContracts$Mode;
import com.miui.gallery.scanner.core.ScanRequest;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.ScanTaskConfigFactory;
import com.miui.gallery.scanner.core.task.raw.ExternalScanTask;
import com.miui.gallery.scanner.core.task.raw.InternalScanTask;
import com.miui.gallery.scanner.core.task.raw.MiMoverRawTask;
import java.util.List;

/* loaded from: classes2.dex */
public class ScanRequestConverter {
    public static ExternalScanTask convertToExternalScanTask(Context context, ScanContracts$Mode scanContracts$Mode, long j, long j2, String str, boolean z, String str2, String str3, int i) {
        return new ExternalScanTask(context, j, j2, str, z, str2, str3, i, new ScanTaskConfig.Builder().cloneFrom(ScanTaskConfigFactory.get(15)).setMode(scanContracts$Mode).build());
    }

    public static InternalScanTask convertToInternalScanTask(Context context, ScanContracts$Mode scanContracts$Mode, ScanRequest scanRequest) {
        return new InternalScanTask(context, scanRequest, new ScanTaskConfig.Builder().cloneFrom(ScanTaskConfigFactory.get(scanRequest.getSceneCode())).setMode(scanContracts$Mode).build());
    }

    public static MiMoverRawTask convertToMiMoverRawTask(Context context, ScanContracts$Mode scanContracts$Mode, List<String> list) {
        return new MiMoverRawTask(context, list, new ScanTaskConfig.Builder().cloneFrom(ScanTaskConfigFactory.get(20)).setMode(scanContracts$Mode).build());
    }
}
