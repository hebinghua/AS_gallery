package com.miui.gallery.search.resultpage;

import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSourceResult;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;

/* loaded from: classes2.dex */
public class DataListSourceResult extends BaseSourceResult {
    public long mIndexHash;
    public boolean mIsLastPage;
    public int mNextPosition;

    public DataListSourceResult(QueryInfo queryInfo, Source source, SuggestionCursor suggestionCursor, int i, boolean z, long j) {
        super(queryInfo, source, suggestionCursor);
        this.mNextPosition = i;
        this.mIsLastPage = z;
        this.mIndexHash = j;
    }

    public int getNextPosition() {
        return this.mNextPosition;
    }

    public boolean isLastPage() {
        return this.mIsLastPage;
    }

    public long getIndexHash() {
        return this.mIndexHash;
    }
}
