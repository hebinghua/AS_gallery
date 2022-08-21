package com.miui.gallery.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoFrameThumbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context mContext;
    public List<Bitmap> mDataList;
    public int mDividerWidth;
    public int mEmptyViewWidth;
    public boolean mIsRtl;
    public int mItemWidth;
    public int mScreenWidth;
    public int mTotalWidth;

    public VideoFrameThumbAdapter(Context context) {
        this.mContext = context;
        this.mDividerWidth = context.getResources().getDimensionPixelSize(R.dimen.video_frame_seek_bar_divider);
        this.mItemWidth = context.getResources().getDimensionPixelSize(R.dimen.video_frame_thumb_width);
        int i = this.mDividerWidth;
        boolean z = true;
        if (i % 2 != 0) {
            this.mDividerWidth = i + 1;
        }
        this.mIsRtl = context.getResources().getConfiguration().getLayoutDirection() != 1 ? false : z;
    }

    public void configLayoutParams(int i) {
        if (i != this.mScreenWidth) {
            this.mScreenWidth = i;
            this.mEmptyViewWidth = (i - this.mDividerWidth) / 2;
            notifyDataSetChanged();
        }
    }

    public void updateDataList(List<Bitmap> list) {
        if (this.mDataList == null) {
            this.mDataList = new ArrayList();
        }
        this.mDataList.clear();
        this.mTotalWidth = 0;
        if (list != null) {
            this.mDataList.addAll(list);
            this.mTotalWidth = this.mItemWidth * list.size();
        }
        notifyDataSetChanged();
    }

    public int getTotalWidth() {
        return this.mTotalWidth - this.mDividerWidth;
    }

    public float getScrollPercent(RecyclerView recyclerView) {
        int currentOffset = getCurrentOffset(recyclerView);
        if (currentOffset <= 0) {
            return 0.0f;
        }
        int i = currentOffset - this.mEmptyViewWidth;
        int i2 = this.mDividerWidth;
        return Math.min(Math.max(this.mTotalWidth > i2 ? ((i - (i2 / 2)) * 1.0f) / getTotalWidth() : 0.0f, 0.0f), 1.0f);
    }

    public final int getCurrentOffset(RecyclerView recyclerView) {
        if (this.mScreenWidth == 0 || getDataListSize() <= 0 || recyclerView.getChildCount() == 0) {
            return 0;
        }
        View childAt = recyclerView.getChildAt(0);
        AbsViewHolder absViewHolder = (AbsViewHolder) recyclerView.getChildViewHolder(childAt);
        if (absViewHolder == null) {
            return 0;
        }
        int right = this.mIsRtl ? childAt.getRight() - this.mScreenWidth : -childAt.getLeft();
        int pos = absViewHolder.getPos();
        int itemViewType = getItemViewType(pos);
        if (itemViewType == 0) {
            return (this.mScreenWidth / 2) + right + ((pos - 1) * this.mItemWidth) + this.mEmptyViewWidth;
        }
        if (itemViewType == 1) {
            return (this.mScreenWidth / 2) + right;
        }
        return 0;
    }

    public int getScrollOffset(RecyclerView recyclerView, float f) {
        int currentOffset = getCurrentOffset(recyclerView);
        if (currentOffset <= 0) {
            return 0;
        }
        return ((int) (((getTotalWidth() * f) + (this.mDividerWidth / 2)) + this.mEmptyViewWidth)) - currentOffset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            View view = new View(this.mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(this.mEmptyViewWidth, -1));
            return new EmptyHolder(view);
        }
        return new FrameThumbHolder(viewGroup);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((AbsViewHolder) viewHolder).setPos(i);
        int itemViewType = getItemViewType(i);
        if (itemViewType == 0) {
            ((FrameThumbHolder) viewHolder).setBitmap(getDataItem(i));
        } else if (itemViewType != 1) {
        } else {
            viewHolder.itemView.getLayoutParams().width = this.mEmptyViewWidth;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return (i == 0 || i == getDataListSize() + 1) ? 1 : 0;
    }

    public int getDataListSize() {
        List<Bitmap> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public final Bitmap getDataItem(int i) {
        List<Bitmap> list = this.mDataList;
        if (list == null) {
            return null;
        }
        return list.get(i - 1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getDataListSize() + 2;
    }

    /* loaded from: classes2.dex */
    public static abstract class AbsViewHolder extends RecyclerView.ViewHolder {
        public int mPos;

        public AbsViewHolder(View view) {
            super(view);
            this.mPos = -1;
        }

        public void setPos(int i) {
            this.mPos = i;
        }

        public int getPos() {
            return this.mPos;
        }
    }

    /* loaded from: classes2.dex */
    public static class EmptyHolder extends AbsViewHolder {
        public EmptyHolder(View view) {
            super(view);
        }
    }

    /* loaded from: classes2.dex */
    public class FrameThumbHolder extends AbsViewHolder {
        public ImageView mImageView;

        public FrameThumbHolder(View view) {
            super(new ImageView(view.getContext()));
            ImageView imageView = (ImageView) this.itemView;
            this.mImageView = imageView;
            imageView.setLayoutParams(new RecyclerView.LayoutParams(VideoFrameThumbAdapter.this.mItemWidth, -1));
            this.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        public void setBitmap(Bitmap bitmap) {
            this.mImageView.setImageBitmap(bitmap);
        }
    }
}
