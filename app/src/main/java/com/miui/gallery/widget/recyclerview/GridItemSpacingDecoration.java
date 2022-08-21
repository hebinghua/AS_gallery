package com.miui.gallery.widget.recyclerview;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.TimeLineGridHeaderItem;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import miuix.core.util.Pools;

/* loaded from: classes3.dex */
public class GridItemSpacingDecoration extends RecyclerView.ItemDecoration {
    public int[] mCachedBorders;
    public HashMap<String, Rect[]> mCachedOffsets;
    public int mHorizontalSpacing;
    public final WeakReference<RecyclerView> mHost;
    public boolean mIncludeEdge;
    public int mSpanCount;
    public final int[] mTempInts;
    public int mTotalSpace;
    public int mVerticalSpacing;

    public GridItemSpacingDecoration(RecyclerView recyclerView, boolean z) {
        this.mHorizontalSpacing = 0;
        this.mVerticalSpacing = 0;
        this.mIncludeEdge = false;
        this.mTempInts = new int[2];
        this.mHost = new WeakReference<>(recyclerView);
        this.mIncludeEdge = z;
    }

    public GridItemSpacingDecoration(RecyclerView recyclerView, boolean z, int i, int i2) {
        this(recyclerView, z);
        this.mHorizontalSpacing = i;
        this.mVerticalSpacing = i2;
    }

    public static int[] calculateItemBorders(int i, int i2) {
        int i3;
        int[] iArr = new int[i + 1];
        int i4 = 0;
        iArr[0] = 0;
        int i5 = i2 / i;
        int i6 = i2 % i;
        int i7 = 0;
        for (int i8 = 1; i8 <= i; i8++) {
            i4 += i6;
            if (i4 <= 0 || i - i4 >= i6) {
                i3 = i5;
            } else {
                i3 = i5 + 1;
                i4 -= i;
            }
            i7 += i3;
            iArr[i8] = i7;
        }
        return iArr;
    }

