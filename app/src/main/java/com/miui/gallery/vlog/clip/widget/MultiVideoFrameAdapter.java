package com.miui.gallery.vlog.clip.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MultiVideoFrameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VideoFrameLoader.OnImageLoadedListener {
    public int mEmptyViewWidth;
    public int mItemWidth;
    public LinearLayoutManager mLayoutManager;
    public OnItemSelectedListener mListener;
    public double mPixelPerMs;
    public boolean mRequestRefresh;
    public int mScreenWidth;
    public int mThumbnailCorner;
    public int mThumbnailPadding;
    public int mTotalWidth;
    public VideoFrameLoader mVideoFrameLoader;
    public ArrayList<ThumbnailItem> mThumbnailItems = new ArrayList<>();
    public SparseIntArray mVideoEndPositionMap = new SparseIntArray();

    /* loaded from: classes2.dex */
    public static class CurrentScrollState {
        public int scrollX;
        public int seekPosition;
        public ThumbnailItem thumbnailItem;
        public int videoRectLeft;
    }

    /* loaded from: classes2.dex */
    public interface OnItemSelectedListener {
        void onItemLongClick(VideoClipInfo videoClipInfo);

        void onItemSelected(VideoClipInfo videoClipInfo);
    }

    /* loaded from: classes2.dex */
    public static class ThumbnailItem {
        public ThumbnailItem firstItem;
        public int index;
        public boolean isFirst;
        public boolean isLast;
        public VideoClipInfo owner;
        public int ownerIndex;
        public int scrollX;
        public long thumbnailTime;
        public int width;
    }

    public MultiVideoFrameAdapter(Context context, LinearLayoutManager linearLayoutManager, OnItemSelectedListener onItemSelectedListener, double d, int i) {
        this.mLayoutManager = linearLayoutManager;
        this.mListener = onItemSelectedListener;
        int curScreenWidth = ScreenUtils.getCurScreenWidth();
        this.mScreenWidth = curScreenWidth;
        this.mEmptyViewWidth = curScreenWidth / 2;
        this.mPixelPerMs = d;
        this.mItemWidth = context.getResources().getDimensionPixelSize(R$dimen.vlog_seek_bar_item_width);
        this.mThumbnailCorner = context.getResources().getDimensionPixelSize(R$dimen.vlog_seek_bar_thumbnail_corner);
        this.mThumbnailPadding = context.getResources().getDimensionPixelSize(R$dimen.vlog_seek_bar_thumbnail_padding);
    }

    public void updateScreenWidth(int i) {
        this.mScreenWidth = i;
    }

    public void setVideoFrameLoader(VideoFrameLoader videoFrameLoader) {
        this.mVideoFrameLoader = videoFrameLoader;
        videoFrameLoader.setListener(this);
    }

    public void setPixelPerMs(double d) {
        this.mPixelPerMs = d;
    }

    public void setEmptyViewWidth(int i) {
        this.mEmptyViewWidth = i;
    }

    public void updateDataList(List<VideoClipInfo> list) {
        long j;
        int i;
        List<VideoClipInfo> list2 = list;
        this.mThumbnailItems.clear();
        this.mVideoEndPositionMap.clear();
        int i2 = 0;
        this.mTotalWidth = 0;
        if (list2 == null) {
            return;
        }
        int i3 = this.mEmptyViewWidth;
        long j2 = (long) (this.mItemWidth / this.mPixelPerMs);
        int size = list.size();
        int i4 = 0;
        while (i4 < size) {
            VideoClipInfo videoClipInfo = list2.get(i4);
            int durationWithTransition = (int) ((videoClipInfo.getDurationWithTransition() * this.mPixelPerMs) + 0.5d);
            ThumbnailItem thumbnailItem = null;
            long trimIn = (long) (videoClipInfo.getTrimIn() / videoClipInfo.getSpeed());
            int i5 = i2;
            while (durationWithTransition > 0) {
                int i6 = i3;
                long speed = ((long) ((trimIn * videoClipInfo.getSpeed()) / j2)) * j2;
                long j3 = ((trimIn / j2) + 1) * j2;
                if (durationWithTransition <= this.mItemWidth) {
                    j = j2;
                    i = durationWithTransition;
                } else {
                    j = j2;
                    i = (int) ((j3 - trimIn) * this.mPixelPerMs);
                }
                durationWithTransition -= i;
                ThumbnailItem thumbnailItem2 = new ThumbnailItem();
                if (i5 == 0) {
                    thumbnailItem = thumbnailItem2;
                }
                thumbnailItem2.owner = videoClipInfo;
                thumbnailItem2.ownerIndex = i4;
                thumbnailItem2.thumbnailTime = speed;
                thumbnailItem2.width = i;
                thumbnailItem2.index = i5;
                thumbnailItem2.scrollX = i6;
                thumbnailItem2.firstItem = thumbnailItem;
                boolean z = true;
                thumbnailItem2.isFirst = i5 == 0;
                if (durationWithTransition > 0) {
                    z = false;
                }
                thumbnailItem2.isLast = z;
                this.mThumbnailItems.add(thumbnailItem2);
                i3 = i6 + i;
                i5++;
                this.mTotalWidth += i;
                trimIn = j3;
                j2 = j;
            }
            this.mVideoEndPositionMap.put(i4, i3);
            i4++;
            list2 = list;
            i2 = 0;
        }
        notifyDataSetChanged();
    }

    public VideoClipInfo getCurrentVideoClip() {
        CurrentScrollState currentScrollState = getCurrentScrollState();
        if (currentScrollState == null) {
            return null;
        }
        return currentScrollState.thumbnailItem.owner;
    }

    public CurrentScrollState getCurrentScrollState() {
        int findFirstVisibleItemPosition;
        if (!checkDataAndLayoutState() && (findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition()) != -1 && findFirstVisibleItemPosition != getItemCount() - 1) {
            int scrollX = getScrollX();
            int i = (this.mScreenWidth / 2) + scrollX;
            int size = this.mThumbnailItems.size();
            for (int i2 = findFirstVisibleItemPosition == 0 ? 0 : findFirstVisibleItemPosition - 1; i2 < size; i2++) {
                ThumbnailItem thumbnailItem = this.mThumbnailItems.get(i2);
                int i3 = thumbnailItem.scrollX;
                if (i3 <= i && i3 + thumbnailItem.width >= i) {
                    CurrentScrollState currentScrollState = new CurrentScrollState();
                    currentScrollState.thumbnailItem = thumbnailItem;
                    currentScrollState.videoRectLeft = thumbnailItem.firstItem.scrollX - scrollX;
                    currentScrollState.seekPosition = i - this.mEmptyViewWidth;
                    currentScrollState.scrollX = scrollX;
                    return currentScrollState;
                }
            }
        }
        return null;
    }

    public SparseIntArray getVideoEndPositionMap() {
        return this.mVideoEndPositionMap;
    }

    public int getScrollX(int i, float f) {
        int i2;
        int i3;
        if (f < 0.0f || f > 1.0f || (i3 = this.mVideoEndPositionMap.get(i, 0) - (i2 = this.mVideoEndPositionMap.get(i - 1, 0))) <= 0) {
            return 0;
        }
        return (int) ((i2 + (i3 * f)) - this.mEmptyViewWidth);
    }

    public int getScrollOffset(int i) {
        return i - getScrollX();
    }

    public int getScrollX() {
        int findFirstVisibleItemPosition;
        View findViewByPosition;
        ThumbnailItem dataItem;
        int i;
        if (!checkDataAndLayoutState() && (findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition()) != -1 && findFirstVisibleItemPosition <= getItemCount() - 1 && (findViewByPosition = this.mLayoutManager.findViewByPosition(findFirstVisibleItemPosition)) != null) {
            int i2 = -findViewByPosition.getLeft();
            if (findFirstVisibleItemPosition == getItemCount() - 1) {
                i = this.mEmptyViewWidth + this.mTotalWidth;
            } else if (findFirstVisibleItemPosition <= 0 || (dataItem = getDataItem(findFirstVisibleItemPosition)) == null) {
                return i2;
            } else {
                i = dataItem.scrollX;
            }
            return i2 + i;
        }
        return 0;
    }

    public final boolean checkDataAndLayoutState() {
        return this.mScreenWidth == 0 || getDataListSize() <= 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            View view = new View(viewGroup.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(this.mEmptyViewWidth, -1));
            return new EmptyHolder(view);
        }
        return new ThumbnailHolder(viewGroup);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType != 0) {
            if (itemViewType != 1) {
                return;
            }
            viewHolder.itemView.getLayoutParams().width = this.mEmptyViewWidth;
            return;
        }
        ThumbnailHolder thumbnailHolder = (ThumbnailHolder) viewHolder;
        ThumbnailItem dataItem = getDataItem(i);
        if (dataItem != null) {
            thumbnailHolder.setDataItem(dataItem);
            thumbnailHolder.setWidth(dataItem.width);
            thumbnailHolder.setCorner(dataItem.isFirst, dataItem.isLast);
            if (!this.mVideoFrameLoader.loadImage(thumbnailHolder.imageView, dataItem.owner.getFilePath(), this.mItemWidth, dataItem.thumbnailTime)) {
                this.mRequestRefresh = (!loadForwardCloseThumbnail(thumbnailHolder.imageView, i - 1)) | this.mRequestRefresh;
            }
        }
        thumbnailHolder.imageView.setImportantForAccessibility(2);
    }

    public final boolean loadForwardCloseThumbnail(ImageView imageView, int i) {
        int size = this.mThumbnailItems.size();
        for (int i2 = i - 1; i2 >= 0 && i2 < size; i2--) {
            ThumbnailItem thumbnailItem = this.mThumbnailItems.get(i2);
            if (this.mVideoFrameLoader.loadFromCache(imageView, thumbnailItem.owner.getFilePath(), this.mItemWidth, thumbnailItem.thumbnailTime)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader.OnImageLoadedListener
    public void onImageDisplayed() {
        if (this.mRequestRefresh) {
            notifyDataSetChanged();
            this.mRequestRefresh = false;
        }
    }

    public final ThumbnailItem getDataItem(int i) {
        int i2;
        ArrayList<ThumbnailItem> arrayList = this.mThumbnailItems;
        if (arrayList != null && i - 1 >= 0 && i2 < arrayList.size()) {
            return this.mThumbnailItems.get(i2);
        }
        return null;
    }

    public int getDataListSize() {
        ArrayList<ThumbnailItem> arrayList = this.mThumbnailItems;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return (i == 0 || i == getDataListSize() + 1) ? 1 : 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getDataListSize() + 2;
    }

    /* loaded from: classes2.dex */
    public class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View view) {
            super(view);
        }
    }

    /* loaded from: classes2.dex */
    public class ThumbnailHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ThumbnailItem dataItem;
        public ThumbnailView imageView;

        public ThumbnailHolder(View view) {
            super(new ThumbnailView(view.getContext(), MultiVideoFrameAdapter.this.mItemWidth, MultiVideoFrameAdapter.this.mThumbnailCorner, MultiVideoFrameAdapter.this.mThumbnailPadding));
            ThumbnailView thumbnailView = (ThumbnailView) this.itemView;
            this.imageView = thumbnailView;
            thumbnailView.setLayoutParams(new RecyclerView.LayoutParams(MultiVideoFrameAdapter.this.mItemWidth, -1));
            this.imageView.setScaleType(ImageView.ScaleType.MATRIX);
            this.imageView.setOnClickListener(this);
            this.imageView.setOnLongClickListener(this);
        }

        public void setDataItem(ThumbnailItem thumbnailItem) {
            this.dataItem = thumbnailItem;
        }

        public void setWidth(int i) {
            this.imageView.setWidth(i);
        }

        public void setCorner(boolean z, boolean z2) {
            this.imageView.setStartCorner(z);
            this.imageView.setEndCorner(z2);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (MultiVideoFrameAdapter.this.mListener == null || this.dataItem == null) {
                return;
            }
            MultiVideoFrameAdapter.this.mListener.onItemSelected(this.dataItem.owner);
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            if (MultiVideoFrameAdapter.this.mListener == null || this.dataItem == null) {
                return true;
            }
            MultiVideoFrameAdapter.this.mListener.onItemLongClick(this.dataItem.owner);
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public static class ThumbnailView extends ImageView {
        public int mCorner;
        public boolean mEndCorner;
        public Matrix mMatrix;
        public int mMaxWidth;
        public int mPadding;
        public Path mPath;
        public boolean mStartCorner;
        public int mWidth;

        public ThumbnailView(Context context, int i, int i2, int i3) {
            super(context);
            this.mPath = new Path();
            this.mCorner = i2;
            this.mPadding = i3;
            this.mMaxWidth = i;
        }

        public void setStartCorner(boolean z) {
            this.mStartCorner = z;
        }

        public void setEndCorner(boolean z) {
            this.mEndCorner = z;
        }

        public void setWidth(int i) {
            this.mWidth = i;
        }

        @Override // android.widget.ImageView
        public void setImageBitmap(Bitmap bitmap) {
            super.setImageBitmap(bitmap);
            this.mMatrix = null;
        }

        @Override // android.widget.ImageView, android.view.View
        public void onMeasure(int i, int i2) {
            setMeasuredDimension(this.mWidth, ImageView.getDefaultSize(getSuggestedMinimumHeight(), i2));
        }

        @Override // android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            float f;
            float f2;
            super.onLayout(z, i, i2, i3, i4);
            Drawable drawable = getDrawable();
            if ((z || this.mMatrix == null) && drawable != null) {
                if (this.mMatrix == null) {
                    this.mMatrix = new Matrix();
                }
                int i5 = i3 - i;
                int i6 = i4 - i2;
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                int i7 = intrinsicWidth * i6;
                int i8 = this.mMaxWidth;
                float f3 = 0.0f;
                if (i7 > i8 * intrinsicHeight) {
                    f = i6 / intrinsicHeight;
                    f2 = this.mEndCorner ? (i8 - (intrinsicWidth * f)) * 0.5f : i5 - (((intrinsicWidth * f) + i8) * 0.5f);
                } else {
                    float f4 = intrinsicWidth;
                    f = i8 / f4;
                    float f5 = (i6 - (intrinsicHeight * f)) * 0.5f;
                    if (!this.mEndCorner) {
                        f3 = i5 - (f4 * f);
                    }
                    f2 = f3;
                    f3 = f5;
                }
                this.mMatrix.setScale(f, f);
                this.mMatrix.postTranslate(Math.round(f2), Math.round(f3));
                setImageMatrix(this.mMatrix);
            }
        }

        @Override // android.widget.ImageView, android.view.View
        public void onDraw(Canvas canvas) {
            int i;
            int width = getWidth();
            int height = getHeight();
            if ((this.mStartCorner || this.mEndCorner) && width >= this.mPadding + this.mCorner) {
                this.mPath.reset();
                if (this.mStartCorner) {
                    this.mPath.moveTo(this.mPadding + this.mCorner, 0.0f);
                    Path path = this.mPath;
                    int i2 = this.mPadding;
                    path.quadTo(i2, 0.0f, i2, this.mCorner);
                    this.mPath.lineTo(this.mPadding, height - this.mCorner);
                    float f = height;
                    this.mPath.quadTo(this.mPadding, f, i + this.mCorner, f);
                } else {
                    this.mPath.moveTo(this.mPadding + this.mCorner, 0.0f);
                    this.mPath.lineTo(0.0f, 0.0f);
                    float f2 = height;
                    this.mPath.lineTo(0.0f, f2);
                    this.mPath.lineTo(this.mPadding + this.mCorner, f2);
                }
                if (this.mEndCorner) {
                    float f3 = height;
                    this.mPath.lineTo((width - this.mPadding) - this.mCorner, f3);
                    Path path2 = this.mPath;
                    int i3 = this.mPadding;
                    path2.quadTo(width - i3, f3, width - i3, height - this.mCorner);
                    this.mPath.lineTo(width - this.mPadding, this.mCorner);
                    Path path3 = this.mPath;
                    int i4 = this.mPadding;
                    path3.quadTo(width - i4, 0.0f, (width - i4) - this.mCorner, 0.0f);
                    this.mPath.lineTo(this.mPadding + this.mCorner, 0.0f);
                } else {
                    float f4 = width;
                    this.mPath.lineTo(f4, height);
                    this.mPath.lineTo(f4, 0.0f);
                    this.mPath.lineTo(this.mPadding + this.mCorner, 0.0f);
                }
                this.mPath.close();
                canvas.clipPath(this.mPath);
            }
            super.onDraw(canvas);
        }
    }
}
