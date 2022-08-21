package com.miui.gallery.search.navigationpage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import com.miui.gallery.view.ExpandAllGridView;

/* loaded from: classes2.dex */
public class SearchHistoryView extends ExpandAllGridView implements NavigationSectionContentView {
    public SearchHistoryView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SearchHistoryView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.miui.gallery.search.navigationpage.NavigationSectionContentView
    public void setContentAdapter(NavigationSectionAdapter navigationSectionAdapter) {
        setAdapter((ListAdapter) navigationSectionAdapter);
    }

    @Override // com.miui.gallery.search.navigationpage.NavigationSectionContentView
    public NavigationSectionAdapter getContentAdapter() {
        if (getAdapter() == null) {
            return null;
        }
        return (NavigationSectionAdapter) getAdapter();
    }
}
