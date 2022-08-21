package com.miui.gallery.search.core.display;

import android.content.Context;
import android.view.ViewGroup;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.navigationpage.NavigationPageViewFactory;
import com.miui.gallery.search.resultpage.FilterBarViewFactory;
import com.miui.gallery.search.resultpage.ResultPageViewFactory;
import com.miui.gallery.search.suggestionpage.SearchSuggestionViewFactory;
import com.miui.gallery.search.utils.SearchLog;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/* loaded from: classes2.dex */
public class SuggestionViewFactoryImpl implements SuggestionViewFactory {
    public final SuggestionViewFactory mDefaultFactory;
    public final LinkedList<SuggestionViewFactory> mFactories = new LinkedList<>();
    public Map<String, SuggestionViewFactory> mViewTypeToFactoryMap = new HashMap();

    public SuggestionViewFactoryImpl(Context context) {
        SingleViewTypeFactory singleViewTypeFactory = new SingleViewTypeFactory(context) { // from class: com.miui.gallery.search.core.display.DefaultSuggestionView$Factory
        };
        this.mDefaultFactory = singleViewTypeFactory;
        addFactory(singleViewTypeFactory);
        addFactory(new SingleViewTypeFactory(context) { // from class: com.miui.gallery.search.core.display.DefaultSectionHeaderView$Factory
            @Override // com.miui.gallery.search.core.display.SingleViewTypeFactory, com.miui.gallery.search.core.display.SuggestionViewFactory
            public String getViewType(QueryInfo queryInfo, SuggestionCursor suggestionCursor, int i) {
                if (!(suggestionCursor instanceof SuggestionSection) || i != -1) {
                    return null;
                }
                return "section";
            }

            @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory, com.miui.gallery.search.core.display.SuggestionViewFactory
            public void bindViewHolder(QueryInfo queryInfo, Suggestion suggestion, int i, BaseSuggestionViewHolder baseSuggestionViewHolder, OnActionClickListener onActionClickListener) {
                SuggestionSection suggestionSection = (SuggestionSection) suggestion;
                if (baseSuggestionViewHolder.getTitle() != null) {
                    baseSuggestionViewHolder.getTitle().setText(suggestionSection.getSectionTitle());
                }
                if (baseSuggestionViewHolder.getSubTitle() != null) {
                    baseSuggestionViewHolder.getSubTitle().setText(suggestionSection.getSectionSubTitle());
                }
            }
        });
        addFactory(new NavigationPageViewFactory(context));
        addFactory(new ResultPageViewFactory(context));
        addFactory(new SearchSuggestionViewFactory(context));
        addFactory(new FilterBarViewFactory(context));
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public Collection<String> getSuggestionViewTypes() {
        return this.mViewTypeToFactoryMap.keySet();
    }

    public final void addFactory(SuggestionViewFactory suggestionViewFactory) {
        this.mFactories.addFirst(suggestionViewFactory);
        addViewTypes(suggestionViewFactory);
    }

    public final void addViewTypes(SuggestionViewFactory suggestionViewFactory) {
        for (String str : suggestionViewFactory.getSuggestionViewTypes()) {
            if (this.mViewTypeToFactoryMap.containsKey(str)) {
                throw new RuntimeException(String.format("The view type %s has already exists in other factory %s, please change a name", str, this.mViewTypeToFactoryMap.get(str)));
            }
            this.mViewTypeToFactoryMap.put(str, suggestionViewFactory);
        }
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public String getViewType(QueryInfo queryInfo, SuggestionCursor suggestionCursor, int i) {
        suggestionCursor.moveToPosition(i);
        Iterator<SuggestionViewFactory> it = this.mFactories.iterator();
        while (it.hasNext()) {
            String viewType = it.next().getViewType(queryInfo, suggestionCursor, i);
            if (viewType != null) {
                return viewType;
            }
        }
        return this.mDefaultFactory.getViewType(queryInfo, suggestionCursor, i);
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public BaseSuggestionViewHolder createViewHolder(ViewGroup viewGroup, String str) {
        BaseSuggestionViewHolder createViewHolder = this.mViewTypeToFactoryMap.get(str).createViewHolder(viewGroup, str);
        if (str == null) {
            SearchLog.e("Error", "empty view type");
        }
        createViewHolder.setViewType(str);
        return createViewHolder;
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public void bindViewHolder(QueryInfo queryInfo, Suggestion suggestion, int i, BaseSuggestionViewHolder baseSuggestionViewHolder, OnActionClickListener onActionClickListener) {
        if (baseSuggestionViewHolder.getViewType() == null || !this.mViewTypeToFactoryMap.containsKey(baseSuggestionViewHolder.getViewType())) {
            return;
        }
        this.mViewTypeToFactoryMap.get(baseSuggestionViewHolder.getViewType()).bindViewHolder(queryInfo, suggestion, i, baseSuggestionViewHolder, onActionClickListener);
    }
}
