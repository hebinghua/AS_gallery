package com.miui.gallery.search.core.result;

import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;

/* loaded from: classes2.dex */
public class BaseSourceResult<S extends SuggestionCursor> extends BaseSuggestionResult<S> implements SourceResult<S> {
    public final Source mSource;

    public BaseSourceResult(QueryInfo queryInfo, Source source, S s) {
        super(queryInfo, s);
        this.mSource = source;
    }

    @Override // com.miui.gallery.search.core.result.SourceResult
    public Source getSource() {
        return this.mSource;
    }
}
