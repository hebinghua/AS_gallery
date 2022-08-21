package com.miui.gallery.search.resultpage;

import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.resultprocessor.ResultProcessor;
import java.util.List;

/* loaded from: classes2.dex */
public class DataListResultProcessor extends ResultProcessor<DataListSourceResult> {
    @Override // com.miui.gallery.search.core.resultprocessor.ResultProcessor
    public /* bridge */ /* synthetic */ DataListSourceResult getMergedResult(List list) {
        return getMergedResult2((List<SourceResult>) list);
    }

    @Override // com.miui.gallery.search.core.resultprocessor.ResultProcessor
    /* renamed from: getMergedResult  reason: avoid collision after fix types in other method */
    public DataListSourceResult getMergedResult2(List<SourceResult> list) {
        for (SourceResult sourceResult : list) {
            if (sourceResult instanceof DataListSourceResult) {
                return (DataListSourceResult) sourceResult;
            }
        }
        return null;
    }
}
