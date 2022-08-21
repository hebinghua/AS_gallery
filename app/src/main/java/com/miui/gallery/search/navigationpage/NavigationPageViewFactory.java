package com.miui.gallery.search.navigationpage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.context.SearchContext;
import com.miui.gallery.search.core.display.AbstractSuggestionViewFactory;
import com.miui.gallery.search.core.display.BaseSuggestionViewHolder;
import com.miui.gallery.search.core.display.OnActionClickListener;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.DimensionUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes2.dex */
public class NavigationPageViewFactory extends AbstractSuggestionViewFactory {
    public static Map<String, Integer> sViewTypes;
    public RequestOptions mPeopleItemDisplayImageOptions;
    public RequestOptions mRecommendItemDisplayImageOptions;

    static {
        HashMap hashMap = new HashMap();
        sViewTypes = hashMap;
        hashMap.put("navigation_section_header", Integer.valueOf((int) R.layout.navigation_section_header));
        sViewTypes.put("navigation_recommend", Integer.valueOf((int) R.layout.navigation_recommend_section_content));
        sViewTypes.put("navigation_recommend_item", Integer.valueOf((int) R.layout.navigation_recommend_item));
        sViewTypes.put("navigation_people", Integer.valueOf((int) R.layout.navigation_people_section_content));
        sViewTypes.put("navigation_people_item", Integer.valueOf((int) R.layout.navigation_people_item));
        sViewTypes.put("navigation_people_more_item", Integer.valueOf((int) R.layout.navigation_people_more_item));
        sViewTypes.put("navigation_tag", Integer.valueOf((int) R.layout.navigation_tag_section_content));
        sViewTypes.put("navigation_location_tag_item", Integer.valueOf((int) R.layout.navigation_tag_item));
        sViewTypes.put("navigation_location_tag_more_item", Integer.valueOf((int) R.layout.navigation_location_more_item));
        sViewTypes.put("navigation_section_content", Integer.valueOf((int) R.layout.navigation_default_section_content));
        sViewTypes.put("navigation_warning_header", Integer.valueOf((int) R.layout.navigation_warning_section_header));
        sViewTypes.put("search_history_content", Integer.valueOf((int) R.layout.search_history_section_content));
        sViewTypes.put("search_history_item", Integer.valueOf((int) R.layout.search_history_item));
        sViewTypes.put("search_history_more_item", Integer.valueOf((int) R.layout.naviagtion_clear_all_search_history));
        sViewTypes.put("search_media_type_content", Integer.valueOf((int) R.layout.navigation_media_type_section_content));
        sViewTypes.put("search_media_type_item", Integer.valueOf((int) R.layout.navigation_media_type_item));
        sViewTypes.put("search_media_type_more_item", Integer.valueOf((int) R.layout.navigation_media_more_item));
    }

