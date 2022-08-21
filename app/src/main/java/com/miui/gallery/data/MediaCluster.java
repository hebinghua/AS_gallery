package com.miui.gallery.data;

import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaSection;
import java.util.LinkedList;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

/* compiled from: MediaCluster.kt */
/* loaded from: classes.dex */
public final class MediaCluster implements Cluster {
    public static final Companion Companion = new Companion(null);
    @Deprecated
    public static final List<Integer> DUMMY_LIST = CollectionsKt__CollectionsJVMKt.listOf(0);
    public List<Integer> counts;
    public List<String> labels;
    public List<Integer> startPos;

    public MediaCluster(List<Integer> counts, List<Integer> startPos, List<String> labels) {
        Intrinsics.checkNotNullParameter(counts, "counts");
        Intrinsics.checkNotNullParameter(startPos, "startPos");
        Intrinsics.checkNotNullParameter(labels, "labels");
        this.counts = counts;
        this.startPos = startPos;
        this.labels = labels;
    }

    public MediaCluster(List<? extends IRecord> records) {
        Intrinsics.checkNotNullParameter(records, "records");
        if (records.isEmpty()) {
            this.counts = CollectionsKt__CollectionsKt.emptyList();
            this.startPos = CollectionsKt__CollectionsKt.emptyList();
            this.labels = CollectionsKt__CollectionsKt.emptyList();
            return;
        }
        int i = 0;
        if (!(records.get(0) instanceof MediaSection)) {
            throw new IllegalStateException(Intrinsics.stringPlus("First item should be ", Reflection.getOrCreateKotlinClass(MediaSection.class).getSimpleName()).toString());
        }
        LinkedList linkedList = new LinkedList();
        LinkedList linkedList2 = new LinkedList();
        LinkedList linkedList3 = new LinkedList();
        while (i < records.size()) {
            IRecord iRecord = records.get(i);
            if (iRecord instanceof MediaSection) {
                MediaSection mediaSection = (MediaSection) iRecord;
                linkedList.add(Integer.valueOf(mediaSection.getCount()));
                linkedList3.add(mediaSection.getLabel());
                linkedList2.add(Integer.valueOf(i));
                i += mediaSection.getCount() + 1;
            }
        }
        this.counts = linkedList;
        this.startPos = linkedList2;
        this.labels = linkedList3;
    }

    @Override // com.miui.gallery.data.Cluster
    public int getItemCount() {
        return CollectionsKt___CollectionsKt.sumOfInt(this.counts);
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupCount(boolean z) {
        List<Integer> list = this.counts;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        if (!z) {
            return 1;
        }
        return this.counts.size();
    }

    @Override // com.miui.gallery.data.Cluster
    public int getChildCount(int i, boolean z) {
        if (!z && i == 0) {
            return getItemCount();
        }
        if (i >= this.counts.size()) {
            return 0;
        }
        return this.counts.get(i).intValue();
    }

    @Override // com.miui.gallery.data.Cluster
    public int packDataPosition(int i, int i2, boolean z) {
        return ((z || i != 0) && i < this.startPos.size()) ? i2 + this.startPos.get(i).intValue() : i2;
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupStartPosition(int i, boolean z) {
        if ((z || i != 0) && i < this.startPos.size()) {
            return this.startPos.get(i).intValue();
        }
        return 0;
    }

    @Override // com.miui.gallery.data.Cluster
    public List<Integer> getGroupStartPositions(boolean z) {
        if (!z) {
            return DUMMY_LIST;
        }
        return this.startPos;
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
        } else if (i >= this.startPos.size()) {
            return new int[0];
        } else {
            int intValue = this.startPos.get(i).intValue();
            int intValue2 = this.counts.get(i).intValue();
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
        if (z && i < this.labels.size()) {
            return this.labels.get(i);
        }
        return null;
    }

    @Override // com.miui.gallery.data.Cluster
    public int[] unpackAdapterPosition(int i, boolean z) {
        if (!z) {
            return new int[]{0, i};
        }
        List<Integer> list = this.startPos;
        if (list == null || list.isEmpty()) {
            return new int[]{0, 0};
        }
        int binarySearch$default = CollectionsKt__CollectionsKt.binarySearch$default(this.startPos, Integer.valueOf(i), 0, 0, 6, null);
        if (binarySearch$default >= 0) {
            return new int[]{binarySearch$default, 0};
        }
        int i2 = -(binarySearch$default + 2);
        return new int[]{i2, i - this.startPos.get(i2).intValue()};
    }

    /* compiled from: MediaCluster.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
