package com.miui.gallery.scanner.core.task.raw;

import android.content.Context;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.MiMoverRawTaskConverter;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class MiMoverRawTask extends RawScanTask {
    public final List<String> mPaths;

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return this == obj;
    }

    public MiMoverRawTask(Context context, List<String> list, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mPaths = list;
        this.mRawScanTaskConverter = new MiMoverRawTaskConverter(this.mContext, this);
    }

    public List<String> getPath() {
        return this.mPaths;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        List<String> list = this.mPaths;
        return ((list != null ? 527 + Objects.hash(list) : 17) * 31) + super.hashCode();
    }
}
