package com.miui.gallery.scanner.core.scanner;

import android.content.Context;
import com.miui.gallery.scanner.core.model.IAlbumEntry;
import com.miui.gallery.scanner.core.model.IItemEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import java.nio.file.Path;

/* loaded from: classes2.dex */
public interface IMediaScanner<ALBUM extends IAlbumEntry, ITEM extends IItemEntry> {
    default ScanResult cleanFile(Context context, Path path, ITEM item, ScanTaskConfig scanTaskConfig) {
        return null;
    }

    default ScanResult scanFile(Context context, Path path, ALBUM album, ScanTaskConfig scanTaskConfig) {
        return null;
    }
}
