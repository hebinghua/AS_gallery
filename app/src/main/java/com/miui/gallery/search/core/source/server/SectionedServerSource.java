package com.miui.gallery.search.core.source.server;

import android.content.Context;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.core.source.server.ConvertUtil;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class SectionedServerSource extends ServerSource {
    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public boolean canCarryLog() {
        return true;
    }

    public ConvertUtil.Filter getFilter() {
        return null;
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public boolean isPersistable(QueryInfo queryInfo) {
        return true;
    }

    public SectionedServerSource(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.source.server.ServerSource
    public SourceResult onResponse(QueryInfo queryInfo, ServerSearchRequest serverSearchRequest, Object obj) {
        return generateDefaultResult(queryInfo, createResultData(queryInfo, obj instanceof SectionedResponseData ? ((SectionedResponseData) obj).sections : null, this));
    }

    public SuggestionCursor createResultData(QueryInfo queryInfo, List<SectionedSuggestion> list, Source source) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (SectionedSuggestion sectionedSuggestion : list) {
            arrayList.add(ConvertUtil.createSuggestionSection(queryInfo, sectionedSuggestion, source, getFilter()));
        }
        if (!arrayList.isEmpty()) {
            return new GroupedSuggestionCursor(queryInfo, arrayList);
        }
        return null;
    }
}