    public static void getSpanIndexAndSize(RecyclerView recyclerView, RecyclerView.State state, View view, int[] iArr) {
        if (state.isPreLayout()) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            assertLayoutManager(gridLayoutManager);
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            if (childAdapterPosition != -1) {
                GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
                spanSizeLookup.invalidateSpanIndexCache();
                spanSizeLookup.invalidateSpanGroupIndexCache();
                iArr[1] = spanSizeLookup.getSpanSize(childAdapterPosition);
                iArr[0] = spanSizeLookup.getSpanIndex(childAdapterPosition, gridLayoutManager.getSpanCount());
                return;
            }
        }
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        iArr[0] = layoutParams.getSpanIndex();
        iArr[1] = layoutParams.getSpanSize();
    }

    public static int[] getSpanSizeOneGroup(RecyclerView recyclerView, View view, int i) {
        int spanSize;
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        assertLayoutManager(gridLayoutManager);
        int spanCount = gridLayoutManager.getSpanCount();
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        int[] iArr = new int[spanCount];
        int i2 = i + 1;
        int i3 = 0;
        for (int i4 = 0; i4 <= i; i4++) {
            iArr[i4] = gridLayoutManager.getSpanSizeLookup().getSpanSize(childAdapterPosition - (i - i4));
            i3 += iArr[i4];
        }
        int i5 = i2;
        while (i2 < spanCount) {
            int i6 = (i2 - i) + childAdapterPosition;
            if (i6 >= recyclerView.getAdapter().getItemCount() || (i3 = i3 + (spanSize = gridLayoutManager.getSpanSizeLookup().getSpanSize(i6))) > spanCount) {
                break;
            }
            iArr[i2] = spanSize;
            i5++;
            i2++;
        }
        int[] iArr2 = new int[i5];
        System.arraycopy(iArr, 0, iArr2, 0, i5);
        return iArr2;
    }

    public static String generateOffsetsCacheKey(int[] iArr) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        for (int i = 0; i < iArr.length; i++) {
            acquire.append(iArr[i]);
            if (i < iArr.length - 1) {
                acquire.append("_");
            }
        }
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public final void cacheOffsets(int[] iArr, int i, Rect rect) {
        String generateOffsetsCacheKey = generateOffsetsCacheKey(iArr);
        if (this.mCachedOffsets == null) {
            this.mCachedOffsets = new HashMap<>();
        }
        Rect[] rectArr = this.mCachedOffsets.get(generateOffsetsCacheKey);
        if (rectArr == null) {
            rectArr = new Rect[iArr.length];
            this.mCachedOffsets.put(generateOffsetsCacheKey, rectArr);
        }
        rectArr[i] = new Rect(rect);
    }

    public final Rect getCachedOffsets(int[] iArr, int i) {
        if (this.mCachedOffsets != null) {
            Rect[] rectArr = this.mCachedOffsets.get(generateOffsetsCacheKey(iArr));
            if (rectArr != null && rectArr.length > i) {
                return rectArr[i];
            }
            return null;
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int i;
        int i2;
        Rect cachedDecorInsets;
        rect.setEmpty();
        int i3 = 0;
        if (this.mIncludeEdge) {
            int i4 = this.mHorizontalSpacing;
            int i5 = i4 / 2;
            rect.left = i5;
            rect.right = i4 - i5;
        } else {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            assertLayoutManager(gridLayoutManager);
            int spanCount = gridLayoutManager.getSpanCount();
            if (spanCount != this.mSpanCount) {
                this.mSpanCount = spanCount;
                invalidateCaches();
            }
            int width = (recyclerView.getWidth() - recyclerView.getPaddingStart()) - recyclerView.getPaddingEnd();
            if (this.mTotalSpace != width) {
                this.mTotalSpace = width;
                invalidateCaches();
            }
            if (this.mCachedBorders == null) {
                this.mCachedBorders = calculateItemBorders(spanCount, width);
            }
            if (state.isPreLayout() && recyclerView.getChildAdapterPosition(view) == -1) {
                if ((gridLayoutManager instanceof GalleryGridLayoutManager) && (cachedDecorInsets = ((GalleryGridLayoutManager) gridLayoutManager).getCachedDecorInsets(recyclerView.getChildLayoutPosition(view))) != null) {
                    rect.set(cachedDecorInsets);
                }
            } else {
                getSpanIndexAndSize(recyclerView, state, view, this.mTempInts);
                int[] iArr = this.mTempInts;
                int i6 = iArr[0];
                boolean z = true;
                int i7 = iArr[1];
                int[] spanSizeOneGroup = getSpanSizeOneGroup(recyclerView, view, i6);
                Rect cachedOffsets = getCachedOffsets(spanSizeOneGroup, i6);
                if (cachedOffsets != null) {
                    rect.set(cachedOffsets);
                } else {
                    DefaultLogger.d("GridItemSpacingDecoration", "calculate offsets");
                    if (gridLayoutManager.getLayoutDirection() != 1) {
                        z = false;
                    }
                    int i8 = this.mHorizontalSpacing;
                    float f = spanCount;
                    int i9 = (int) (i7 * ((((spanCount - 1) * 1.0f) * i8) / f));
                    if (i6 == 0) {
                        rect.left = 0;
                        if (i7 == spanCount) {
                            i9 = 0;
                        }
                        rect.right = i9;
                    } else {
                        float f2 = ((width - (i * i8)) * 1.0f) / f;
                        int i10 = this.mCachedBorders[i6];
                        float f3 = 0.0f;
                        for (int i11 = 0; i11 < i6; i11++) {
                            f3 += spanSizeOneGroup[i11] * f2;
                        }
                        float f4 = i10 - f3;
                        int i12 = this.mHorizontalSpacing - ((int) (f4 - ((i6 - 1) * i2)));
                        rect.left = i12;
                        rect.right = i9 - i12;
                    }
                    if (z) {
                        int i13 = rect.left;
                        int i14 = rect.right;
                        int i15 = i13 + i14;
                        rect.left = i15;
                        int i16 = i15 - i14;
                        rect.right = i16;
                        rect.left = i15 - i16;
                    }
                    cacheOffsets(spanSizeOneGroup, i6, rect);
                }
            }
        }
        rect.top = 0;
        if (!(view instanceof TimeLineGridHeaderItem)) {
            i3 = this.mVerticalSpacing;
        }
        rect.bottom = i3;
    }

    public final void invalidate() {
        RecyclerView recyclerView = this.mHost.get();
        if (recyclerView != null) {
            recyclerView.invalidateItemDecorations();
        }
    }

    public void setHorizontalSpacing(int i) {
        if (this.mHorizontalSpacing != i) {
            this.mHorizontalSpacing = i;
            invalidate();
        }
    }

    public void setVerticalSpacing(int i) {
        if (this.mVerticalSpacing != i) {
            this.mVerticalSpacing = i;
            invalidate();
        }
    }

    public void setSpacing(int i, int i2) {
        boolean z;
        boolean z2 = true;
        if (this.mHorizontalSpacing != i) {
            this.mHorizontalSpacing = i;
            z = true;
        } else {
            z = false;
        }
        if (this.mVerticalSpacing != i2) {
            this.mVerticalSpacing = i2;
        } else {
            z2 = z;
        }
        if (z2) {
            invalidate();
        }
    }

    public final void invalidateCaches() {
        this.mCachedBorders = null;
        this.mCachedOffsets = null;
    }

    public static void assertLayoutManager(GridLayoutManager gridLayoutManager) {
        if (gridLayoutManager != null) {
            return;
        }
        throw new IllegalArgumentException("the LayoutManager of RecyclerView should be GridLayoutManager");
    }
}