    public NavigationPageViewFactory(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public void initDisplayImageOptions(Context context) {
        super.initDisplayImageOptions(context);
        float dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.search_radius_large);
        this.mRecommendItemDisplayImageOptions = getDefaultRequestOptions().mo980transform(new GranularRoundedCorners(dimensionPixelSize, 0.0f, 0.0f, dimensionPixelSize));
        this.mPeopleItemDisplayImageOptions = getDefaultRequestOptions().mo972placeholder(R.drawable.people_face_default).mo954error(R.drawable.people_face_default).mo956fallback(R.drawable.people_face_default);
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
        if (queryInfo.getSearchType() != SearchConstants.SearchType.SEARCH_TYPE_NAVIGATION || !(suggestionCursor instanceof SuggestionSection)) {
            return null;
        }
        SearchConstants.SectionType sectionType = ((SuggestionSection) suggestionCursor).getSectionType();
        if (i == -3) {
            switch (AnonymousClass2.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()]) {
                case 2:
                    return "navigation_recommend_item";
                case 3:
                    return "navigation_people_more_item";
                case 4:
                case 5:
                    return "navigation_location_tag_more_item";
                case 6:
                    return "search_history_more_item";
                case 7:
                    return "search_media_type_more_item";
                default:
                    return "navigation_location_tag_item";
            }
        } else if (i != -2) {
            if (i == -1) {
                return AnonymousClass2.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()] != 1 ? "navigation_section_header" : "navigation_warning_header";
            } else if (i < 0 || !SearchConfig.get().getNavigationConfig().useBatchContent(sectionType)) {
                return null;
            } else {
                int i2 = AnonymousClass2.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()];
                return i2 != 2 ? i2 != 3 ? i2 != 6 ? i2 != 7 ? "navigation_location_tag_item" : "search_media_type_item" : "search_history_item" : "navigation_people_item" : "navigation_recommend_item";
            }
        } else {
            switch (AnonymousClass2.$SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[sectionType.ordinal()]) {
                case 2:
                    return "navigation_recommend";
                case 3:
                    return "navigation_people";
                case 4:
                case 5:
                    return "navigation_tag";
                case 6:
                    return "search_history_content";
                case 7:
                    return "search_media_type_content";
                default:
                    return "navigation_section_content";
            }
        }
    }

    /* renamed from: com.miui.gallery.search.navigationpage.NavigationPageViewFactory$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType;

        static {
            int[] iArr = new int[SearchConstants.SectionType.values().length];
            $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType = iArr;
            try {
                iArr[SearchConstants.SectionType.SECTION_TYPE_WARNING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_RECOMMEND.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_PEOPLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_LOCATION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_TAG.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_HISTORY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$SearchConstants$SectionType[SearchConstants.SectionType.SECTION_TYPE_MEDIA_TYPE_FOLD.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory, com.miui.gallery.search.core.display.SuggestionViewFactory
    public void bindViewHolder(QueryInfo queryInfo, Suggestion suggestion, int i, BaseSuggestionViewHolder baseSuggestionViewHolder, final OnActionClickListener onActionClickListener) {
        SuggestionSection suggestionSection = (SuggestionSection) suggestion;
        if (i == -3) {
            super.bindViewHolder(queryInfo, suggestionSection.moveToMore(), i, baseSuggestionViewHolder, onActionClickListener);
            if ((suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_PEOPLE || suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_LOCATION || suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_TAG) && suggestionSection.moveToPosition(SearchConfig.get().getNavigationConfig().getSectionMaxItemCount(suggestionSection.getSectionType())) && suggestionSection.getIntentActionURI() != null && baseSuggestionViewHolder.getIconView() != null) {
                ImageView iconView = baseSuggestionViewHolder.getIconView();
                Glide.with(iconView).clear(iconView);
                bindIcon(iconView, suggestionSection.getSuggestionIcon(), getDisplayImageOptionsForViewType(baseSuggestionViewHolder.getViewType()), null);
                Folme.useAt(iconView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(iconView, new AnimConfig[0]);
            }
            if (suggestionSection.getSectionType() != SearchConstants.SectionType.SECTION_TYPE_HISTORY) {
                return;
            }
            baseSuggestionViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.search.navigationpage.NavigationPageViewFactory.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    onActionClickListener.onClick(view, 2, null, SearchStatUtils.buildSearchEventExtras(null, new String[]{"from"}, new String[]{"from_navigation_history"}));
                }
            });
        } else if (i == -2) {
            NavigationSectionContentView navigationSectionContentView = (NavigationSectionContentView) baseSuggestionViewHolder.itemView;
            navigationSectionContentView.refreshResource();
            NavigationSectionAdapter contentAdapter = navigationSectionContentView.getContentAdapter();
            if (contentAdapter != null && suggestionSection.getSectionType() != SearchConstants.SectionType.SECTION_TYPE_HISTORY) {
                contentAdapter.changeSectionData(suggestionSection);
                return;
            }
            NavigationSectionAdapter createContentAdapter = createContentAdapter(suggestionSection);
            createContentAdapter.setActionClickListener(onActionClickListener);
            ((NavigationSectionContentView) baseSuggestionViewHolder.itemView).setContentAdapter(createContentAdapter);
        } else if (i == -1) {
            if (suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_WARNING) {
                if (baseSuggestionViewHolder.getTitle() == null) {
                    return;
                }
                if (suggestionSection.moveToFirst()) {
                    baseSuggestionViewHolder.getTitle().setText(suggestionSection.getSuggestionTitle());
                } else {
                    baseSuggestionViewHolder.getTitle().setText(suggestionSection.getSectionTitle());
                }
            } else if (baseSuggestionViewHolder.getTitle() == null) {
            } else {
                baseSuggestionViewHolder.getTitle().setText(suggestionSection.getSectionTitle());
                if (suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_PEOPLE) {
                    baseSuggestionViewHolder.getTitle().setPadding(0, (int) DimensionUtils.getDimensionPixelSize(R.dimen.search_section_people_top_margin), 0, (int) DimensionUtils.getDimensionPixelSize(R.dimen.search_navigation_section_header_padding_bottom));
                }
                if (suggestionSection.getSectionType() != SearchConstants.SectionType.SECTION_TYPE_HISTORY) {
                    return;
                }
                baseSuggestionViewHolder.getTitle().setPadding(0, (int) (DimensionUtils.getDimensionPixelSize(R.dimen.search_section_item_top_margin) - DimensionUtils.getDimensionPixelSize(R.dimen.search_section_divider_height)), 0, (int) (DimensionUtils.getDimensionPixelSize(R.dimen.search_history_title_padding_bottom) - DimensionUtils.getDimensionPixelSize(R.dimen.search_history_item_padding_top_bottom)));
            }
        } else {
            super.bindViewHolder(queryInfo, suggestion, i, baseSuggestionViewHolder, onActionClickListener);
        }
    }

    @Override // com.miui.gallery.search.core.display.AbstractSuggestionViewFactory
    public RequestOptions getDisplayImageOptionsForViewType(String str) {
        if (str == null) {
            SearchLog.e("Error", "empty view type");
            return super.getDisplayImageOptionsForViewType(null);
        } else if (str.equals("navigation_recommend_item")) {
            return this.mRecommendItemDisplayImageOptions.clone();
        } else {
            if (str.equals("navigation_people_item") || str.equals("navigation_people_more_item")) {
                return this.mPeopleItemDisplayImageOptions.clone();
            }
            return super.getDisplayImageOptionsForViewType(str);
        }
    }

    public NavigationSectionAdapter createContentAdapter(SuggestionSection suggestionSection) {
        SearchConstants.SectionType sectionType = suggestionSection.getSectionType();
        SearchConstants.SectionType sectionType2 = SearchConstants.SectionType.SECTION_TYPE_HISTORY;
        return new NavigationSectionAdapter(getContext(), SearchContext.getInstance().getSuggestionViewFactory(), suggestionSection, sectionType == sectionType2 ? "from_navigation_history" : "from_navigation", SearchConfig.get().getNavigationConfig().isNeedAppendMore(suggestionSection) && (suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_PEOPLE || suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_FEATURE || suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_LOCATION || suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_TAG || suggestionSection.getSectionType() == sectionType2));
    }
}
