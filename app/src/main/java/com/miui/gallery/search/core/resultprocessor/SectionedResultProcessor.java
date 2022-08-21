package com.miui.gallery.search.core.resultprocessor;

import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.search.SearchConfig;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.GroupList;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSuggestionResult;
import com.miui.gallery.search.core.result.ErrorInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.suggestion.BaseSuggestion;
import com.miui.gallery.search.core.suggestion.BaseSuggestionSection;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.ListSuggestionCursor;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/* loaded from: classes2.dex */
public class SectionedResultProcessor extends ResultProcessor<SuggestionResult<GroupedSuggestionCursor<SuggestionSection>>> {
    public boolean mRemoveDuplicateItems;

    public boolean acceptEmptySection(SuggestionSection suggestionSection) {
        return false;
    }

    public SectionedResultProcessor() {
        this(false);
    }

    public SectionedResultProcessor(boolean z) {
        this.mRemoveDuplicateItems = false;
        this.mRemoveDuplicateItems = z;
    }

    @Override // com.miui.gallery.search.core.resultprocessor.ResultProcessor
    public SuggestionResult<GroupedSuggestionCursor<SuggestionSection>> getMergedResult(List<SourceResult> list) {
        ErrorInfo mergedErrorInfo = getMergedErrorInfo(list);
        ArrayList arrayList = new ArrayList();
        HashSet<String> hashSet = new HashSet<>();
        QueryInfo queryInfo = null;
        GroupedSuggestionCursor<SuggestionSection> groupedSuggestionCursor = null;
        for (SourceResult sourceResult : list) {
            if (queryInfo == null && sourceResult.getQueryInfo() != null) {
                queryInfo = sourceResult.getQueryInfo();
            }
            if (sourceResult.getData() != 0) {
                if (sourceResult.getData() instanceof SuggestionSection) {
                    SuggestionSection suggestionSection = (SuggestionSection) sourceResult.getData();
                    if (suggestionSection.getSectionType() != SearchConstants.SectionType.SECTION_TYPE_NO_RESULT || arrayList.size() <= 0) {
                        onAddSection(arrayList, hashSet, suggestionSection);
                    }
                } else if (sourceResult.getData() instanceof GroupList) {
                    GroupList groupList = (GroupList) sourceResult.getData();
                    for (int i = 0; i < groupList.getGroupCount(); i++) {
                        Object mo1334getGroup = groupList.mo1334getGroup(i);
                        if (mo1334getGroup instanceof SuggestionSection) {
                            SuggestionSection suggestionSection2 = (SuggestionSection) mo1334getGroup;
                            if (suggestionSection2.getSectionType() == SearchConstants.SectionType.SECTION_TYPE_NO_RESULT && arrayList.size() > 0) {
                                break;
                            }
                            onAddSection(arrayList, hashSet, suggestionSection2);
                        }
                    }
                }
                if (!arrayList.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("itemCount", hashSet.size());
                    groupedSuggestionCursor = packData(queryInfo, arrayList, bundle);
                }
            }
        }
        return new BaseSuggestionResult(queryInfo, groupedSuggestionCursor, mergedErrorInfo);
    }

    public GroupedSuggestionCursor<SuggestionSection> packData(QueryInfo queryInfo, List<SuggestionSection> list, Bundle bundle) {
        GroupedSuggestionCursor<SuggestionSection> groupedSuggestionCursor = new GroupedSuggestionCursor<>(queryInfo, list);
        if (bundle != null) {
            groupedSuggestionCursor.setExtras(bundle);
        }
        return groupedSuggestionCursor;
    }

    public void onAddSection(List<SuggestionSection> list, HashSet<String> hashSet, SuggestionSection suggestionSection) {
        if ((acceptEmptySection(suggestionSection) || suggestionSection.getCount() > 0 || !TextUtils.isEmpty(suggestionSection.getDataURI())) && SearchConfig.get().showSection(suggestionSection.getSectionType())) {
            list.add(getIndependentSection(hashSet, suggestionSection));
        }
    }

    public BaseSuggestion toSuggestion(SuggestionSection suggestionSection, Suggestion suggestion) {
        return super.toSuggestion(suggestion);
    }

    public BaseSuggestionSection getIndependentSection(HashSet<String> hashSet, SuggestionSection suggestionSection) {
        ArrayList arrayList = new ArrayList(suggestionSection.getCount());
        if (suggestionSection.moveToFirst()) {
            do {
                String suggestionKey = getSuggestionKey(suggestionSection, suggestionSection.getCurrent());
                if (!TextUtils.isEmpty(suggestionKey)) {
                    if (!this.mRemoveDuplicateItems || !hashSet.contains(suggestionKey)) {
                        hashSet.add(suggestionKey);
                    }
                }
                arrayList.add(toSuggestion(suggestionSection, suggestionSection.getCurrent()));
            } while (suggestionSection.moveToNext());
            return new BaseSuggestionSection(suggestionSection.getQueryInfo(), suggestionSection.getSectionTypeString(), new ListSuggestionCursor(suggestionSection.getQueryInfo(), arrayList), suggestionSection.getDataURI(), suggestionSection.getSectionTitle(), suggestionSection.getSectionSubTitle(), toSuggestion(suggestionSection, suggestionSection.moveToMore()), suggestionSection.getRankInfos(), suggestionSection.getExtras());
        }
        return new BaseSuggestionSection(suggestionSection.getQueryInfo(), suggestionSection.getSectionTypeString(), new ListSuggestionCursor(suggestionSection.getQueryInfo(), arrayList), suggestionSection.getDataURI(), suggestionSection.getSectionTitle(), suggestionSection.getSectionSubTitle(), toSuggestion(suggestionSection, suggestionSection.moveToMore()), suggestionSection.getRankInfos(), suggestionSection.getExtras());
    }

    public String getSuggestionKey(SuggestionSection suggestionSection, Suggestion suggestion) {
        if (SearchConfig.get().getSuggestionConfig().countToRecall(suggestionSection.getSectionType())) {
            return String.format("%s%s", suggestion.getSuggestionTitle(), suggestion.getIntentActionURI());
        }
        return null;
    }
}
