package com.miui.gallery.search.navigationpage;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import com.miui.gallery.R;
import com.miui.gallery.view.ExpandAllGridView;

/* loaded from: classes2.dex */
public class NavigationPeopleView extends ExpandAllGridView implements NavigationSectionContentView {
    public NavigationPeopleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NavigationPeopleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        refreshResource();
    }

    @Override // com.miui.gallery.search.navigationpage.NavigationSectionContentView
    public void refreshResource() {
        setNumColumns(getResources().getInteger(R.integer.search_navigation_people_column_count));
        setColumnWidth(getResources().getDimensionPixelSize(R.dimen.people_face_size_regular));
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

    @Override // com.miui.gallery.view.ExpandAllGridView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        refreshResource();
        setContentAdapter(getContentAdapter());
    }
}
