package com.miui.gallery.ui.pictures.cluster;

import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.LazyValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class FilteredTimelineCluster extends BaseTimelineCluster {
    public final LazyValue<Void, Integer> ITEM_COUNT = new LazyValue<Void, Integer>() { // from class: com.miui.gallery.ui.pictures.cluster.FilteredTimelineCluster.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Integer mo1272onInit(Void r3) {
            int i = 0;
            for (List<Integer> list : FilteredTimelineCluster.this.mPositionIndexes) {
                i += list.size();
            }
            return Integer.valueOf(i);
        }
    };
    public List<String> mGroupLocations;
    public List<List<Integer>> mPositionIndexes;

    public FilteredTimelineCluster(List<List<Integer>> list, List<String> list2) {
        this.mPositionIndexes = list;
        this.mGroupLocations = list2;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster, com.miui.gallery.data.Cluster
    public int getItemCount() {
        return this.ITEM_COUNT.get(null).intValue();
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupCount(boolean z) {
        if (!BaseMiscUtil.isValid(this.mPositionIndexes)) {
            return 0;
        }
        if (!z) {
            return 1;
        }
        return this.mPositionIndexes.size();
    }

    @Override // com.miui.gallery.data.Cluster
    public int packDataPosition(int i, int i2, boolean z) {
        return ((z || i != 0) && i < this.mPositionIndexes.size() && i2 < this.mPositionIndexes.get(i).size()) ? this.mPositionIndexes.get(i).get(i2).intValue() : i2;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int packAdapterPosition(int i, int i2, boolean z) {
        if (z || i != 0) {
            int i3 = 0;
            for (int i4 = 0; i4 < i; i4++) {
                i3 += this.mPositionIndexes.get(i4).size();
            }
            return i3 + i2;
        }
        return i2;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.BaseTimelineCluster
    public int getRawChildCount(int i, boolean z) {
        if (!z && i == 0) {
            return getItemCount();
        }
        if (i >= this.mPositionIndexes.size()) {
            return 0;
        }
        return this.mPositionIndexes.get(i).size();
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupStartPosition(int i, boolean z) {
        if ((z || i != 0) && i < this.mPositionIndexes.size()) {
            return this.mPositionIndexes.get(i).get(0).intValue();
        }
        return 0;
    }

    @Override // com.miui.gallery.data.Cluster
    public List<Integer> getGroupStartPositions(boolean z) {
        if (!z) {
            return Collections.singletonList(0);
        }
        ArrayList arrayList = new ArrayList(this.mPositionIndexes.size());
        for (List<Integer> list : this.mPositionIndexes) {
            arrayList.add(list.get(0));
        }
        return arrayList;
    }

    @Override // com.miui.gallery.data.Cluster
    public int[] getGroupPositions(int i, boolean z) {
        int i2 = 0;
        if (!z && i == 0) {
            int itemCount = getItemCount();
            int[] iArr = new int[itemCount];
            while (i2 < itemCount) {
                iArr[i2] = i2;
                i2++;
            }
            return iArr;
        } else if (i >= this.mPositionIndexes.size()) {
            return new int[0];
        } else {
            int[] iArr2 = new int[this.mPositionIndexes.get(i).size()];
            for (Integer num : this.mPositionIndexes.get(i)) {
                iArr2[i2] = num.intValue();
                i2++;
            }
            return iArr2;
        }
    }

    @Override // com.miui.gallery.data.Cluster
    public String getGroupLabel(int i, boolean z) {
        if (z && i < this.mGroupLocations.size()) {
            return this.mGroupLocations.get(i);
        }
        return null;
    }

    @Override // com.miui.gallery.data.Cluster
    public int[] unpackAdapterPosition(int i, boolean z) {
        int i2;
        if (!z) {
            return new int[]{0, i};
        }
        Iterator<List<Integer>> it = this.mPositionIndexes.iterator();
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (!it.hasNext()) {
                i2 = 0;
                break;
            }
            List<Integer> next = it.next();
            if (next.size() + i3 >= i + 1) {
                i2 = i - i3;
                break;
            }
            i3 += next.size();
            i4++;
        }
        return new int[]{i4, i2};
    }
}
