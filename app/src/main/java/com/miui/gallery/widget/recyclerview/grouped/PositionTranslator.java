package com.miui.gallery.widget.recyclerview.grouped;

/* loaded from: classes3.dex */
public class PositionTranslator {
    public int[] mCachedGroupId;
    public long[] mCachedGroupPosInfo;
    public int mChildCount;
    public int mGroupCount;
    public int mEndOfCalculatedOffsetGroupPosition = -1;
    public boolean mIsFlatten = false;

    public void build(GroupedItemAdapter groupedItemAdapter) {
        boolean isFlatten = groupedItemAdapter.isFlatten();
        int groupCount = groupedItemAdapter.getGroupCount();
        if (!isFlatten) {
            enlargeArraysIfNeeded(groupCount, false);
            long[] jArr = this.mCachedGroupPosInfo;
            int[] iArr = this.mCachedGroupId;
            int i = 0;
            for (int i2 = 0; i2 < groupCount; i2++) {
                long groupId = groupedItemAdapter.getGroupId(i2);
                int childCount = groupedItemAdapter.getChildCount(i2);
                jArr[i2] = ((i2 + i) << 32) | childCount | 2147483648L;
                iArr[i2] = (int) (groupId & 4294967295L);
                i += childCount;
            }
            this.mIsFlatten = false;
            this.mGroupCount = groupCount;
            this.mChildCount = i;
            this.mEndOfCalculatedOffsetGroupPosition = Math.max(0, groupCount - 1);
            return;
        }
        this.mIsFlatten = true;
        this.mGroupCount = groupCount;
        this.mChildCount = groupedItemAdapter.getChildCount(0);
        this.mEndOfCalculatedOffsetGroupPosition = 0;
    }

    public int getItemCount() {
        if (this.mIsFlatten) {
            return this.mChildCount;
        }
        return this.mGroupCount + this.mChildCount;
    }

    public long getExpandablePosition(int i) {
        long j = -1;
        if (i == -1) {
            return -1L;
        }
        int i2 = 0;
        if (this.mIsFlatten) {
            return GroupedAdapterHelper.getPackedPositionForChild(0, i);
        }
        int i3 = this.mGroupCount;
        int binarySearchGroupPositionByFlatPosition = binarySearchGroupPositionByFlatPosition(this.mCachedGroupPosInfo, this.mEndOfCalculatedOffsetGroupPosition, i);
        int i4 = this.mEndOfCalculatedOffsetGroupPosition;
        if (binarySearchGroupPositionByFlatPosition != 0) {
            i2 = (int) (this.mCachedGroupPosInfo[binarySearchGroupPositionByFlatPosition] >>> 32);
        }
        while (true) {
            if (binarySearchGroupPositionByFlatPosition >= i3) {
                binarySearchGroupPositionByFlatPosition = i4;
                break;
            }
            long[] jArr = this.mCachedGroupPosInfo;
            long j2 = jArr[binarySearchGroupPositionByFlatPosition];
            jArr[binarySearchGroupPositionByFlatPosition] = (i2 << 32) | (4294967295L & j2);
            if (i2 >= i) {
                j = GroupedAdapterHelper.getPackedPositionForGroup(binarySearchGroupPositionByFlatPosition);
                break;
            }
            i2++;
            if ((2147483648L & j2) != 0) {
                int i5 = (int) (j2 & 2147483647L);
                if (i5 > 0 && (i2 + i5) - 1 >= i) {
                    j = GroupedAdapterHelper.getPackedPositionForChild(binarySearchGroupPositionByFlatPosition, i - i2);
                    break;
                }
                i2 += i5;
            }
            i4 = binarySearchGroupPositionByFlatPosition;
            binarySearchGroupPositionByFlatPosition++;
        }
        this.mEndOfCalculatedOffsetGroupPosition = Math.max(this.mEndOfCalculatedOffsetGroupPosition, binarySearchGroupPositionByFlatPosition);
        return j;
    }

