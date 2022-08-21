package com.miui.gallery.ui.pictures.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.widget.ViewUtils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class MultiImageView extends View implements IMultiImageView {
    public final LinkedList<ImageCell> mActiveCells;
    public RecycledCellPoll mCellPoll;
    public RequestOptions mPreviewOptions;
    public RequestOptions mRequestOptions;
    public int mSpacing;
    public int mSpanCount;

    public MultiImageView(Context context) {
        super(context);
        this.mActiveCells = new LinkedList<>();
        this.mSpacing = 0;
        this.mSpanCount = 10;
    }

    public final RequestOptions buildRequestOptions(ImageCellData imageCellData) {
        return this.mRequestOptions.clone().mo976signature(new ObjectKey(Long.valueOf(imageCellData.mFileLength)));
    }

    public final RequestOptions buildPreviewOptions(ImageCellData imageCellData) {
        RequestOptions requestOptions = this.mPreviewOptions;
        if (requestOptions != null) {
            return requestOptions.clone().mo976signature(new ObjectKey(Long.valueOf(imageCellData.mFileLength)));
        }
        return null;
    }

    @Override // com.miui.gallery.ui.pictures.view.IMultiImageView
    public void setRecycledCellPoll(RecycledCellPoll recycledCellPoll) {
        this.mCellPoll = recycledCellPoll;
    }

    public RecycledCellPoll getRecycledCellPoll() {
        if (this.mCellPoll == null) {
            this.mCellPoll = new SoftCellPool(10);
        }
        return this.mCellPoll;
    }

    @Override // com.miui.gallery.ui.pictures.view.IMultiImageView
    public void bindData(List<ImageCellData> list) {
        int i = 0;
        boolean z = true;
        boolean z2 = calculateLines(list.size()) != calculateLines(this.mActiveCells.size());
        if (z2 || list.size() == this.mActiveCells.size()) {
            z = false;
        }
        for (int size = this.mActiveCells.size(); size < list.size(); size++) {
            this.mActiveCells.add(obtainCell());
        }
        int size2 = this.mActiveCells.size();
        for (int size3 = list.size(); size3 < size2; size3++) {
            releaseCell(this.mActiveCells.removeLast());
        }
        Iterator<ImageCell> it = this.mActiveCells.iterator();
        while (it.hasNext()) {
            int i2 = i + 1;
            ImageCellData imageCellData = list.get(i);
            it.next().bindData(imageCellData, buildRequestOptions(imageCellData), buildPreviewOptions(imageCellData));
            i = i2;
        }
        if (z2) {
            requestLayoutIfNecessary();
        } else if (z) {
            layoutCells();
        }
        invalidate();
    }

    @Override // com.miui.gallery.ui.pictures.view.IMultiImageView
    public void setSpacing(int i) {
        if (this.mSpacing != i) {
            this.mSpacing = i;
            requestLayoutIfNecessary();
        }
    }

    @Override // com.miui.gallery.ui.pictures.view.IMultiImageView
    public void setSpanCount(int i) {
        if (this.mSpanCount != i) {
            this.mSpanCount = i;
            requestLayoutIfNecessary();
        }
    }

    @Override // com.miui.gallery.ui.pictures.view.IMultiImageView
    public void recycle() {
        releaseAllCells();
    }

    @Override // com.miui.gallery.ui.pictures.view.IMultiImageView
    public void setRequestOptions(GlideOptions glideOptions) {
        this.mRequestOptions = glideOptions;
    }

    @Override // com.miui.gallery.ui.pictures.view.IMultiImageView
    public void setPreviewOptions(GlideOptions glideOptions) {
        this.mPreviewOptions = glideOptions;
    }

    public final void requestLayoutIfNecessary() {
        if (this.mActiveCells.size() <= 0 || !isLaidOut()) {
            return;
        }
        requestLayout();
    }

    public final void releaseAllCells() {
        Iterator<ImageCell> it = this.mActiveCells.iterator();
        while (it.hasNext()) {
            releaseCell(it.next());
        }
        this.mActiveCells.clear();
    }

    public final void releaseCell(ImageCell imageCell) {
        imageCell.setActive(false);
        if (imageCell.isRecyclable()) {
            getRecycledCellPoll().release(imageCell);
        }
    }

    public final ImageCell obtainCell() {
        ImageCell obtainFor = getRecycledCellPoll().obtainFor(this);
        obtainFor.setActive(true);
        obtainFor.setRecyclable(true);
        obtainFor.setResourceCallback(this);
        return obtainFor;
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        if (size <= 0) {
            throw new IllegalArgumentException("the width of view must be positive");
        }
        float calculateCellWidth = calculateCellWidth(size);
        int calculateLines = calculateLines(this.mActiveCells.size());
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(getPaddingTop() + getPaddingBottom() + (this.mSpacing * (calculateLines - 1)) + (Math.round(calculateCellWidth) * calculateLines), 1073741824));
    }

    @Override // android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        layoutCells();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator<ImageCell> it = this.mActiveCells.iterator();
        while (it.hasNext()) {
            it.next().onDraw(canvas);
        }
    }

    public final float calculateCellWidth(int i) {
        return (((i - (getPaddingStart() + getPaddingEnd())) - (this.mSpacing * (this.mSpanCount - 1))) * 1.0f) / this.mSpanCount;
    }

    public final int calculateLines(int i) {
        int i2 = this.mSpanCount;
        return ((i + i2) - 1) / i2;
    }

    public final void layoutCells() {
        int width = getWidth();
        if (this.mSpanCount <= 0 || width <= 0) {
            return;
        }
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        float calculateCellWidth = calculateCellWidth(width);
        float round = Math.round(calculateCellWidth);
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        int i = -1;
        Iterator<ImageCell> it = this.mActiveCells.iterator();
        float f = 0.0f;
        float f2 = 0.0f;
        while (it.hasNext()) {
            ImageCell next = it.next();
            i++;
            if (i == this.mSpanCount) {
                i = 0;
                f += this.mSpacing + round;
                f2 = 0.0f;
            }
            float f3 = isLayoutRtl ? width - ((paddingStart + f2) + calculateCellWidth) : paddingStart + f2;
            float f4 = paddingTop + f;
            RectF rectF = next.mFrame;
            if (rectF == null) {
                rectF = new RectF();
            }
            rectF.set(f3, f4, f3 + calculateCellWidth, f4 + round);
            next.setFrame(rectF);
            f2 += this.mSpacing + calculateCellWidth;
            width = width;
        }
    }

    @Override // com.miui.gallery.ui.pictures.view.ImageCell.RequestDrawingCallback
    public void requestDraw() {
        invalidate();
    }
}
