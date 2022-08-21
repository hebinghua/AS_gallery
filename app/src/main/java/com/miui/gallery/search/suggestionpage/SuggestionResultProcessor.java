package com.miui.gallery.search.suggestionpage;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.resultprocessor.SectionedResultProcessor;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.BaseSuggestionSection;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SuggestionResultProcessor extends SectionedResultProcessor {
    public SuggestionResultProcessor() {
        super(true);
    }

    @Override // com.miui.gallery.search.core.resultprocessor.SectionedResultProcessor
    public GroupedSuggestionCursor<SuggestionSection> packData(QueryInfo queryInfo, List<SuggestionSection> list, Bundle bundle) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            if ((list.size() == 1 && list.get(0).getSectionType() == SearchConstants.SectionType.SECTION_TYPE_SUGGESTION) || (list.get(0).getSectionType() != SearchConstants.SectionType.SECTION_TYPE_NO_RESULT && list.get(0).getSectionType() != SearchConstants.SectionType.SECTION_TYPE_SUGGESTION)) {
                GroupedSuggestionCursor<SuggestionSection> packData = super.packData(queryInfo, list, bundle);
                if (packData != null && !TextUtils.isEmpty(queryInfo.getParam("enableShortcut")) && Boolean.valueOf(queryInfo.getParam("enableShortcut")).booleanValue()) {
                    String shortcutUri = getShortcutUri(list);
                    if (!TextUtils.isEmpty(shortcutUri)) {
                        Bundle extras = packData.getExtras();
                        if (extras == null || extras == Bundle.EMPTY) {
                            extras = new Bundle();
                        }
                        extras.putString("short_cut_uri", shortcutUri);
                        packData.setExtras(extras);
                    }
                }
                return packData;
            }
            ArrayList arrayList = new ArrayList(list.size());
            ArrayList arrayList2 = new ArrayList();
            SearchConfig.SuggestionConfig suggestionConfig = SearchConfig.get().getSuggestionConfig();
            for (SuggestionSection suggestionSection : list) {
                if (!suggestionConfig.shouldDrawSectionHeader(suggestionSection.getSectionType())) {
                    for (int i = 0; i < suggestionSection.getCount(); i++) {
                        suggestionSection.moveToPosition(i);
                        arrayList2.add(suggestionSection.getCurrent());
                    }
                } else {
                    arrayList.add(suggestionSection);
                }
            }
            if (!arrayList2.isEmpty()) {
                SearchConstants.SectionType sectionType = SearchConstants.SectionType.SECTION_TYPE_SUGGESTION;
                arrayList.add(new BaseSuggestionSection(queryInfo, sectionType, new ListSuggestionCursor(queryInfo, arrayList2), null, SearchConfig.get().getTitleForSection(sectionType), null, null));
            }
            return super.packData(queryInfo, arrayList, bundle);
        } finally {
            SearchLog.d("SuggestionResultProcessor", "Pack data cost %d", Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
        }
    }

    @Override // com.miui.gallery.search.core.resultprocessor.SectionedResultProcessor
    public boolean acceptEmptySection(SuggestionSection suggestionSection) {
        return suggestionSection.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_NO_RESULT || super.acceptEmptySection(suggestionSection);
    }

    @Override // com.miui.gallery.search.core.resultprocessor.SectionedResultProcessor
    public BaseSuggestion toSuggestion(SuggestionSection suggestionSection, Suggestion suggestion) {
        BaseSuggestion suggestion2 = super.toSuggestion(suggestionSection, suggestion);
        if (suggestionSection != null && suggestion2 != null && TextUtils.isEmpty(suggestion2.getSuggestionSubTitle()) && suggestionSection.getSectionType() != SearchConstants.SectionType.SECTION_TYPE_OTHER && suggestionSection.getSectionType() != SearchConstants.SectionType.SECTION_TYPE_GUIDE) {
            suggestion2.setSuggestionSubTitle(suggestionSection.getSectionTitle());
        }
        return suggestion2;
    }

    public final String getShortcutUri(List<SuggestionSection> list) {
        if (BaseMiscUtil.isValid(list)) {
            SearchConfig.SuggestionConfig suggestionConfig = SearchConfig.get().getSuggestionConfig();
            for (SuggestionSection suggestionSection : list) {
                if (suggestionConfig.supportShortcut(suggestionSection.getSectionType()) && suggestionSection.moveToFirst() && !TextUtils.isEmpty(suggestionSection.getIntentActionURI())) {
                    return suggestionSection.getIntentActionURI();
                }
            }
            return null;
        }
        return null;
    }
}
