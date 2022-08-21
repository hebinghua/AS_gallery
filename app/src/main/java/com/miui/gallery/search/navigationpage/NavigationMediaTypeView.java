package com.miui.gallery.search.navigationpage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import com.miui.gallery.R;
import com.miui.gallery.view.ExpandAllGridView;

/* loaded from: classes2.dex */
public class NavigationMediaTypeView extends ExpandAllGridView implements NavigationSectionContentView {
    public NavigationMediaTypeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NavigationMediaTypeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setNumColumns(getResources().getInteger(R.integer.search_navigation_people_column_count));
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
