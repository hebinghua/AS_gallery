package com.miui.gallery.search.core.resultprocessor;

import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSuggestionResult;
import com.miui.gallery.search.core.result.ErrorInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class LinearResultProcessor extends ResultProcessor<SuggestionResult> {
    /* JADX WARN: Type inference failed for: r3v3, types: [com.miui.gallery.search.core.suggestion.SuggestionCursor, android.database.Cursor] */
    @Override // com.miui.gallery.search.core.resultprocessor.ResultProcessor
    public SuggestionResult getMergedResult(List<SourceResult> list) {
        ErrorInfo mergedErrorInfo = getMergedErrorInfo(list);
        ArrayList arrayList = new ArrayList();
        QueryInfo queryInfo = null;
        for (SourceResult sourceResult : list) {
            if (queryInfo == null && sourceResult.getQueryInfo() != null) {
                queryInfo = sourceResult.getQueryInfo();
            }
            ?? data = sourceResult.getData();
            if (data != 0) {
                for (int i = 0; i < data.getCount(); i++) {
                    if (data.moveToPosition(i)) {
                        arrayList.add(toSuggestion(data.getCurrent()));
                    }
                }
            }
        }
        return new BaseSuggestionResult(queryInfo, new ListSuggestionCursor(queryInfo, arrayList), mergedErrorInfo);
    }
}
