package com.miui.gallery.search.core.source;

import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.SourceResult;

/* loaded from: classes2.dex */
public interface Source extends SuggestionResultProvider<SourceResult> {
    int getPriority(QueryInfo queryInfo);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.search.core.source.SuggestionResultProvider
    /* renamed from: getSuggestions */
    SourceResult mo1333getSuggestions(QueryInfo queryInfo);
}
