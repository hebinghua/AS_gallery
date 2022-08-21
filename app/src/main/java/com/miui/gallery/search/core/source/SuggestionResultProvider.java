package com.miui.gallery.search.core.source;

import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.SuggestionResult;

/* loaded from: classes2.dex */
public interface SuggestionResultProvider<C extends SuggestionResult> {
    String getName();

    /* renamed from: getSuggestions */
    C mo1333getSuggestions(QueryInfo queryInfo);

    boolean match(QueryInfo queryInfo);
}
