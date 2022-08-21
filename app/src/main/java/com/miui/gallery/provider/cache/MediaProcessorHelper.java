package com.miui.gallery.provider.cache;

import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MediaProcessorHelper.kt */
/* loaded from: classes2.dex */
public final class MediaProcessorHelper {
    public static final MediaProcessorHelper INSTANCE = new MediaProcessorHelper();

    public final void wrapGroupInfos(List<Integer> burstIndexer, int i, List<Integer> groupStartPos, List<Integer> groupItemCount) {
        Intrinsics.checkNotNullParameter(burstIndexer, "burstIndexer");
        Intrinsics.checkNotNullParameter(groupStartPos, "groupStartPos");
        Intrinsics.checkNotNullParameter(groupItemCount, "groupItemCount");
        int size = groupStartPos.size();
        int i2 = 0;
        int i3 = 0;
        int i4 = 1;
        for (int i5 = 0; i5 < i && i2 < size; i5++) {
            if (i4 < size && groupStartPos.get(i4).intValue() == i5) {
                int i6 = i4;
                i4++;
                i2 = i6;
            }
            if (i3 >= burstIndexer.size() || burstIndexer.get(i3).intValue() != i5) {
                groupItemCount.set(i2, Integer.valueOf(groupItemCount.get(i2).intValue() - 1));
            } else {
                i3++;
            }
        }
        int size2 = groupStartPos.size();
        for (int i7 = 1; i7 < size2; i7++) {
            int i8 = i7 - 1;
            groupStartPos.set(i7, Integer.valueOf(groupStartPos.get(i8).intValue() + groupItemCount.get(i8).intValue()));
        }
    }

    public final void removeEmptyGroup(List<Integer> counts, List<Integer> startPos, List<String> locations) {
        Intrinsics.checkNotNullParameter(counts, "counts");
        Intrinsics.checkNotNullParameter(startPos, "startPos");
        Intrinsics.checkNotNullParameter(locations, "locations");
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (Integer num : counts) {
            int i2 = i + 1;
            if (num.intValue() <= 0) {
                arrayList.add(Integer.valueOf(i));
            }
            i = i2;
        }
        for (Number number : CollectionsKt___CollectionsKt.reversed(arrayList)) {
            int intValue = number.intValue();
            startPos.remove(intValue);
            counts.remove(intValue);
            locations.remove(intValue);
        }
    }
}
