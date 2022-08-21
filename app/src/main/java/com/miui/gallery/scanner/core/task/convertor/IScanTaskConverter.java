package com.miui.gallery.scanner.core.task.convertor;

import com.miui.gallery.scanner.core.task.BaseScanTask;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public interface IScanTaskConverter<TASK extends BaseScanTask> {
    default Observable<TASK> convertFlow(List<Throwable> list) {
        return null;
    }

    default List<TASK> convert(List<Throwable> list) {
        return Collections.emptyList();
    }
}
