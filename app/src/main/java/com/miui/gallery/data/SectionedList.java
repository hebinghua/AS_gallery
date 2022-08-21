package com.miui.gallery.data;

import java.util.List;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SectionedList.kt */
/* loaded from: classes.dex */
public final class SectionedList<ITEM, CONTENT extends ITEM, SECTION extends ITEM> extends AbstractList<ITEM> {
    public final Cluster cluster;
    public final List<CONTENT> contents;
    public final int[] groupStartPos;
    public final boolean isFlatten;
    public final List<SECTION> sections;

    /* JADX WARN: Multi-variable type inference failed */
    public SectionedList(List<? extends CONTENT> contents, List<? extends SECTION> sections, Cluster cluster, boolean z) {
        int[] iArr;
        Intrinsics.checkNotNullParameter(contents, "contents");
        Intrinsics.checkNotNullParameter(sections, "sections");
        Intrinsics.checkNotNullParameter(cluster, "cluster");
        this.contents = contents;
        this.sections = sections;
        this.cluster = cluster;
        this.isFlatten = z;
        if (!z) {
            List<Integer> groupStartPositions = cluster.getGroupStartPositions(true);
            int size = groupStartPositions.size();
            iArr = new int[size];
            for (int i = 0; i < size; i++) {
                iArr[i] = groupStartPositions.get(i).intValue() + i;
            }
        } else {
            iArr = null;
        }
        this.groupStartPos = iArr;
    }

    @Override // kotlin.collections.AbstractCollection
    public int getSize() {
        if (!this.isFlatten) {
            return this.contents.size() + this.sections.size();
        }
        return this.contents.size();
    }

    /* JADX WARN: Type inference failed for: r8v1, types: [ITEM, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r8v4, types: [ITEM, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r8v6, types: [ITEM, java.lang.Object] */
    @Override // kotlin.collections.AbstractList, java.util.List
    public ITEM get(int i) {
        if (!this.isFlatten) {
            int[] iArr = this.groupStartPos;
            Intrinsics.checkNotNull(iArr);
            int binarySearch$default = ArraysKt___ArraysJvmKt.binarySearch$default(iArr, i, 0, 0, 6, null);
            if (binarySearch$default >= 0) {
                return this.sections.get(binarySearch$default);
            }
            return this.contents.get(i + binarySearch$default + 1);
        }
        return this.contents.get(i);
    }
}
