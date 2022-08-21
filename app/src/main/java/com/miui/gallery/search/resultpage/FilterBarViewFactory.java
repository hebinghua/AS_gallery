package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.AbstractSuggestionViewFactory;
import com.miui.gallery.search.core.display.BaseSuggestionViewHolder;
import com.miui.gallery.search.core.display.OnActionClickListener;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class FilterBarViewFactory extends AbstractSuggestionViewFactory {
    public static Map<String, Integer> sViewTypes;
    public RequestOptions mFilterItemRequestOptions;

    static {
        HashMap hashMap = new HashMap();
        sViewTypes = hashMap;
        hashMap.put("filter_item", Integer.valueOf((int) R.layout.filter_bar_item));
        sViewTypes.put("filter_item_no_icon", Integer.valueOf((int) R.layout.filter_bar_item_no_icon));
    }

    public FilterBarViewFactory(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public void initDisplayImageOptions(Context context) {
        super.initDisplayImageOptions(context);
        this.mFilterItemRequestOptions = GlideOptions.peopleFaceOf();
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public Collection<String> getSuggestionViewTypes() {
        return sViewTypes.keySet();
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public int getLayoutIdForViewType(String str) {
        return sViewTypes.get(str).intValue();
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public String getViewType(QueryInfo queryInfo, SuggestionCursor suggestionCursor, int i) {
        if (queryInfo.getSearchType() != SearchConstants.SearchType.SEARCH_TYPE_FILTER) {
            return null;
        }
        return TextUtils.isEmpty(suggestionCursor.getSuggestionIcon()) ? "filter_item_no_icon" : "filter_item";
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory, com.miui.gallery.search.core.display.SuggestionViewFactory
    public void bindViewHolder(QueryInfo queryInfo, final Suggestion suggestion, final int i, BaseSuggestionViewHolder baseSuggestionViewHolder, final OnActionClickListener onActionClickListener) {
        Suggestion item = getItem((SuggestionCursor) suggestion, i);
        if (item == null) {
            return;
        }
        super.bindViewHolder(queryInfo, item, i, baseSuggestionViewHolder, onActionClickListener);
        if (item.getIntentActionURI() == null || baseSuggestionViewHolder.getClickView() == null || onActionClickListener == null) {
            return;
        }
        baseSuggestionViewHolder.getClickView().setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.resultpage.FilterBarViewFactory.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                onActionClickListener.onClick(view, 0, FilterBarViewFactory.this.getItem((SuggestionCursor) suggestion, i), SearchStatUtils.buildSearchEventExtras(null, new String[]{"from"}, new String[]{"from_image_result_filter"}));
            }
        });
    }

    public final Suggestion getItem(SuggestionCursor suggestionCursor, int i) {
        suggestionCursor.moveToPosition(i);
        return suggestionCursor.getCurrent();
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public RequestOptions getDisplayImageOptionsForViewType(String str) {
        return this.mFilterItemRequestOptions.clone();
    }
}
