package com.miui.gallery.search.core.display;

import android.app.Activity;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;

/* loaded from: classes2.dex */
public class SectionedSuggestionAdapter extends BaseSuggestionAdapter<GroupedSuggestionCursor<SuggestionSection>> {
    public int getHeaderCount(int i) {
        return 1;
    }

    public boolean useBatchContent(int i) {
        return false;
    }

    public SectionedSuggestionAdapter(Activity activity, SuggestionViewFactory suggestionViewFactory, String str) {
        super(activity, suggestionViewFactory, str);
    }

    public SuggestionSection getSection(int i) {
        if (getSuggestionCursor() != null) {
            return getSuggestionCursor().mo1334getGroup(i);
        }
        return null;
    }

    @Override // com.miui.gallery.search.core.display.BaseSuggestionAdapter, com.miui.gallery.search.core.display.QuickAdapterBase
    public int getInnerItemViewCount() {
        int i = 0;
        for (int i2 = 0; i2 < getSectionCount(); i2++) {
            i += getSectionTotalItemCount(i2);
        }
        return i;
    }

    public int getSectionTotalItemCount(int i) {
        return getHeaderCount(i) + getSectionItemCount(i) + ((getSection(i).moveToMore() == null || useBatchContent(i)) ? 0 : 1);
    }

    public int getSectionCount() {
        if (getSuggestionCursor() != null) {
            return getSuggestionCursor().getGroupCount();
        }
        return 0;
    }

    public int getSectionItemCount(int i) {
        if (getSection(i) == null) {
            return 0;
        }
        return useBatchContent(i) ? getHeaderCount(i) : getSection(i).getCount();
    }

    @Override // com.miui.gallery.search.core.display.BaseSuggestionAdapter, com.miui.gallery.search.core.display.QuickAdapterBase
    public int getInnerItemViewType(int i) {
        int[] indexes = getIndexes(i);
        return this.mViewTypeMap.get(this.mViewFactory.getViewType(this.mQueryInfo, getSection(indexes[0]), indexes[1])).intValue();
    }

    @Override // com.miui.gallery.search.core.display.BaseSuggestionAdapter, com.miui.gallery.search.core.display.QuickAdapterBase
    public void bindInnerItemViewHolder(BaseSuggestionViewHolder baseSuggestionViewHolder, int i) {
        int[] indexes = getIndexes(i);
        this.mViewFactory.bindViewHolder(this.mQueryInfo, getSection(indexes[0]), indexes[1], baseSuggestionViewHolder, getActionClickListener());
    }

    public int[] getIndexes(int i) {
        int[] iArr = new int[2];
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= getSectionCount()) {
                break;
            }
            int sectionTotalItemCount = getSectionTotalItemCount(i2) + i3;
            if (i < sectionTotalItemCount) {
                int headerCount = getHeaderCount(i2);
                iArr[0] = i2;
                iArr[1] = i - i3;
                if (iArr[1] < headerCount) {
                    iArr[1] = -1;
                } else if (iArr[1] == getSectionItemCount(i2) + headerCount) {
                    iArr[1] = -3;
                } else if (useBatchContent(iArr[0])) {
                    iArr[1] = -2;
                } else {
                    iArr[1] = iArr[1] - headerCount;
                }
            } else {
                i2++;
                i3 = sectionTotalItemCount;
            }
        }
        if (iArr[1] >= 0) {
            getSuggestionCursor().moveToPosition(iArr[0], iArr[1]);
        }
        return iArr;
    }
}
