package com.miui.gallery.ui.pictures.cluster;

import android.util.Log;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class StandardTimelineCluster extends BaseTimelineCluster {
    public static final boolean DBG = Log.isLoggable("StandardTimelineCluster", 3);
    public List<Integer> mGroupItemCount;
    public List<String> mGroupLabels;
    public List<Integer> mGroupStartPos;

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public boolean isAggregated() {
        return false;
    }

    public StandardTimelineCluster(List<Integer> list, List<Integer> list2, List<String> list3) {
        this.mGroupItemCount = list;
        this.mGroupStartPos = list2;
        this.mGroupLabels = list3;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster, com.miui.gallery.data.Cluster
    public int getItemCount() {
        List<Integer> list = this.mGroupStartPos;
        int intValue = list.get(list.size() - 1).intValue();
        List<Integer> list2 = this.mGroupItemCount;
        return intValue + list2.get(list2.size() - 1).intValue();
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupCount(boolean z) {
        if (!BaseMiscUtil.isValid(this.mGroupItemCount)) {
            return 0;
        }
        if (!z) {
            return 1;
        }
        return this.mGroupItemCount.size();
    }

    @Override // com.miui.gallery.data.Cluster
    public int packDataPosition(int i, int i2, boolean z) {
        return ((z || i != 0) && i < this.mGroupStartPos.size()) ? this.mGroupStartPos.get(i).intValue() + i2 : i2;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int packAdapterPosition(int i, int i2, boolean z) {
        return ((z || i != 0) && i < this.mGroupStartPos.size()) ? this.mGroupStartPos.get(i).intValue() + i2 : i2;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildCount(int i, int i2, int i3, boolean z) {
        if (!z && i == 0) {
            return getItemCount();
        }
        if (i >= this.mGroupItemCount.size()) {
            return 0;
        }
        return this.mGroupItemCount.get(i).intValue();
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildRows(int i, int i2, int i3, boolean z) {
        int i4 = 1;
        if (!z && i == 0) {
            int itemCount = getItemCount() / i2;
            if (getItemCount() % i2 == 0) {
                i4 = 0;
            }
            return itemCount + i4;
        } else if (i >= this.mGroupItemCount.size()) {
            return 0;
        } else {
            int intValue = this.mGroupItemCount.get(i).intValue();
            int i5 = intValue / i2;
            if (intValue % i2 == 0) {
                i4 = 0;
            }
            return i5 + i4;
        }
    }

    @Override // com.miui.gallery.ui.pictures.cluster.BaseTimelineCluster
    public int getRawChildCount(int i, boolean z) {
        if (!z && i == 0) {
            return getItemCount();
        }
        return this.mGroupItemCount.get(i).intValue();
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupStartPosition(int i, boolean z) {
        if ((z || i != 0) && i < this.mGroupStartPos.size()) {
            return this.mGroupStartPos.get(i).intValue();
        }
        return 0;
    }

    @Override // com.miui.gallery.data.Cluster
    public List<Integer> getGroupStartPositions(boolean z) {
        if (!z) {
            return Collections.singletonList(0);
        }
        return Collections.unmodifiableList(this.mGroupStartPos);
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
        } else if (i >= this.mGroupStartPos.size()) {
            return new int[0];
        } else {
            int intValue = this.mGroupStartPos.get(i).intValue();
            int intValue2 = this.mGroupItemCount.get(i).intValue();
            int[] iArr2 = new int[intValue2];
            while (i2 < intValue2) {
                iArr2[i2] = intValue + i2;
                i2++;
            }
            return iArr2;
        }
    }

    @Override // com.miui.gallery.data.Cluster
    public String getGroupLabel(int i, boolean z) {
        if (z && i < this.mGroupLabels.size()) {
            return this.mGroupLabels.get(i);
        }
        return null;
    }

    @Override // com.miui.gallery.data.Cluster
    public int[] unpackAdapterPosition(int i, boolean z) {
        if (!z) {
            return new int[]{0, i};
        }
        int binarySearch = Collections.binarySearch(this.mGroupStartPos, Integer.valueOf(i));
        if (binarySearch >= 0) {
            return new int[]{binarySearch, 0};
        }
        int i2 = -(binarySearch + 2);
        return new int[]{i2, i - this.mGroupStartPos.get(i2).intValue()};
    }
}
