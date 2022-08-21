package com.miui.gallery.search.suggestionpage;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes2.dex */
public class SearchSuggestionViewFactory extends AbstractSuggestionViewFactory {
    public static Map<String, Integer> sViewTypes;
    public RequestOptions mItemRequestOptions;
    public RequestOptions mPeopleItemRequestOptions;

    static {
        HashMap hashMap = new HashMap();
        sViewTypes = hashMap;
        hashMap.put("search_suggestion_people", Integer.valueOf((int) R.layout.face_suggestion_item));
        sViewTypes.put("search_suggestion_item", Integer.valueOf((int) R.layout.default_suggestion_item));
        sViewTypes.put("search_suggestion_no_result_header", Integer.valueOf((int) R.layout.suggestion_no_result_item));
        sViewTypes.put("search_suggestion_guide_item", Integer.valueOf((int) R.layout.guide_suggestion_item));
    }

    public SearchSuggestionViewFactory(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public void initDisplayImageOptions(Context context) {
        super.initDisplayImageOptions(context);
        this.mItemRequestOptions = getDefaultRequestOptions().mo972placeholder(R.drawable.search_suggestion_icon_default).mo954error(R.drawable.search_suggestion_icon_default).mo956fallback(R.drawable.search_suggestion_icon_default).circleCrop();
        this.mPeopleItemRequestOptions = GlideOptions.peopleFaceOf();
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
        if ((queryInfo.getSearchType() == SearchConstants.SearchType.SEARCH_TYPE_SEARCH || queryInfo.getSearchType() == SearchConstants.SearchType.SEARCH_TYPE_SUGGESTION) && (suggestionCursor instanceof SuggestionSection)) {
            SearchConstants.SectionType sectionType = ((SuggestionSection) suggestionCursor).getSectionType();
            if (sectionType == SearchConstants.SectionType.SECTION_TYPE_NO_RESULT && i == -1) {
                return "search_suggestion_no_result_header";
            }
            if (i < 0 && i != -3) {
                return null;
            }
            return sectionType == SearchConstants.SectionType.SECTION_TYPE_PEOPLE ? "search_suggestion_people" : sectionType == SearchConstants.SectionType.SECTION_TYPE_GUIDE ? "search_suggestion_guide_item" : "search_suggestion_item";
        }
        return null;
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory, com.miui.gallery.search.core.display.SuggestionViewFactory
    public void bindViewHolder(QueryInfo queryInfo, final Suggestion suggestion, final int i, BaseSuggestionViewHolder baseSuggestionViewHolder, final OnActionClickListener onActionClickListener) {
        int[] findQueryTextSpan;
        SuggestionSection suggestionSection = (SuggestionSection) suggestion;
        if (i == -1) {
            if (baseSuggestionViewHolder.getTitle() == null) {
                return;
            }
            baseSuggestionViewHolder.getTitle().setText(suggestionSection.getSectionTitle());
            return;
        }
        Suggestion item = getItem(suggestionSection, i);
        if (item == null) {
            return;
        }
        super.bindViewHolder(queryInfo, item, i, baseSuggestionViewHolder, onActionClickListener);
        if (!"search_suggestion_guide_item".equals(baseSuggestionViewHolder.getViewType()) && !TextUtils.isEmpty(queryInfo.getParam("query")) && baseSuggestionViewHolder.getTitle() != null && !TextUtils.isEmpty(item.getSuggestionTitle()) && (findQueryTextSpan = findQueryTextSpan(item.getSuggestionTitle().toLowerCase(), queryInfo.getParam("query").toLowerCase())) != null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(item.getSuggestionTitle());
            spannableStringBuilder.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.search_text_blue)), findQueryTextSpan[0], findQueryTextSpan[1], 33);
            baseSuggestionViewHolder.getTitle().setText(spannableStringBuilder);
        }
        if (item.getIntentActionURI() != null && baseSuggestionViewHolder.getClickView() != null && onActionClickListener != null) {
            baseSuggestionViewHolder.getClickView().setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.suggestionpage.SearchSuggestionViewFactory.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    String str = ((SuggestionSection) suggestion).getSectionType() == SearchConstants.SectionType.SECTION_TYPE_GUIDE ? "from_guide" : "from_suggestion";
                    SuggestionSection suggestionSection2 = (SuggestionSection) suggestion;
                    onActionClickListener.onClick(view, 0, SearchSuggestionViewFactory.this.getItem(suggestionSection2, i), SearchStatUtils.buildSearchEventExtras(null, new String[]{"from", "sectionType"}, new String[]{str, suggestionSection2.getSectionTypeString()}));
                }
            });
        }
        Folme.useAt(baseSuggestionViewHolder.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(baseSuggestionViewHolder.itemView, new AnimConfig[0]);
    }

    public final Suggestion getItem(SuggestionSection suggestionSection, int i) {
        if (i == -3) {
            return suggestionSection.moveToMore();
        }
        if (i < 0) {
            return null;
        }
        suggestionSection.moveToPosition(i);
        return suggestionSection.getCurrent();
    }

    public final int[] findQueryTextSpan(String str, String str2) {
        int indexOf = str.indexOf(str2);
        if (indexOf >= 0) {
            if (str.lastIndexOf(str2) != indexOf) {
                int indexOf2 = str.indexOf("\"" + str2);
                if (indexOf2 < 0) {
                    indexOf2 = str.indexOf("“" + str2);
                }
                if (indexOf2 >= 0) {
                    indexOf = indexOf2 + 1;
                }
            }
            return new int[]{indexOf, indexOf + str2.length()};
        }
        return null;
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public RequestOptions getDisplayImageOptionsForViewType(String str) {
        if (str.equals("search_suggestion_people") || str.equals("search_suggestion_guide_item")) {
            return this.mPeopleItemRequestOptions.clone();
        }
        return this.mItemRequestOptions.clone();
    }
}
