package com.miui.gallery.search.resultpage;

import android.content.Context;
import android.text.TextUtils;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.AbstractSuggestionViewFactory;
import com.miui.gallery.search.core.display.BaseSuggestionViewHolder;
import com.miui.gallery.search.core.display.OnActionClickListener;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/* loaded from: classes2.dex */
public class ResultPageViewFactory extends AbstractSuggestionViewFactory {
    public static final Map<String, Integer> sViewTypes;
    public RequestOptions mMapAlbumDisplayOptions;

    static {
        HashMap hashMap = new HashMap();
        sViewTypes = hashMap;
        hashMap.put("result_tag_item", Integer.valueOf((int) R.layout.result_tag_item));
        hashMap.put("result_map_album", Integer.valueOf((int) R.layout.search_map_album_cover));
    }

    public ResultPageViewFactory(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public Collection<String> getSuggestionViewTypes() {
        return sViewTypes.keySet();
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public int getLayoutIdForViewType(String str) {
        return sViewTypes.get(str).intValue();
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public boolean needSetTouchAnim(String str) {
        return TextUtils.equals(str, "result_tag_item");
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory, com.miui.gallery.search.core.display.SuggestionViewFactory
    public void bindViewHolder(QueryInfo queryInfo, Suggestion suggestion, int i, BaseSuggestionViewHolder baseSuggestionViewHolder, OnActionClickListener onActionClickListener) {
        super.bindViewHolder(queryInfo, suggestion, i, baseSuggestionViewHolder, onActionClickListener);
        if ("result_map_album".equals(baseSuggestionViewHolder.getViewType())) {
            bindSuggestionIcons(baseSuggestionViewHolder, suggestion);
        }
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public String getViewType(QueryInfo queryInfo, SuggestionCursor suggestionCursor, int i) {
        if (queryInfo.getSearchType() != SearchConstants.SearchType.SEARCH_TYPE_RESULT) {
            return null;
        }
        return i == -1 ? "result_map_album" : "result_tag_item";
    }

    public void bindSuggestionIcons(BaseSuggestionViewHolder baseSuggestionViewHolder, Suggestion suggestion) {
        if (baseSuggestionViewHolder.hasSuggestionIconsView()) {
            if (BaseMiscUtil.isValid(suggestion.getSuggestionIcons())) {
                if (suggestion.getSuggestionIcons().size() >= 4) {
                    for (int i = 0; i < baseSuggestionViewHolder.getSuggestionIconViewList().size(); i++) {
                        bindLocalImage(baseSuggestionViewHolder.getSuggestionIconViewList().get(i), suggestion.getSuggestionIcons().get(i), getDisplayImageOptionsForViewType(baseSuggestionViewHolder.getViewType()));
                    }
                    return;
                }
                bindLocalImage(baseSuggestionViewHolder.getSuggestionIconBigView(), suggestion.getSuggestionIcons().get(0), getDisplayImageOptionsForViewType(baseSuggestionViewHolder.getViewType()));
                return;
            }
            bindLocalImage(baseSuggestionViewHolder.getSuggestionIconBigView(), null, getDisplayImageOptionsForViewType(baseSuggestionViewHolder.getViewType()));
        }
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public RequestOptions getDisplayImageOptionsForViewType(String str) {
        if (Objects.equals(str, "result_map_album")) {
            if (this.mMapAlbumDisplayOptions == null) {
                this.mMapAlbumDisplayOptions = getDefaultRequestOptions().mo972placeholder(R.drawable.map_album_load_fail).mo954error(R.drawable.map_album_load_fail).mo956fallback(R.drawable.map_album_load_fail);
            }
            return this.mMapAlbumDisplayOptions;
        }
        return super.getDisplayImageOptionsForViewType(str);
    }
}
