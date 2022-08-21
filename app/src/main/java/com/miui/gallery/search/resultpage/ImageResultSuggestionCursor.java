package com.miui.gallery.search.resultpage;

import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor;
import com.miui.gallery.search.core.suggestion.SuggestionSection;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ImageResultSuggestionCursor extends GroupedSuggestionCursor<SuggestionSection> {
    public SparseArray<Integer[]> mGroupCollapsedItemArray;
    public SparseBooleanArray mGroupExpandStateArray;
    public boolean mSupportExpand;

    public ImageResultSuggestionCursor(QueryInfo queryInfo, List<SuggestionSection> list, boolean z) {
        super(queryInfo, list);
        this.mSupportExpand = z;
    }

    public ImageResultSuggestionCursor(QueryInfo queryInfo, List<SuggestionSection> list, boolean z, SparseBooleanArray sparseBooleanArray) {
        this(queryInfo, list, z);
        initGroupCollapsedItemArray(list, sparseBooleanArray);
    }

    public String getGroupTitle(int i) {
        if (this.mSupportExpand && getGroupExpandState(i)) {
            return getGroupExtra(i, "expand_title");
        }
        return getGroupExtra(i, "title");
    }

    public long getGroupCreateTime(int i) {
        if (getGroupExtra(i, "create_time") != null) {
            return Long.valueOf(getGroupExtra(i, "create_time")).longValue();
        }
        return -1L;
    }

    public int getGroupCreateDate(int i) {
        if (getGroupExtra(i, "create_date") != null) {
            return Integer.valueOf(getGroupExtra(i, "create_date")).intValue();
        }
        return -1;
    }

    public String getGroupRankValue(int i) {
        return getGroupExtra(i, "rank_value");
    }

    public SparseBooleanArray getGroupExpandStateArray() {
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray(getGroupCount());
        for (int i = 0; i < getGroupCount(); i++) {
            sparseBooleanArray.put(getGroupCreateDate(i), getGroupExpandState(i));
        }
        return sparseBooleanArray;
    }

    public String getImageIds() {
        int groupCount = getGroupCount();
        ArrayList arrayList = new ArrayList(getGroupCount());
        for (int i = 0; i < groupCount; i++) {
            if (!this.mSupportExpand || getGroupExpandState(i)) {
                arrayList.add(getGroupExtra(i, "all_ids"));
            } else {
                arrayList.add(getGroupExtra(i, "collapse_visible_ids"));
            }
        }
        if (arrayList.size() > 0) {
            return TextUtils.join(",", arrayList);
        }
        return null;
    }

    public boolean getGroupExpandState(int i) {
        checkGroupPosition(i);
        return this.mSupportExpand && this.mGroupExpandStateArray.get(i);
    }

    public int getChildrenCount(int i, boolean z) {
        checkGroupPosition(i);
        if (!this.mSupportExpand || z) {
            return mo1334getGroup(i).getCount();
        }
        return this.mGroupCollapsedItemArray.get(i).length;
    }

    public String getGroupExtra(int i, String str) {
        return mo1334getGroup(i).getSectionExtras().getExtra(str);
    }

    @Override // com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor
    public boolean onMoveGroup(int i, int i2) {
        if (!this.mSupportExpand || getGroupExpandState(i)) {
            return super.onMoveGroup(i, i2);
        }
        return mo1334getGroup(i).moveToPosition(this.mGroupCollapsedItemArray.get(i)[i2].intValue());
    }

    @Override // com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor
    public int getChildrenCount(int i) {
        return getChildrenCount(i, getGroupExpandState(i));
    }

    public final void initGroupCollapsedItemArray(List<SuggestionSection> list, SparseBooleanArray sparseBooleanArray) {
        this.mGroupExpandStateArray = new SparseBooleanArray(list.size());
        this.mGroupCollapsedItemArray = new SparseArray<>(list.size());
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            SuggestionSection suggestionSection = list.get(i);
            if (sparseBooleanArray != null) {
                this.mGroupExpandStateArray.put(i, sparseBooleanArray.get(Integer.valueOf(suggestionSection.getSectionExtras().getExtra("create_date")).intValue(), false));
            }
            int columnIndex = suggestionSection.getColumnIndex("item_collapse_visibility");
            for (int i2 = 0; i2 < suggestionSection.getCount(); i2++) {
                suggestionSection.moveToPosition(i2);
                if (columnIndex >= 0 && MapBundleKey.MapObjKey.OBJ_SL_VISI.equals(suggestionSection.getString(columnIndex))) {
                    arrayList.add(Integer.valueOf(i2));
                }
            }
            Integer[] numArr = new Integer[arrayList.size()];
            if (!arrayList.isEmpty()) {
                numArr = (Integer[]) arrayList.toArray(numArr);
            }
            this.mGroupCollapsedItemArray.put(i, numArr);
            arrayList.clear();
        }
    }
}
