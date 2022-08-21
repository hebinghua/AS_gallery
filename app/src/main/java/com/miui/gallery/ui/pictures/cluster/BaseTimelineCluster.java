package com.miui.gallery.ui.pictures.cluster;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/* loaded from: classes2.dex */
public abstract class BaseTimelineCluster implements TimelineCluster {
    public static final boolean DBG = Log.isLoggable("BaseTimelineCluster", 3);

    public abstract int getRawChildCount(int i, boolean z);

    @Override // com.miui.gallery.data.Cluster
    public int getChildCount(int i, boolean z) {
        return getRawChildCount(i, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public Rect estimateItemRect(int i, int i2, long j, int i3, int i4, int i5, boolean z) {
        int[] unpackAdapterPosition = unpackAdapterPosition(i2, z);
        float f = i5;
        float f2 = (i - ((1.0f * f) * (i3 - 1))) / i3;
        float round = Math.round(f2);
        int i6 = (unpackAdapterPosition[1] % (i4 * i3)) / i3;
        float f3 = (f + f2) * (unpackAdapterPosition[1] % i3);
        float f4 = (f + round) * i6;
        return new Rect(Math.round(f3), Math.round(f4), Math.round(f3 + f2), Math.round(f4 + round));
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int calculateMatchItemPosition(int i, int i2, float f, float f2, int i3, int i4, int i5, int i6, boolean z) {
        return packDataPosition(i, i2, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int calculateScrollPosition(int i, int i2, int i3, boolean z) {
        return unpackAdapterPosition(i, z)[0] + (z ? 1 : 0) + i;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public List<TransitFrame> calculateTransitFrames(int i, long j, float f, int i2, int i3, int i4, Function<Integer, Integer> function, int i5, int i6, Rect rect, boolean z, boolean z2, boolean z3) {
        LinkedList linkedList;
        float f2;
        char c;
        boolean z4;
        LinkedList linkedList2;
        if (DBG) {
            DefaultLogger.d("BaseTimelineCluster", "original calculate guideline %f", Float.valueOf(f));
        }
        LinkedList linkedList3 = new LinkedList();
        float f3 = (i5 - ((i4 * 1.0f) * (i2 - 1))) / i2;
        int groupCount = getGroupCount(z3);
        int[] unpackAdapterPosition = unpackAdapterPosition(i, z3);
        fillTowardsTop(unpackAdapterPosition[0], unpackAdapterPosition[1], i, f, f3, i2, i4, function, i5, z, linkedList3, z3);
        if (z2) {
            linkedList = linkedList3;
            float consumeTopSpace = consumeTopSpace(rect.top, linkedList);
            if (consumeTopSpace != 0.0f) {
                f2 = f + consumeTopSpace;
                z4 = true;
                c = 0;
                linkedList2 = linkedList;
                fillTowardsBottom(unpackAdapterPosition[c], unpackAdapterPosition[1], i, f2, f3, i2, i4, function, i5, i6, z, linkedList, z3);
                if (!z4 || !z2 || consumeBottomSpace(i6, rect.bottom + i4, groupCount, linkedList2, z3) == 0.0f) {
                    return linkedList2;
                }
                TransitFrame transitFrame = linkedList2.get(0);
                fillTowardsTop(transitFrame.groupIndex, transitFrame.childIndex, transitFrame.adapterPosition, transitFrame.frame.top, f3, i2, i4, function, i5, z, linkedList2, z3);
                consumeTopSpace(rect.top, linkedList2);
                return linkedList2;
            }
        } else {
            linkedList = linkedList3;
        }
        f2 = f;
        c = 0;
        z4 = false;
        linkedList2 = linkedList;
        fillTowardsBottom(unpackAdapterPosition[c], unpackAdapterPosition[1], i, f2, f3, i2, i4, function, i5, i6, z, linkedList, z3);
        if (!z4) {
        }
        return linkedList2;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0100  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void fillTowardsTop(int r18, int r19, int r20, float r21, float r22, int r23, int r24, java.util.function.Function<java.lang.Integer, java.lang.Integer> r25, int r26, boolean r27, java.util.List<com.miui.gallery.ui.pictures.cluster.TransitFrame> r28, boolean r29) {
        /*
            Method dump skipped, instructions count: 274
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.pictures.cluster.BaseTimelineCluster.fillTowardsTop(int, int, int, float, float, int, int, java.util.function.Function, int, boolean, java.util.List, boolean):void");
    }

    public final float consumeTopSpace(int i, List<TransitFrame> list) {
        if (list.size() > 0) {
            TransitFrame transitFrame = list.get(0);
            if (transitFrame.groupIndex <= 0 && transitFrame.childIndex <= 0) {
                float f = list.get(0).frame.top;
                float f2 = i;
                if (f > f2) {
                    float f3 = -(f - f2);
                    for (TransitFrame transitFrame2 : list) {
                        transitFrame2.frame.offset(0.0f, f3);
                    }
                    if (DBG) {
                        DefaultLogger.d("BaseTimelineCluster", "offset frames by %.5f to remove top space", Float.valueOf(f3));
                    }
                    return f3;
                }
            }
        }
        return 0.0f;
    }

    public final void fillTowardsBottom(int i, int i2, int i3, float f, float f2, int i4, int i5, Function<Integer, Integer> function, int i6, int i7, boolean z, List<TransitFrame> list, boolean z2) {
        int i8;
        int i9;
        int i10;
        float f3;
        float f4;
        int i11 = i4;
        Function<Integer, Integer> function2 = function;
        int i12 = i7;
        float round = Math.round(f2);
        int itemCount = getItemCount();
        int groupCount = getGroupCount(z2);
        int rawChildCount = getRawChildCount(i, z2);
        int i13 = i2;
        float calcLeft = calcLeft(i11, i5, f2, i13, z);
        if (DBG) {
            i8 = rawChildCount;
            DefaultLogger.d("BaseTimelineCluster", "start calculate items below guideline");
        } else {
            i8 = rawChildCount;
        }
        int i14 = i;
        int i15 = i3;
        float f5 = f;
        int i16 = i8;
        float f6 = calcLeft;
        int i17 = i16;
        while (i15 < itemCount && i14 < groupCount) {
            int i18 = itemCount;
            if (i13 >= i17) {
                float f7 = f5 + i5 + round;
                if (f7 >= i12) {
                    return;
                }
                TransitFrame transitFrame = new TransitFrame();
                i10 = groupCount;
                transitFrame.frame = new RectF(0.0f, f7, i6, function2.apply(Integer.valueOf(i14)).intValue() + f7);
                transitFrame.adapterPosition = i15;
                int i19 = i14 + 1;
                transitFrame.dataPosition = packDataPosition(i19, 0, z2);
                transitFrame.isHeader = true;
                transitFrame.groupIndex = i14;
                list.add(transitFrame);
                int rawChildCount2 = getRawChildCount(i19, z2);
                int i20 = i15;
                f3 = f2;
                f5 = f7 + function2.apply(Integer.valueOf(i19)).intValue();
                i17 = rawChildCount2;
                i9 = i20;
                f4 = calcLeft(i11, i5, f3, 0, z);
                i13 = 0;
                i14 = i19;
            } else {
                i9 = i15;
                i10 = groupCount;
                f3 = f2;
                if (i9 != i3 && i13 % i11 == 0) {
                    f6 = calcLeft(i11, i5, f3, i13, z);
                    f5 += i5 + round;
                    if (f5 >= i7) {
                        return;
                    }
                }
                f4 = f6;
            }
            TransitFrame transitFrame2 = new TransitFrame();
            int i21 = i17;
            transitFrame2.frame = new RectF(f4, f5, f4 + f3, f5 + round);
            transitFrame2.adapterPosition = i9;
            transitFrame2.dataPosition = packDataPosition(i14, i13, z2);
            transitFrame2.isHeader = false;
            transitFrame2.groupIndex = i14;
            transitFrame2.childIndex = i13;
            list.add(transitFrame2);
            f6 = f4 + ((i5 + f3) * (z ? -1 : 1));
            i13++;
            i17 = i21;
            function2 = function;
            i12 = i7;
            i15 = i9 + 1;
            itemCount = i18;
            groupCount = i10;
            i11 = i4;
        }
    }

    public final float consumeBottomSpace(int i, int i2, int i3, List<TransitFrame> list, boolean z) {
        int i4;
        if (list.size() > 0) {
            TransitFrame transitFrame = list.get(list.size() - 1);
            if (transitFrame.groupIndex >= i3 - 1 && transitFrame.childIndex >= getRawChildCount(i4, z) - 1) {
                float f = list.get(list.size() - 1).frame.bottom;
                float f2 = i - i2;
                if (f < f2) {
                    float f3 = f2 - f;
                    for (TransitFrame transitFrame2 : list) {
                        transitFrame2.frame.offset(0.0f, f3);
                    }
                    if (DBG) {
                        DefaultLogger.d("BaseTimelineCluster", "offset frames by %.5f to remove redundant bottom space", Float.valueOf(f3));
                    }
                    return f3;
                }
            }
        }
        return 0.0f;
    }

    public static float calcLeft(int i, int i2, float f, int i3, boolean z) {
        float f2;
        if (!z) {
            f2 = i3 % i;
        } else {
            f2 = (i - (i3 % i)) - 1;
        }
        return f2 * (f + i2);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public float getGroupHeight(int i, int i2, int i3, int i4, int i5, boolean z) {
        float f = i4;
        float round = Math.round((i5 - ((1.0f * f) * (i2 - 1))) / i2);
        int rawChildCount = getRawChildCount(i, z);
        int i6 = rawChildCount % i2;
        int i7 = rawChildCount / i2;
        if (i6 != 0) {
            i7++;
        }
        return i3 + i4 + (i7 * (round + f));
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int computeScrollRange(int i, int i2, int i3, Function<Integer, Integer> function, int i4, boolean z) {
        int groupCount = getGroupCount(z);
        float f = i3;
        float round = Math.round((i4 - ((1.0f * f) * (i - 1))) / i);
        int i5 = 0;
        for (int i6 = 0; i6 < groupCount; i6++) {
            i5 = (int) (i5 + function.apply(Integer.valueOf(i6)).intValue() + (getChildRows(i6, i, 1, z) * (round + f)));
        }
        return i5;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int computeScrollOffset(int i, int i2, int i3, int i4, Function<Integer, Integer> function, int i5, boolean z) {
        float f;
        float f2;
        float f3;
        int groupCount = getGroupCount(z);
        int i6 = i2 * i3;
        float round = Math.round((i5 - ((1.0f * f) * (i2 - 1))) / i2) + i4;
        float f4 = i3 * round;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i7 < groupCount) {
            int intValue = i8 + function.apply(Integer.valueOf(i7)).intValue();
            if (i7 != 0) {
                i9++;
            }
            if (i9 == i) {
                return intValue;
            }
            int rawChildCount = getRawChildCount(i7, z);
            int i10 = rawChildCount / i6;
            int i11 = rawChildCount % i6;
            if (isAggregated()) {
                rawChildCount = (i11 == 0 ? 0 : 1) + i10;
            }
            int i12 = i11 % i2;
            int i13 = i11 / i2;
            if (i12 != 0) {
                i13++;
            }
            int i14 = rawChildCount + i9;
            if (i14 >= i) {
                int i15 = i - i9;
                if (!isAggregated()) {
                    int i16 = i15 % i2;
                    i15 /= i2;
                    if (i16 != 0) {
                        i15++;
                    }
                } else if (i == i14 && i13 != 0) {
                    f2 = intValue;
                    f3 = ((i15 - 1) * f4) + (i13 * round);
                    return (int) (f2 + f3);
                }
                f2 = intValue;
                f3 = i15 * f4;
                return (int) (f2 + f3);
            }
            i8 = (int) (intValue + (i10 * f4) + (i13 * round));
            i7++;
            i9 = i14;
        }
        return i8;
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00d9, code lost:
        r13 = r13 + 1;
        r12 = 1;
     */
    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int[] computeScrollPositionAndOffset(int r18, int r19, int r20, java.util.function.Function<java.lang.Integer, java.lang.Integer> r21, int r22, int r23, float r24, boolean r25) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.pictures.cluster.BaseTimelineCluster.computeScrollPositionAndOffset(int, int, int, java.util.function.Function, int, int, float, boolean):int[]");
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int computeDataPosition(int i, int i2, int i3, Function<Integer, Integer> function, int i4, int i5, int i6, float f, int i7, boolean z) {
        int computeScrollRange = computeScrollRange(i, i2, i3, function, i5, z) - i6;
        if (computeScrollRange <= 0) {
            return 0;
        }
        int intValue = ((int) ((computeScrollRange * f) + function.apply(0).intValue() + (i7 * f))) + i4;
        float f2 = i3;
        float round = Math.round((i5 - ((1.0f * f2) * (i - 1))) / i);
        int groupCount = getGroupCount(z);
        int i8 = 0;
        while (intValue > 0) {
            for (int i9 = 0; i9 < groupCount; i9++) {
                if (intValue - function.apply(Integer.valueOf(i9)).intValue() < 0) {
                    return i8;
                }
                int intValue2 = intValue - function.apply(Integer.valueOf(i9)).intValue();
                int rawChildCount = getRawChildCount(i9, z);
                float f3 = round + f2;
                float f4 = (rawChildCount % i == 0 ? rawChildCount / i : (rawChildCount / i) + 1) * f3;
                float f5 = intValue2;
                if (f4 > f5) {
                    return i8 + (((int) (f5 / f3)) * i);
                }
                intValue = (int) (f5 - f4);
                i8 += rawChildCount;
            }
        }
        return i8;
    }
}
