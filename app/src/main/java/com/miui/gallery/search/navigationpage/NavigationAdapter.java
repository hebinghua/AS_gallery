package com.miui.gallery.search.navigationpage;

import android.app.Activity;
import android.view.ViewGroup;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.display.BaseSuggestionViewHolder;
import com.miui.gallery.search.core.display.SectionedSuggestionAdapter;
import com.miui.gallery.search.core.display.SuggestionViewFactory;

/* loaded from: classes2.dex */
public class NavigationAdapter extends SectionedSuggestionAdapter {
    public NavigationAdapter(Activity activity, SuggestionViewFactory suggestionViewFactory, String str) {
        super(activity, suggestionViewFactory, str);
    }

    @Override // com.miui.gallery.search.core.display.SectionedSuggestionAdapter
    public boolean useBatchContent(int i) {
        return SearchConfig.get().getNavigationConfig().useBatchContent(getSection(i).getSectionType());
    }

    @Override // com.miui.gallery.search.core.display.SectionedSuggestionAdapter
    public int getSectionItemCount(int i) {
        if (getSection(i).getSectionType() == SearchConstants.SectionType.SECTION_TYPE_WARNING) {
            return 0;
        }
        return super.getSectionItemCount(i);
    }

    @Override // com.miui.gallery.search.core.display.QuickAdapterBase, androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BaseSuggestionViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return super.mo1843onCreateViewHolder(viewGroup, i);
    }
}
