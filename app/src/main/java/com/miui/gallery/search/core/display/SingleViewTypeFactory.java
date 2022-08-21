package com.miui.gallery.search.core.display;

import android.content.Context;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import java.util.Collection;
import java.util.Collections;

/* loaded from: classes2.dex */
public class SingleViewTypeFactory extends AbstractSuggestionViewFactory {
    public final int mLayoutId;
    public final String mViewType;

    public SingleViewTypeFactory(String str, int i, Context context) {
        super(context);
        this.mViewType = str;
        this.mLayoutId = i;
        initDisplayImageOptions(context);
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public Collection<String> getSuggestionViewTypes() {
        return Collections.singletonList(this.mViewType);
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public String getViewType(QueryInfo queryInfo, SuggestionCursor suggestionCursor, int i) {
        return this.mViewType;
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public int getLayoutIdForViewType(String str) {
        return this.mLayoutId;
    }
}