    public int getFlatPosition(long j) {
        int i = -1;
        if (j == -1) {
            return -1;
        }
        int packedPositionGroup = GroupedAdapterHelper.getPackedPositionGroup(j);
        int packedPositionChild = GroupedAdapterHelper.getPackedPositionChild(j);
        int i2 = this.mGroupCount;
        if (this.mIsFlatten) {
            return packedPositionChild;
        }
        if (packedPositionGroup >= 0 && packedPositionGroup < i2) {
            int max = Math.max(0, Math.min(packedPositionGroup, this.mEndOfCalculatedOffsetGroupPosition));
            int i3 = this.mEndOfCalculatedOffsetGroupPosition;
            int i4 = (int) (this.mCachedGroupPosInfo[max] >>> 32);
            while (true) {
                if (max >= i2) {
                    max = i3;
                    break;
                }
                long[] jArr = this.mCachedGroupPosInfo;
                long j2 = jArr[max];
                jArr[max] = (i4 << 32) | (4294967295L & j2);
                int i5 = (int) (2147483647L & j2);
                if (max != packedPositionGroup) {
                    i4++;
                    if ((j2 & 2147483648L) != 0) {
                        i4 += i5;
                    }
                    i3 = max;
                    max++;
                } else if (packedPositionChild == -1) {
                    i = i4;
                } else if (packedPositionChild < i5) {
                    i = i4 + 1 + packedPositionChild;
                }
            }
            this.mEndOfCalculatedOffsetGroupPosition = Math.max(this.mEndOfCalculatedOffsetGroupPosition, max);
        }
        return i;
    }

    public boolean isGroupPosition(int i) {
        boolean z = false;
        if (i != -1 && !this.mIsFlatten) {
            int i2 = this.mGroupCount;
            int binarySearchGroupPositionByFlatPosition = binarySearchGroupPositionByFlatPosition(this.mCachedGroupPosInfo, this.mEndOfCalculatedOffsetGroupPosition, i);
            int i3 = this.mEndOfCalculatedOffsetGroupPosition;
            int i4 = binarySearchGroupPositionByFlatPosition == 0 ? 0 : (int) (this.mCachedGroupPosInfo[binarySearchGroupPositionByFlatPosition] >>> 32);
            while (true) {
                if (binarySearchGroupPositionByFlatPosition >= i2) {
                    binarySearchGroupPositionByFlatPosition = i3;
                    break;
                }
                long[] jArr = this.mCachedGroupPosInfo;
                long j = jArr[binarySearchGroupPositionByFlatPosition];
                jArr[binarySearchGroupPositionByFlatPosition] = (i4 << 32) | (4294967295L & j);
                if (i4 >= i) {
                    z = true;
                    break;
                }
                i4++;
                if ((2147483648L & j) != 0) {
                    int i5 = (int) (j & 2147483647L);
                    if (i5 > 0 && (i4 + i5) - 1 >= i) {
                        break;
                    }
                    i4 += i5;
                }
                i3 = binarySearchGroupPositionByFlatPosition;
                binarySearchGroupPositionByFlatPosition++;
            }
            this.mEndOfCalculatedOffsetGroupPosition = Math.max(this.mEndOfCalculatedOffsetGroupPosition, binarySearchGroupPositionByFlatPosition);
            return z;
        }
        return false;
    }

    public static int binarySearchGroupPositionByFlatPosition(long[] jArr, int i, int i2) {
        int i3 = 0;
        if (i <= 0) {
            return 0;
        }
        int i4 = (int) (jArr[0] >>> 32);
        int i5 = (int) (jArr[i] >>> 32);
        if (i2 <= i4) {
            return 0;
        }
        if (i2 >= i5) {
            return i;
        }
        int i6 = 0;
        while (i3 < i) {
            int i7 = (i3 + i) >>> 1;
            if (((int) (jArr[i7] >>> 32)) < i2) {
                i6 = i3;
                i3 = i7 + 1;
            } else {
                i = i7;
            }
        }
        return i6;
    }

    public final void enlargeArraysIfNeeded(int i, boolean z) {
        int i2 = (i + 511) & (-256);
        long[] jArr = this.mCachedGroupPosInfo;
        int[] iArr = this.mCachedGroupId;
        long[] jArr2 = (jArr == null || jArr.length < i) ? new long[i2] : jArr;
        int[] iArr2 = (iArr == null || iArr.length < i) ? new int[i2] : iArr;
        if (z) {
            if (jArr != null && jArr != jArr2) {
                System.arraycopy(jArr, 0, jArr2, 0, jArr.length);
            }
            if (iArr != null && iArr != iArr2) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
            }
        }
        this.mCachedGroupPosInfo = jArr2;
        this.mCachedGroupId = iArr2;
    }
}
