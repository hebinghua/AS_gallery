package com.miui.gallery.scanner.core.task.convertor;

import android.content.Context;
import com.miui.gallery.scanner.core.task.raw.MiMoverRawTask;
import com.miui.gallery.scanner.core.task.semi.MiMoverSemiTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MiMoverRawTaskConverter implements IScanTaskConverter<SemiScanTask> {
    public Context mContext;
    public MiMoverRawTask mTask;

    public MiMoverRawTaskConverter(Context context, MiMoverRawTask miMoverRawTask) {
        this.mContext = context;
        this.mTask = miMoverRawTask;
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
    public List<SemiScanTask> convert(List<Throwable> list) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(new MiMoverSemiTask(this.mContext, this.mTask.getPath(), this.mTask.getConfig()));
        return arrayList;
    }
}
